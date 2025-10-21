package service;

import model.ActivityLog;
import database.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for managing activity logs
 */
public class ActivityLogService {
    
    public void logActivity(ActivityLog activity) {
        String sql = "INSERT INTO activity_logs (user_id, username, action, details, bug_id, timestamp) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, activity.getUserId());
            pstmt.setString(2, activity.getUsername());
            pstmt.setString(3, activity.getAction());
            pstmt.setString(4, activity.getDetails());
            pstmt.setInt(5, activity.getBugId());
            pstmt.setString(6, activity.getTimestamp());
            
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Error logging activity: " + e.getMessage());
        }
    }
    
    public List<ActivityLog> getAllActivities() {
        List<ActivityLog> activities = new ArrayList<>();
        String sql = "SELECT * FROM activity_logs ORDER BY timestamp DESC LIMIT 100";
        
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                ActivityLog activity = new ActivityLog(
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("action"),
                    rs.getString("details"),
                    rs.getInt("bug_id")
                );
                activity.setId(rs.getInt("id"));
                activity.setTimestamp(rs.getString("timestamp"));
                activities.add(activity);
            }
            
        } catch (SQLException e) {
            System.err.println("Error fetching activities: " + e.getMessage());
        }
        
        return activities;
    }
    
    public List<ActivityLog> getActivitiesByBug(int bugId) {
        List<ActivityLog> activities = new ArrayList<>();
        String sql = "SELECT * FROM activity_logs WHERE bug_id = ? ORDER BY timestamp DESC";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, bugId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                ActivityLog activity = new ActivityLog(
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("action"),
                    rs.getString("details"),
                    rs.getInt("bug_id")
                );
                activity.setId(rs.getInt("id"));
                activity.setTimestamp(rs.getString("timestamp"));
                activities.add(activity);
            }
            
        } catch (SQLException e) {
            System.err.println("Error fetching bug activities: " + e.getMessage());
        }
        
        return activities;
    }
    
    public List<ActivityLog> getActivitiesByUser(int userId) {
        List<ActivityLog> activities = new ArrayList<>();
        String sql = "SELECT * FROM activity_logs WHERE user_id = ? ORDER BY timestamp DESC LIMIT 50";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                ActivityLog activity = new ActivityLog(
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("action"),
                    rs.getString("details"),
                    rs.getInt("bug_id")
                );
                activity.setId(rs.getInt("id"));
                activity.setTimestamp(rs.getString("timestamp"));
                activities.add(activity);
            }
            
        } catch (SQLException e) {
            System.err.println("Error fetching user activities: " + e.getMessage());
        }
        
        return activities;
    }
}
