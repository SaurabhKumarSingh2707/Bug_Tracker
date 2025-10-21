package util;

import java.sql.*;

/**
 * Simple utility to view database contents
 * Run this to see all your database data
 */
public class DatabaseViewer {
    
    public static void main(String[] args) {
        String url = "jdbc:sqlite:bugtracker.db";
        
        try (Connection conn = DriverManager.getConnection(url)) {
            System.out.println("=".repeat(100));
            System.out.println("DATABASE CONTENTS - Bug Tracker");
            System.out.println("=".repeat(100));
            
            // View Users
            viewUsers(conn);
            
            // View Bugs
            viewBugs(conn);
            
            // View Comments
            viewComments(conn);
            
            System.out.println("\n" + "=".repeat(100));
            System.out.println("END OF DATABASE CONTENTS");
            System.out.println("=".repeat(100));
            
        } catch (SQLException e) {
            System.err.println("Error connecting to database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void viewUsers(Connection conn) throws SQLException {
        System.out.println("\n╔══════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                    USERS TABLE                                   ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════════════════╝");
        
        String sql = "SELECT id, username, email, full_name, user_type, created_date FROM users";
        
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            System.out.println(String.format("%-5s %-20s %-30s %-25s %-12s %-20s", 
                "ID", "Username", "Email", "Full Name", "User Type", "Created Date"));
            System.out.println("-".repeat(120));
            
            int count = 0;
            while (rs.next()) {
                System.out.println(String.format("%-5d %-20s %-30s %-25s %-12s %-20s",
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("full_name"),
                    rs.getString("user_type"),
                    rs.getString("created_date")
                ));
                count++;
            }
            
            if (count == 0) {
                System.out.println("  (No users found)");
            } else {
                System.out.println("-".repeat(120));
                System.out.println("Total users: " + count);
            }
        }
    }
    
    private static void viewBugs(Connection conn) throws SQLException {
        System.out.println("\n╔══════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                    BUGS TABLE                                    ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════════════════╝");
        
        String sql = "SELECT id, title, priority, status, assigned_to, created_by, created_date FROM bugs";
        
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            System.out.println(String.format("%-5s %-40s %-12s %-15s %-15s %-12s %-20s", 
                "ID", "Title", "Priority", "Status", "Assigned To", "Created By", "Created Date"));
            System.out.println("-".repeat(130));
            
            int count = 0;
            while (rs.next()) {
                System.out.println(String.format("%-5d %-40s %-12s %-15s %-15s %-12d %-20s",
                    rs.getInt("id"),
                    truncate(rs.getString("title"), 38),
                    rs.getString("priority"),
                    rs.getString("status"),
                    truncate(rs.getString("assigned_to"), 13),
                    rs.getInt("created_by"),
                    rs.getString("created_date")
                ));
                count++;
            }
            
            if (count == 0) {
                System.out.println("  (No bugs found)");
            } else {
                System.out.println("-".repeat(130));
                System.out.println("Total bugs: " + count);
            }
        }
    }
    
    private static void viewComments(Connection conn) throws SQLException {
        System.out.println("\n╔══════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                 COMMENTS TABLE                                   ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════════════════╝");
        
        // Check if comments table exists
        try (Statement checkStmt = conn.createStatement();
             ResultSet rs = checkStmt.executeQuery(
                 "SELECT name FROM sqlite_master WHERE type='table' AND name='comments'")) {
            
            if (!rs.next()) {
                System.out.println("  (Comments table does not exist yet)");
                return;
            }
        }
        
        String sql = "SELECT comment_id, bug_id, user_id, comment_text, created_at FROM comments";
        
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            System.out.println(String.format("%-5s %-8s %-8s %-60s %-20s", 
                "ID", "Bug ID", "User ID", "Comment", "Created At"));
            System.out.println("-".repeat(120));
            
            int count = 0;
            while (rs.next()) {
                System.out.println(String.format("%-5d %-8d %-8d %-60s %-20s",
                    rs.getInt("comment_id"),
                    rs.getInt("bug_id"),
                    rs.getInt("user_id"),
                    truncate(rs.getString("comment_text"), 58),
                    rs.getString("created_at")
                ));
                count++;
            }
            
            if (count == 0) {
                System.out.println("  (No comments found)");
            } else {
                System.out.println("-".repeat(120));
                System.out.println("Total comments: " + count);
            }
        } catch (SQLException e) {
            // If comments table structure is different or doesn't have expected columns
            System.out.println("  (Comments table exists but may have different structure)");
        }
    }
    
    private static String truncate(String str, int maxLength) {
        if (str == null) return "";
        if (str.length() <= maxLength) return str;
        return str.substring(0, maxLength - 3) + "...";
    }
}
