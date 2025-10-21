package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Model class for activity logging
 */
public class ActivityLog {
    private int id;
    private int userId;
    private String username;
    private String action;
    private String details;
    private int bugId;
    private String timestamp;
    
    public ActivityLog(int userId, String username, String action, String details, int bugId) {
        this.userId = userId;
        this.username = username;
        this.action = action;
        this.details = details;
        this.bugId = bugId;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
    
    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
    
    public int getBugId() { return bugId; }
    public void setBugId(int bugId) { this.bugId = bugId; }
    
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    
    @Override
    public String toString() {
        return String.format("[%s] %s: %s - %s", timestamp, username, action, details);
    }
}
