package ru.pack.csps.util;

import java.util.List;

public class RestMethodApiDescription {
    private String url;
    private String modelName;
    //private String params;
    private List<ApiParameter> params;
    private String method;
    private String actionName;

    public RestMethodApiDescription(String url, String method, String actionName, String modelName, List<ApiParameter> params) {
        this.url = url;
        this.modelName = modelName;
        this.params = params;
        this.method = method;
        this.actionName = actionName;
    }

    public String getUrl() {
        return url;
    }

    public String getModelName() {
        return modelName;
    }

    public List<ApiParameter> getParams() {
        return params;
    }

    public String getMethod() {
        return method;
    }

    public String getActionName() {
        return actionName;
    }
}
