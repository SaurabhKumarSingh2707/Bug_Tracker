package com.bugtracker;

import com.bugtracker.model.UserRole;
import com.bugtracker.service.AuthService;
import com.bugtracker.service.BugService;
import com.bugtracker.service.UserService;
import com.bugtracker.util.DataStore;
import com.bugtracker.view.LoginFrame;
import javax.swing.*;

/**
 * Main application class for Bug Tracker
 */
public class BugTrackerApp {
    private static DataStore dataStore;
    private static UserService userService;
    private static BugService bugService;
    private static AuthService authService;

    public static void main(String[] args) {
        // Set Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Initialize services
        dataStore = new DataStore();
        userService = new UserService(dataStore);
        bugService = new BugService(dataStore);
        authService = new AuthService(userService);

        // Create default users if none exist
        if (userService.getAllUsers().isEmpty()) {
            createDefaultUsers();
        }

        // Start application
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame(authService, userService, bugService);
            loginFrame.setVisible(true);
        });
    }

    private static void createDefaultUsers() {
        try {
            authService.register("admin", "admin@bugtracker.com", "admin123", 
                "System Administrator", UserRole.ADMIN);
            authService.register("manager", "manager@bugtracker.com", "manager123", 
                "Project Manager", UserRole.MANAGER);
            authService.register("developer", "developer@bugtracker.com", "dev123", 
                "John Developer", UserRole.DEVELOPER);
            authService.register("tester", "tester@bugtracker.com", "test123", 
                "Jane Tester", UserRole.TESTER);
            
            System.out.println("=== Default Users Created ===");
            System.out.println("Admin - Username: admin, Password: admin123");
            System.out.println("Manager - Username: manager, Password: manager123");
            System.out.println("Developer - Username: developer, Password: dev123");
            System.out.println("Tester - Username: tester, Password: test123");
            System.out.println("============================");
        } catch (Exception e) {
            System.err.println("Error creating default users: " + e.getMessage());
        }
    }

    public static DataStore getDataStore() {
        return dataStore;
    }

    public static UserService getUserService() {
        return userService;
    }

    public static BugService getBugService() {
        return bugService;
    }

    public static AuthService getAuthService() {
        return authService;
    }
}
