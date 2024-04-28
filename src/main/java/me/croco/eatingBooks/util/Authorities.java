package me.croco.eatingBooks.util;

public enum Authorities {
    ROLE_USER("ROLE_USER"), ROLE_ADMIN("ROLE_ADMIN"), ROLE_DELETED("ROLE_DELETED");

    private final String name;

    private Authorities(String name) {
        this.name = name;
    }

    public String getAuthorityName() {
        return this.name;
    }
}
