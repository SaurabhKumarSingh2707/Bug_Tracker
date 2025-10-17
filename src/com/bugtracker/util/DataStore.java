package com.bugtracker.util;

import com.bugtracker.model.Bug;
import com.bugtracker.model.User;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data store for persisting users and bugs to files
 */
public class DataStore {
    private static final String DATA_DIR = "data";
    private static final String USERS_FILE = DATA_DIR + File.separator + "users.dat";
    private static final String BUGS_FILE = DATA_DIR + File.separator + "bugs.dat";

    private List<User> users;
    private List<Bug> bugs;

    public DataStore() {
        this.users = new ArrayList<>();
        this.bugs = new ArrayList<>();
        ensureDataDirectory();
        load();
    }

    private void ensureDataDirectory() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public void load() {
        loadUsers();
        loadBugs();
    }

    public void save() {
        saveUsers();
        saveBugs();
    }

    @SuppressWarnings("unchecked")
    private void loadUsers() {
        File file = new File(USERS_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                users = (List<User>) ois.readObject();
            } catch (Exception e) {
                System.err.println("Error loading users: " + e.getMessage());
                users = new ArrayList<>();
            }
        }
    }

    private void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USERS_FILE))) {
            oos.writeObject(users);
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void loadBugs() {
        File file = new File(BUGS_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                bugs = (List<Bug>) ois.readObject();
            } catch (Exception e) {
                System.err.println("Error loading bugs: " + e.getMessage());
                bugs = new ArrayList<>();
            }
        }
    }

    private void saveBugs() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BUGS_FILE))) {
            oos.writeObject(bugs);
        } catch (IOException e) {
            System.err.println("Error saving bugs: " + e.getMessage());
        }
    }

    // User operations
    public List<User> getUsers() {
        return new ArrayList<>(users);
    }

    public void addUser(User user) {
        users.add(user);
        save();
    }

    public void updateUser(User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUserId().equals(user.getUserId())) {
                users.set(i, user);
                save();
                return;
            }
        }
    }

    public boolean deleteUser(String userId) {
        boolean removed = users.removeIf(u -> u.getUserId().equals(userId));
        if (removed) save();
        return removed;
    }

    // Bug operations
    public List<Bug> getBugs() {
        return new ArrayList<>(bugs);
    }

    public void addBug(Bug bug) {
        bugs.add(bug);
        save();
    }

    public void updateBug(Bug bug) {
        for (int i = 0; i < bugs.size(); i++) {
            if (bugs.get(i).getBugId().equals(bug.getBugId())) {
                bugs.set(i, bug);
                save();
                return;
            }
        }
    }

    public boolean deleteBug(String bugId) {
        boolean removed = bugs.removeIf(b -> b.getBugId().equals(bugId));
        if (removed) save();
        return removed;
    }
}
