package util;

import java.sql.*;
import java.util.Scanner;

/**
 * Interactive database query tool
 * Allows running custom SQL queries from the command line
 */
public class QueryDatabase {
    
    public static void main(String[] args) {
        String url = "jdbc:sqlite:bugtracker.db";
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=".repeat(80));
        System.out.println("Bug Tracker - Interactive Query Tool");
        System.out.println("=".repeat(80));
        System.out.println();
        System.out.println("Commands:");
        System.out.println("  - Type SQL query and press Enter");
        System.out.println("  - Type 'help' for example queries");
        System.out.println("  - Type 'tables' to list all tables");
        System.out.println("  - Type 'exit' to quit");
        System.out.println();
        
        try (Connection conn = DriverManager.getConnection(url)) {
            
            while (true) {
                System.out.print("SQL> ");
                String input = scanner.nextLine().trim();
                
                if (input.isEmpty()) {
                    continue;
                }
                
                if (input.equalsIgnoreCase("exit") || input.equalsIgnoreCase("quit")) {
                    System.out.println("Goodbye!");
                    break;
                }
                
                if (input.equalsIgnoreCase("help")) {
                    showHelp();
                    continue;
                }
                
                if (input.equalsIgnoreCase("tables")) {
                    listTables(conn);
                    continue;
                }
                
                // Execute query
                executeQuery(conn, input);
            }
            
        } catch (SQLException e) {
            System.err.println("Error connecting to database: " + e.getMessage());
        }
        
        scanner.close();
    }
    
    private static void executeQuery(Connection conn, String sql) {
        try (Statement stmt = conn.createStatement()) {
            
            // Check if it's a SELECT query
            if (sql.trim().toUpperCase().startsWith("SELECT")) {
                ResultSet rs = stmt.executeQuery(sql);
                displayResults(rs);
                rs.close();
            } else {
                // For INSERT, UPDATE, DELETE
                int affected = stmt.executeUpdate(sql);
                System.out.println("✓ Query executed successfully. " + affected + " row(s) affected.");
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error executing query: " + e.getMessage());
        }
        System.out.println();
    }
    
    private static void displayResults(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        
        // Print column headers
        System.out.println("-".repeat(100));
        for (int i = 1; i <= columnCount; i++) {
            System.out.printf("%-20s ", metaData.getColumnName(i));
        }
        System.out.println();
        System.out.println("-".repeat(100));
        
        // Print rows
        int rowCount = 0;
        while (rs.next()) {
            for (int i = 1; i <= columnCount; i++) {
                String value = rs.getString(i);
                if (value == null) value = "NULL";
                if (value.length() > 18) value = value.substring(0, 15) + "...";
                System.out.printf("%-20s ", value);
            }
            System.out.println();
            rowCount++;
            
            // Limit display to 100 rows
            if (rowCount >= 100) {
                System.out.println("... (showing first 100 rows only)");
                break;
            }
        }
        
        System.out.println("-".repeat(100));
        System.out.println(rowCount + " row(s) returned");
    }
    
    private static void listTables(Connection conn) {
        System.out.println("\n📋 Database Tables:");
        System.out.println("-".repeat(40));
        
        String sql = "SELECT name FROM sqlite_master WHERE type='table' ORDER BY name";
        
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            int count = 0;
            while (rs.next()) {
                String tableName = rs.getString("name");
                if (!tableName.startsWith("sqlite_")) {
                    count++;
                    System.out.println(count + ". " + tableName);
                    
                    // Show row count
                    try (Statement countStmt = conn.createStatement();
                         ResultSet countRs = countStmt.executeQuery("SELECT COUNT(*) as cnt FROM " + tableName)) {
                        if (countRs.next()) {
                            System.out.println("   └─ " + countRs.getInt("cnt") + " rows");
                        }
                    }
                }
            }
            
            System.out.println("-".repeat(40));
            System.out.println();
            
        } catch (SQLException e) {
            System.err.println("Error listing tables: " + e.getMessage());
        }
    }
    
    private static void showHelp() {
        System.out.println("\n📚 Example Queries:");
        System.out.println("-".repeat(80));
        System.out.println();
        
        System.out.println("-- View all users");
        System.out.println("SELECT * FROM users;");
        System.out.println();
        
        System.out.println("-- View all bugs");
        System.out.println("SELECT id, title, priority, status FROM bugs;");
        System.out.println();
        
        System.out.println("-- Count bugs by status");
        System.out.println("SELECT status, COUNT(*) as count FROM bugs GROUP BY status;");
        System.out.println();
        
        System.out.println("-- Get CRITICAL bugs");
        System.out.println("SELECT * FROM bugs WHERE priority = 'CRITICAL';");
        System.out.println();
        
        System.out.println("-- Get bugs assigned to john_doe");
        System.out.println("SELECT * FROM bugs WHERE assigned_to = 'john_doe';");
        System.out.println();
        
        System.out.println("-- Recent activity logs");
        System.out.println("SELECT * FROM activity_logs ORDER BY timestamp DESC LIMIT 10;");
        System.out.println();
        
        System.out.println("-- Time spent by user");
        System.out.println("SELECT username, SUM(hours_spent) as total FROM time_logs GROUP BY username;");
        System.out.println();
        
        System.out.println("-- Bugs with user details");
        System.out.println("SELECT b.id, b.title, b.status, u.full_name");
        System.out.println("FROM bugs b JOIN users u ON b.assigned_to = u.username;");
        System.out.println();
        
        System.out.println("-".repeat(80));
        System.out.println();
    }
}
