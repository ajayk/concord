// @flow
import {combineReducers} from "redux";
import types from "./actions";

const testResult = (state: any = null, {type, response, error}: any) => {
    switch (type) {
        case types.REPOSITORY_TEST_RESPONSE:
            if (error) {
                return false;
            }
            return response;
        case types.REPOSITORY_TEST_REQUEST:
        case types.REPOSITORY_TEST_RESET:
            return null;
        default:
            return state;
    }
};

const loading = (state = false, {type}: any) => {
    switch (type) {
        case types.REPOSITORY_TEST_REQUEST:
            return true;
        case types.REPOSITORY_TEST_RESPONSE:
        case types.REPOSITORY_TEST_RESET:
            return false;
        default:
            return state;
    }
};

const error = (state: any = null, {type, error, message}: any) => {
    switch (type) {
        case types.REPOSITORY_TEST_RESPONSE:
            if (!error) {
                return null;
            }
            return message;
        case types.REPOSITORY_TEST_REQUEST:
        case types.REPOSITORY_TEST_RESET:
            return null;
        default:
            return state;
    }
};

const refreshResult = (state: any = null, {type, response, error}: any) => {
    switch (type) {
        case types.REPOSITORY_REFRESH_RESPONSE:
            if (error) {
                return false;
            }
            return response;
        case types.REPOSITORY_REFRESH_REQUEST:
            return null;
        default:
            return state;
    }
};

const refreshLoading = (state = false, {type}: any) => {
    switch (type) {
        case types.REPOSITORY_REFRESH_REQUEST:
            return true;
        case types.REPOSITORY_REFRESH_RESPONSE:
            return false;
        default:
            return state;
    }
};

const refreshError = (state: any = null, {type, error, message}: any) => {
    switch (type) {
        case types.REPOSITORY_REFRESH_RESPONSE:
            if (!error) {
                return null;
            }
            return message;
        case types.REPOSITORY_REFRESH_REQUEST:
            return null;
        default:
            return state;
    }
};

export default combineReducers({testResult, loading, error, refreshResult, refreshLoading, refreshError});

export const getTestResult = (state: any) => state.testResult;
export const isLoading = (state: any) => state.loading;
export const getError = (state: any) => state.error;

export const isRefreshLoading = (state: any) => state && state.refreshLoading;
export const getRefreshResult = (state: any) => state && state.refreshResult;
export const getRefreshError = (state: any) => state && state.refreshError;
