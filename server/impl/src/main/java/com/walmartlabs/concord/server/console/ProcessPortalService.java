package com.walmartlabs.concord.server.console;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/api/service/process_portal")
public interface ProcessPortalService {

    @GET
    @Path("/start")
    Response startProcess(@QueryParam("entryPoint") String entryPoint,
                          @QueryParam("activeProfiles") String activeProfiles,
                          @Context UriInfo uriInfo);
}
