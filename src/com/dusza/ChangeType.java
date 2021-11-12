package com.dusza;

public enum ChangeType {
    MODIFIED("MODOSIT"),
    CREATED("LETREHOZ"),
    DELETED("TOROL"),
    RECURSIVE_DELETE("REKURZIV-TOROL");

    private String type;

    public String getType() {
        return type;
    }

    ChangeType(String type) {
        this.type = type;
    }
}
