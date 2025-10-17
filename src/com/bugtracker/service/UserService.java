package com.bugtracker.service;

import com.bugtracker.model.User;
import com.bugtracker.util.DataStore;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service for managing users
 */
public class UserService {
    private final DataStore dataStore;

    public UserService(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public User createUser(User user) {
        dataStore.addUser(user);
        return user;
    }

    public User updateUser(User user) {
        dataStore.updateUser(user);
        return user;
    }

    public boolean deleteUser(String userId) {
        return dataStore.deleteUser(userId);
    }

    public Optional<User> getUserById(String userId) {
        return dataStore.getUsers().stream()
                .filter(u -> u.getUserId().equals(userId))
                .findFirst();
    }

    public Optional<User> getUserByUsername(String username) {
        return dataStore.getUsers().stream()
                .filter(u -> u.getUsername().equalsIgnoreCase(username))
                .findFirst();
    }

    public Optional<User> getUserByEmail(String email) {
        return dataStore.getUsers().stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    public List<User> getAllUsers() {
        return dataStore.getUsers();
    }

    public List<User> getActiveUsers() {
        return dataStore.getUsers().stream()
                .filter(User::isActive)
                .collect(Collectors.toList());
    }

    public List<User> searchUsers(String query) {
        String lowerQuery = query.toLowerCase();
        return dataStore.getUsers().stream()
                .filter(u -> u.getUsername().toLowerCase().contains(lowerQuery) ||
                        u.getFullName().toLowerCase().contains(lowerQuery) ||
                        u.getEmail().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
    }
}
