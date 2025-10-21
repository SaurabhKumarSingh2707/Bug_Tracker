package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Database manager for SQLite connection and initialization
 */
public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:bugtracker.db";
    private static Connection connection;
    
    // Get database connection
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection(DB_URL);
            } catch (ClassNotFoundException e) {
                throw new SQLException("SQLite JDBC driver not found", e);
            }
        }
        return connection;
    }
    
    // Initialize database tables
    public static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Create users table
            String createUsersTable = """
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT UNIQUE NOT NULL,
                    email TEXT UNIQUE NOT NULL,
                    password_hash TEXT NOT NULL,
                    full_name TEXT NOT NULL,
                    user_type TEXT NOT NULL DEFAULT 'DEVELOPER',
                    created_date TEXT NOT NULL,
                    last_login_date TEXT
                )
            """;
            stmt.execute(createUsersTable);
            
            // Migrate existing users table if user_type column doesn't exist
            try {
                String checkColumn = "SELECT user_type FROM users LIMIT 1";
                stmt.executeQuery(checkColumn);
            } catch (SQLException e) {
                // Column doesn't exist, add it
                System.out.println("Migrating users table: adding user_type column...");
                String alterTable = "ALTER TABLE users ADD COLUMN user_type TEXT NOT NULL DEFAULT 'DEVELOPER'";
                stmt.execute(alterTable);
                System.out.println("Migration completed: user_type column added.");
            }
            
            // Create bugs table
            String createBugsTable = """
                CREATE TABLE IF NOT EXISTS bugs (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    title TEXT NOT NULL,
                    description TEXT NOT NULL,
                    priority TEXT NOT NULL,
                    status TEXT NOT NULL,
                    assigned_to TEXT NOT NULL,
                    created_by INTEGER NOT NULL,
                    created_date TEXT NOT NULL,
                    updated_date TEXT NOT NULL,
                    FOREIGN KEY (created_by) REFERENCES users(id)
                )
            """;
            stmt.execute(createBugsTable);
            
            // Create activity_logs table
            String createActivityLogsTable = """
                CREATE TABLE IF NOT EXISTS activity_logs (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_id INTEGER NOT NULL,
                    username TEXT NOT NULL,
                    action TEXT NOT NULL,
                    details TEXT NOT NULL,
                    bug_id INTEGER,
                    timestamp TEXT NOT NULL,
                    FOREIGN KEY (user_id) REFERENCES users(id),
                    FOREIGN KEY (bug_id) REFERENCES bugs(id)
                )
            """;
            stmt.execute(createActivityLogsTable);
            
            // Create tags table
            String createTagsTable = """
                CREATE TABLE IF NOT EXISTS tags (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT UNIQUE NOT NULL,
                    color TEXT NOT NULL
                )
            """;
            stmt.execute(createTagsTable);
            
            // Create bug_tags junction table
            String createBugTagsTable = """
                CREATE TABLE IF NOT EXISTS bug_tags (
                    bug_id INTEGER NOT NULL,
                    tag_id INTEGER NOT NULL,
                    PRIMARY KEY (bug_id, tag_id),
                    FOREIGN KEY (bug_id) REFERENCES bugs(id),
                    FOREIGN KEY (tag_id) REFERENCES tags(id)
                )
            """;
            stmt.execute(createBugTagsTable);
            
            // Create attachments table
            String createAttachmentsTable = """
                CREATE TABLE IF NOT EXISTS attachments (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    bug_id INTEGER NOT NULL,
                    file_name TEXT NOT NULL,
                    file_path TEXT NOT NULL,
                    file_size INTEGER NOT NULL,
                    uploaded_by TEXT NOT NULL,
                    upload_date TEXT NOT NULL,
                    FOREIGN KEY (bug_id) REFERENCES bugs(id)
                )
            """;
            stmt.execute(createAttachmentsTable);
            
            // Create time_logs table
            String createTimeLogsTable = """
                CREATE TABLE IF NOT EXISTS time_logs (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    bug_id INTEGER NOT NULL,
                    user_id INTEGER NOT NULL,
                    username TEXT NOT NULL,
                    hours_spent REAL NOT NULL,
                    description TEXT,
                    log_date TEXT NOT NULL,
                    FOREIGN KEY (bug_id) REFERENCES bugs(id),
                    FOREIGN KEY (user_id) REFERENCES users(id)
                )
            """;
            stmt.execute(createTimeLogsTable);
            
            // Create comments table
            String createCommentsTable = """
                CREATE TABLE IF NOT EXISTS comments (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    bug_id INTEGER NOT NULL,
                    user_id INTEGER NOT NULL,
                    username TEXT NOT NULL,
                    comment_text TEXT NOT NULL,
                    created_date TEXT NOT NULL,
                    FOREIGN KEY (bug_id) REFERENCES bugs(id),
                    FOREIGN KEY (user_id) REFERENCES users(id)
                )
            """;
            stmt.execute(createCommentsTable);
            
            System.out.println("Database initialized successfully!");
            
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Close database connection
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }
}
