package service;

import model.User;
import model.ActivityLog;
import database.UserDAO;

/**
 * Service class for authentication operations
 */
public class AuthService {
    private static User currentUser;
    private static ActivityLogService activityLogService = new ActivityLogService();
    
    // Get the currently logged in user
    public static User getCurrentUser() {
        return currentUser;
    }
    
    // Login user
    public static boolean login(String username, String password) {
        User user = UserDAO.loginUser(username, password);
        if (user != null) {
            currentUser = user;
            
            // Log activity
            ActivityLog log = new ActivityLog(
                user.getId(),
                user.getUsername(),
                "USER_LOGIN",
                String.format("User '%s' logged in [Role: %s]", user.getUsername(), user.getUserType()),
                0
            );
            activityLogService.logActivity(log);
            
            return true;
        }
        return false;
    }
    
    // Register new user
    public static boolean register(String username, String email, String password, String fullName, User.UserType userType) {
        // Validate inputs
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        if (password == null || password.length() < 6) {
            return false;
        }
        if (fullName == null || fullName.trim().isEmpty()) {
            return false;
        }
        if (userType == null) {
            userType = User.UserType.DEVELOPER; // Default
        }
        
        // Check if username or email already exists
        if (UserDAO.usernameExists(username)) {
            return false;
        }
        if (UserDAO.emailExists(email)) {
            return false;
        }
        
        // Create new user
        String passwordHash = UserDAO.hashPassword(password);
        User user = new User(username, email, passwordHash, fullName, userType);
        
        boolean registered = UserDAO.registerUser(user);
        
        if (registered) {
            // Log activity
            ActivityLog log = new ActivityLog(
                0,  // System action
                "SYSTEM",
                "USER_REGISTERED",
                String.format("New user registered: '%s' (%s) [Role: %s]", username, email, userType),
                0
            );
            activityLogService.logActivity(log);
        }
        
        return registered;
    }
    
    // Logout user
    public static void logout() {
        if (currentUser != null) {
            // Log activity
            ActivityLog log = new ActivityLog(
                currentUser.getId(),
                currentUser.getUsername(),
                "USER_LOGOUT",
                String.format("User '%s' logged out", currentUser.getUsername()),
                0
            );
            activityLogService.logActivity(log);
            
            currentUser = null;
        }
    }
    
    // Check if user is logged in
    public static boolean isLoggedIn() {
        return currentUser != null;
    }
}
