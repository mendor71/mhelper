package ru.pack.csps.util;

import java.util.List;

public class RestControllerApiDescription {
    private String controllerName;
    private List<RestMethodApiDescription> methodList;

    public RestControllerApiDescription(String controllerName) {
        this.controllerName = controllerName;
    }

    public RestControllerApiDescription(String controllerName, List<RestMethodApiDescription> methodList) {
        this.controllerName = controllerName;
        this.methodList = methodList;
    }

    public String getControllerName() {
        return controllerName;
    }

    public void setControllerName(String controllerName) {
        this.controllerName = controllerName;
    }

    public List<RestMethodApiDescription> getMethodList() {
        return methodList;
    }

    public void setMethodList(List<RestMethodApiDescription> methodList) {
        this.methodList = methodList;
    }
}
