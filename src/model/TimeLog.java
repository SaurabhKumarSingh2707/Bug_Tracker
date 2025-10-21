package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Model class for time tracking
 */
public class TimeLog {
    private int id;
    private int bugId;
    private int userId;
    private String username;
    private double hoursSpent;
    private String description;
    private String logDate;
    
    public TimeLog(int bugId, int userId, String username, double hoursSpent, String description) {
        this.bugId = bugId;
        this.userId = userId;
        this.username = username;
        this.hoursSpent = hoursSpent;
        this.description = description;
        this.logDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
    
    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getBugId() { return bugId; }
    public void setBugId(int bugId) { this.bugId = bugId; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public double getHoursSpent() { return hoursSpent; }
    public void setHoursSpent(double hoursSpent) { this.hoursSpent = hoursSpent; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getLogDate() { return logDate; }
    public void setLogDate(String logDate) { this.logDate = logDate; }
}
