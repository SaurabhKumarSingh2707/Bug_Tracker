package database;

import model.Bug;
import model.Bug.Priority;
import model.Bug.Status;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Bug operations
 */
public class BugDAO {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    // Create a new bug
    public static Bug createBug(Bug bug, int createdByUserId) {
        String sql = "INSERT INTO bugs (title, description, priority, status, assigned_to, created_by, created_date, updated_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, bug.getTitle());
            pstmt.setString(2, bug.getDescription());
            pstmt.setString(3, bug.getPriority().name());
            pstmt.setString(4, bug.getStatus().name());
            pstmt.setString(5, bug.getAssignedTo());
            pstmt.setInt(6, createdByUserId);
            pstmt.setString(7, LocalDateTime.now().format(formatter));
            pstmt.setString(8, LocalDateTime.now().format(formatter));
            
            pstmt.executeUpdate();
            
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return getBugById(rs.getInt(1));
            }
            
        } catch (SQLException e) {
            System.err.println("Error creating bug: " + e.getMessage());
        }
        
        return null;
    }
    
    // Get all bugs
    public static List<Bug> getAllBugs() {
        List<Bug> bugs = new ArrayList<>();
        String sql = "SELECT * FROM bugs ORDER BY id DESC";
        
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                bugs.add(extractBugFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all bugs: " + e.getMessage());
        }
        
        return bugs;
    }
    
    // Get bugs created by a specific user
    public static List<Bug> getBugsByUser(int userId) {
        List<Bug> bugs = new ArrayList<>();
        String sql = "SELECT * FROM bugs WHERE created_by = ? ORDER BY id DESC";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                bugs.add(extractBugFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting bugs by user: " + e.getMessage());
        }
        
        return bugs;
    }
    
    // Get bug by ID
    public static Bug getBugById(int id) {
        String sql = "SELECT * FROM bugs WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractBugFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting bug by ID: " + e.getMessage());
        }
        
        return null;
    }
    
    // Update bug
    public static boolean updateBug(Bug bug) {
        String sql = "UPDATE bugs SET title = ?, description = ?, priority = ?, status = ?, assigned_to = ?, updated_date = ? WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, bug.getTitle());
            pstmt.setString(2, bug.getDescription());
            pstmt.setString(3, bug.getPriority().name());
            pstmt.setString(4, bug.getStatus().name());
            pstmt.setString(5, bug.getAssignedTo());
            pstmt.setString(6, LocalDateTime.now().format(formatter));
            pstmt.setInt(7, bug.getId());
            
            pstmt.executeUpdate();
            return true;
            
        } catch (SQLException e) {
            System.err.println("Error updating bug: " + e.getMessage());
            return false;
        }
    }
    
    // Delete bug
    public static boolean deleteBug(int id) {
        String sql = "DELETE FROM bugs WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            return true;
            
        } catch (SQLException e) {
            System.err.println("Error deleting bug: " + e.getMessage());
            return false;
        }
    }
    
    // Search bugs
    public static List<Bug> searchBugs(String keyword) {
        List<Bug> bugs = new ArrayList<>();
        String sql = "SELECT * FROM bugs WHERE LOWER(title) LIKE ? OR LOWER(description) LIKE ? ORDER BY id DESC";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + keyword.toLowerCase() + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                bugs.add(extractBugFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error searching bugs: " + e.getMessage());
        }
        
        return bugs;
    }
    
    // Filter bugs by status
    public static List<Bug> filterByStatus(Status status) {
        List<Bug> bugs = new ArrayList<>();
        String sql = "SELECT * FROM bugs WHERE status = ? ORDER BY id DESC";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status.name());
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                bugs.add(extractBugFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error filtering bugs by status: " + e.getMessage());
        }
        
        return bugs;
    }
    
    // Filter bugs by priority
    public static List<Bug> filterByPriority(Priority priority) {
        List<Bug> bugs = new ArrayList<>();
        String sql = "SELECT * FROM bugs WHERE priority = ? ORDER BY id DESC";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, priority.name());
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                bugs.add(extractBugFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error filtering bugs by priority: " + e.getMessage());
        }
        
        return bugs;
    }
    
    // Extract Bug object from ResultSet
    private static Bug extractBugFromResultSet(ResultSet rs) throws SQLException {
        Bug bug = new Bug(
            rs.getString("title"),
            rs.getString("description"),
            Priority.valueOf(rs.getString("priority")),
            rs.getString("assigned_to")
        );
        
        // Use reflection to set the ID (since Bug class generates IDs automatically)
        try {
            java.lang.reflect.Field idField = Bug.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(bug, rs.getInt("id"));
            
            java.lang.reflect.Field statusField = Bug.class.getDeclaredField("status");
            statusField.setAccessible(true);
            statusField.set(bug, Status.valueOf(rs.getString("status")));
            
            java.lang.reflect.Field createdDateField = Bug.class.getDeclaredField("createdDate");
            createdDateField.setAccessible(true);
            String createdDateStr = rs.getString("created_date");
            if (createdDateStr != null) {
                createdDateField.set(bug, LocalDateTime.parse(createdDateStr, formatter));
            }
            
            java.lang.reflect.Field updatedDateField = Bug.class.getDeclaredField("updatedDate");
            updatedDateField.setAccessible(true);
            String updatedDateStr = rs.getString("updated_date");
            if (updatedDateStr != null) {
                updatedDateField.set(bug, LocalDateTime.parse(updatedDateStr, formatter));
            }
            
        } catch (Exception e) {
            System.err.println("Error setting bug fields: " + e.getMessage());
        }
        
        return bug;
    }
}
