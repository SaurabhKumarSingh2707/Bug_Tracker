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
            
            // View Activity Logs
            viewActivityLogs(conn);
            
            // View Time Logs
            viewTimeLogs(conn);
            
            // View Comments
            viewComments(conn);
            
            // View Attachments
            viewAttachments(conn);
            
            // View Tags
            viewTags(conn);
            
            // Summary Statistics
            viewSummary(conn);
            
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
    
    private static void viewActivityLogs(Connection conn) throws SQLException {
        System.out.println("\n╔══════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                              ACTIVITY LOGS TABLE                                 ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════════════════╝");
        
        String sql = "SELECT id, username, action, bug_id, timestamp FROM activity_logs ORDER BY timestamp DESC LIMIT 20";
        
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            System.out.println(String.format("%-5s %-20s %-20s %-10s %-20s", 
                "ID", "Username", "Action", "Bug ID", "Timestamp"));
            System.out.println("-".repeat(80));
            
            int count = 0;
            while (rs.next()) {
                System.out.println(String.format("%-5d %-20s %-20s %-10d %-20s",
                    rs.getInt("id"),
                    truncate(rs.getString("username"), 18),
                    truncate(rs.getString("action"), 18),
                    rs.getInt("bug_id"),
                    rs.getString("timestamp")
                ));
                count++;
            }
            
            if (count == 0) {
                System.out.println("  (No activity logs found)");
            } else {
                System.out.println("-".repeat(80));
                System.out.println("Showing latest 20 activity logs");
            }
        } catch (SQLException e) {
            System.out.println("  (Activity logs table may not exist or have different structure)");
        }
    }
    
    private static void viewTimeLogs(Connection conn) throws SQLException {
        System.out.println("\n╔══════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                TIME LOGS TABLE                                   ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════════════════╝");
        
        String sql = "SELECT id, bug_id, username, hours_spent, description, log_date FROM time_logs";
        
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            System.out.println(String.format("%-5s %-8s %-15s %-10s %-40s %-20s", 
                "ID", "Bug ID", "Username", "Hours", "Description", "Date"));
            System.out.println("-".repeat(110));
            
            int count = 0;
            double totalHours = 0;
            while (rs.next()) {
                double hours = rs.getDouble("hours_spent");
                totalHours += hours;
                System.out.println(String.format("%-5d %-8d %-15s %-10.2f %-40s %-20s",
                    rs.getInt("id"),
                    rs.getInt("bug_id"),
                    truncate(rs.getString("username"), 13),
                    hours,
                    truncate(rs.getString("description"), 38),
                    rs.getString("log_date")
                ));
                count++;
            }
            
            if (count == 0) {
                System.out.println("  (No time logs found)");
            } else {
                System.out.println("-".repeat(110));
                System.out.println(String.format("Total time logged: %.2f hours (%d entries)", totalHours, count));
            }
        } catch (SQLException e) {
            System.out.println("  (Time logs table may not exist or have different structure)");
        }
    }
    
    private static void viewAttachments(Connection conn) throws SQLException {
        System.out.println("\n╔══════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                               ATTACHMENTS TABLE                                  ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════════════════╝");
        
        String sql = "SELECT id, bug_id, file_name, file_size, uploaded_by, upload_date FROM attachments";
        
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            System.out.println(String.format("%-5s %-8s %-30s %-12s %-15s %-20s", 
                "ID", "Bug ID", "File Name", "Size (KB)", "Uploaded By", "Upload Date"));
            System.out.println("-".repeat(100));
            
            int count = 0;
            while (rs.next()) {
                System.out.println(String.format("%-5d %-8d %-30s %-12.2f %-15s %-20s",
                    rs.getInt("id"),
                    rs.getInt("bug_id"),
                    truncate(rs.getString("file_name"), 28),
                    rs.getInt("file_size") / 1024.0,
                    truncate(rs.getString("uploaded_by"), 13),
                    rs.getString("upload_date")
                ));
                count++;
            }
            
            if (count == 0) {
                System.out.println("  (No attachments found)");
            } else {
                System.out.println("-".repeat(100));
                System.out.println("Total attachments: " + count);
            }
        } catch (SQLException e) {
            System.out.println("  (Attachments table may not exist or have different structure)");
        }
    }
    
    private static void viewTags(Connection conn) throws SQLException {
        System.out.println("\n╔══════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                  TAGS TABLE                                      ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════════════════╝");
        
        String sql = "SELECT id, name, color FROM tags";
        
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            System.out.println(String.format("%-5s %-30s %-15s", 
                "ID", "Tag Name", "Color"));
            System.out.println("-".repeat(60));
            
            int count = 0;
            while (rs.next()) {
                System.out.println(String.format("%-5d %-30s %-15s",
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("color")
                ));
                count++;
            }
            
            if (count == 0) {
                System.out.println("  (No tags found)");
            } else {
                System.out.println("-".repeat(60));
                System.out.println("Total tags: " + count);
            }
        } catch (SQLException e) {
            System.out.println("  (Tags table may not exist or have different structure)");
        }
    }
    
    private static void viewSummary(Connection conn) throws SQLException {
        System.out.println("\n╔══════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                              SUMMARY STATISTICS                                  ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════════════════╝");
        
        // Count users
        int userCount = getCount(conn, "users");
        System.out.println("👥 Total Users: " + userCount);
        
        // Count bugs
        int bugCount = getCount(conn, "bugs");
        System.out.println("🐛 Total Bugs: " + bugCount);
        
        // Bugs by status
        System.out.println("\n📊 Bugs by Status:");
        countByField(conn, "bugs", "status");
        
        // Bugs by priority
        System.out.println("\n🔥 Bugs by Priority:");
        countByField(conn, "bugs", "priority");
        
        // Activity count
        int activityCount = getCount(conn, "activity_logs");
        System.out.println("\n📝 Total Activity Logs: " + activityCount);
        
        // Time logged
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT SUM(hours_spent) as total FROM time_logs")) {
            if (rs.next()) {
                double totalHours = rs.getDouble("total");
                System.out.println("⏱️  Total Time Logged: " + String.format("%.2f hours", totalHours));
            }
        } catch (SQLException e) {
            // Time logs table may not exist
        }
    }
    
    private static int getCount(Connection conn, String tableName) {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM " + tableName)) {
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            // Table may not exist
        }
        return 0;
    }
    
    private static void countByField(Connection conn, String tableName, String fieldName) {
        String sql = String.format("SELECT %s, COUNT(*) as count FROM %s GROUP BY %s", 
                                  fieldName, tableName, fieldName);
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println(String.format("  • %-15s: %d", 
                    rs.getString(fieldName), rs.getInt("count")));
            }
        } catch (SQLException e) {
            // Field may not exist
        }
    }
    
    private static String truncate(String str, int maxLength) {
        if (str == null) return "";
        if (str.length() <= maxLength) return str;
        return str.substring(0, maxLength - 3) + "...";
    }
}
