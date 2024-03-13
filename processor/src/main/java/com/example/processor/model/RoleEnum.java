package com.example.processor.model;

public enum RoleEnum {
    ADMIN("ADMIN"), USER("USER");

    private final String type;

    RoleEnum(String string) {
        type = string;
    }

    @Override
    public String toString() {
        return type;
    }

}
