package ru.pack.csps.util;

public class ApiParameter {
    private String name;
    private String type;
    private boolean pathVariable;

    public ApiParameter() {}

    public ApiParameter(String name, String type, boolean pathVariable) {
        this.name = name;
        this.type = type;
        this.pathVariable = pathVariable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isPathVariable() {
        return pathVariable;
    }

    public void setPathVariable(boolean pathVariable) {
        this.pathVariable = pathVariable;
    }
}
