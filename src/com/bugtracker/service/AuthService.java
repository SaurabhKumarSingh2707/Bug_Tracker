package com.bugtracker.service;

import com.bugtracker.model.User;
import com.bugtracker.model.UserRole;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Service for handling user authentication
 */
public class AuthService {
    private final UserService userService;
    private User currentUser;

    public AuthService(UserService userService) {
        this.userService = userService;
    }

    public User register(String username, String email, String password, 
                        String fullName, UserRole role) {
        // Check if username already exists
        if (userService.getUserByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        // Check if email already exists
        if (userService.getUserByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Create user
        User user = new User(UUID.randomUUID().toString(), username, email, 
                           password, fullName, role);
        userService.createUser(user);
        return user;
    }

    public boolean login(String username, String password) {
        Optional<User> userOpt = userService.getUserByUsername(username);

        if (userOpt.isEmpty()) {
            return false;
        }

        User user = userOpt.get();

        if (!user.isActive()) {
            throw new IllegalStateException("User account is deactivated");
        }

        // Verify password (in production, use hashed passwords)
        if (password.equals(user.getPassword())) {
            user.setLastLogin(LocalDateTime.now());
            userService.updateUser(user);
            this.currentUser = user;
            return true;
        }

        return false;
    }

    public void logout() {
        this.currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public boolean changePassword(String oldPassword, String newPassword) {
        if (!isLoggedIn()) {
            return false;
        }

        if (!oldPassword.equals(currentUser.getPassword())) {
            return false;
        }

        currentUser.setPassword(newPassword);
        userService.updateUser(currentUser);
        return true;
    }

    public boolean hasRole(UserRole role) {
        return isLoggedIn() && currentUser.getRole() == role;
    }

    public boolean isAdmin() {
        return hasRole(UserRole.ADMIN);
    }
}
