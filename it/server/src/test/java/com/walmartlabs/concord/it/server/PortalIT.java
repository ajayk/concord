package com.walmartlabs.concord.it.server;

import com.walmartlabs.concord.server.api.process.ProcessEntry;
import com.walmartlabs.concord.server.api.project.ProjectEntry;
import com.walmartlabs.concord.server.api.project.ProjectResource;
import com.walmartlabs.concord.server.api.project.UpdateRepositoryRequest;
import com.walmartlabs.concord.server.api.security.secret.SecretResource;
import com.walmartlabs.concord.server.console.ProcessPortalService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.walmartlabs.concord.it.common.ServerClient.assertLog;
import static java.util.Collections.singletonMap;
import static org.junit.Assert.assertEquals;

public class PortalIT extends AbstractServerIT {

    private MockGitSshServer gitServer;
    private int gitPort;

    @Before
    public void setUp() throws Exception {
        Path data = Paths.get(PortalIT.class.getResource("portal").toURI());
        Path repo = GitUtils.createBareRepository(data);

        gitServer = new MockGitSshServer(0, repo.toAbsolutePath().toString());
        gitServer.start();

        gitPort = gitServer.getPort();
    }

    @After
    public void tearDown() throws Exception {
        gitServer.stop();
    }

    @Test
    public void test() throws Exception {
        String projectName = "project@" + System.currentTimeMillis();
        String repoSecretName = "repoSecret@" + System.currentTimeMillis();
        String repoName = "repo@" + System.currentTimeMillis();
        String repoUrl = String.format(ITConstants.GIT_SERVER_URL_PATTERN, gitPort);

        // ---

        SecretResource secretResource = proxy(SecretResource.class);
        secretResource.createKeyPair(repoSecretName);

        // ---

        UpdateRepositoryRequest repo = new UpdateRepositoryRequest(repoUrl, "master", null, repoSecretName);
        ProjectResource projectResource = proxy(ProjectResource.class);
        projectResource.createOrUpdate(new ProjectEntry(projectName, null, singletonMap(repoName, repo), null));

        // ---

        ProcessPortalService portalService = proxy(ProcessPortalService.class);
        Response resp = portalService.startProcess(projectName + ":" + repoName + ":main", "test1,test2", null);
        assertEquals(200, resp.getStatus());

        ProcessEntry pe = resp.readEntity(ProcessEntry.class);

        // ---

        byte[] ab = getLog(pe.getLogFileName());
        assertLog(".*Hello, world.*", ab);
        assertLog(".*x=123.*", ab);
    }
}
