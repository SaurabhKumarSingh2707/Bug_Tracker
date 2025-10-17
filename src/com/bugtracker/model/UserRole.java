package com.bugtracker.model;

/**
 * Enum representing different user roles in the system
 */
public enum UserRole {
    ADMIN("Administrator"),
    MANAGER("Project Manager"),
    DEVELOPER("Developer"),
    TESTER("Tester"),
    VIEWER("Viewer");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
