package util;

import database.DatabaseManager;
import database.UserDAO;
import database.BugDAO;
import model.User;
import model.Bug;
import model.Bug.Priority;
import java.time.LocalDateTime;

/**
 * Utility class to populate the database with sample bugs for testing
 */
public class SampleDataPopulator {
    
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("Sample Data Populator - Bug Tracker");
        System.out.println("========================================\n");
        
        // Initialize database
        DatabaseManager.initializeDatabase();
        
        // Create sample users if they don't exist
        createSampleUsers();
        
        // Create sample bugs
        createSampleBugs();
        
        System.out.println("\n========================================");
        System.out.println("Sample data population completed!");
        System.out.println("========================================");
    }
    
    private static void createSampleUsers() {
        System.out.println("Creating sample users...");
        
        String[][] users = {
            {"admin", "admin@bugtracker.com", "Admin123", "System Administrator", "ADMIN"},
            {"john_doe", "john.doe@company.com", "John123", "John Doe", "DEVELOPER"},
            {"jane_smith", "jane.smith@company.com", "Jane123", "Jane Smith", "DEVELOPER"},
            {"mike_tester", "mike.test@company.com", "Mike123", "Mike Wilson", "TESTER"},
            {"sarah_manager", "sarah.mgr@company.com", "Sarah123", "Sarah Johnson", "MANAGER"},
            {"bob_dev", "bob.dev@company.com", "Bob123", "Bob Anderson", "DEVELOPER"},
            {"alice_qa", "alice.qa@company.com", "Alice123", "Alice Brown", "TESTER"}
        };
        
        int created = 0;
        for (String[] userData : users) {
            if (!UserDAO.usernameExists(userData[0])) {
                User user = new User();
                user.setUsername(userData[0]);
                user.setEmail(userData[1]);
                user.setPasswordHash(UserDAO.hashPassword(userData[2]));
                user.setFullName(userData[3]);
                user.setUserType(User.UserType.valueOf(userData[4]));
                
                if (UserDAO.registerUser(user)) {
                    System.out.println("  ✓ Created user: " + userData[0] + " (Password: " + userData[2] + ")");
                    created++;
                }
            } else {
                System.out.println("  ⊝ User already exists: " + userData[0]);
            }
        }
        
        System.out.println("Created " + created + " new users.\n");
    }
    
    private static void createSampleBugs() {
        System.out.println("Creating sample bugs...");
        
        // Get user IDs
        User admin = UserDAO.getUserByUsername("admin");
        User john = UserDAO.getUserByUsername("john_doe");
        User jane = UserDAO.getUserByUsername("jane_smith");
        User mike = UserDAO.getUserByUsername("mike_tester");
        User sarah = UserDAO.getUserByUsername("sarah_manager");
        
        if (admin == null) {
            System.err.println("  ✗ Admin user not found. Cannot create bugs.");
            return;
        }
        
        // Define sample bugs with different types, priorities, and assignments
        Object[][] bugs = {
            // {title, description, priority, assignedTo, createdBy}
            {
                "Login page not loading on Safari browser",
                "Users on Safari browser (version 16.0+) are unable to load the login page. The page shows a blank screen after clicking the login button. This is affecting approximately 15% of our user base who use Safari.",
                Priority.CRITICAL,
                "john_doe",
                mike != null ? mike.getId() : admin.getId()
            },
            {
                "Database connection timeout in production",
                "Production environment is experiencing intermittent database connection timeouts during peak hours (2-4 PM EST). This is causing transaction failures and user frustration. Error logs show 'Connection timeout after 30 seconds'.",
                Priority.CRITICAL,
                "jane_smith",
                sarah != null ? sarah.getId() : admin.getId()
            },
            {
                "Payment gateway integration returning 500 errors",
                "The Stripe payment gateway integration is returning HTTP 500 errors for approximately 3% of transactions. Users report that payment appears to process but order confirmation is not received. Needs immediate investigation.",
                Priority.HIGH,
                "john_doe",
                admin.getId()
            },
            {
                "Search functionality returns incorrect results",
                "When users search for products using special characters (e.g., '&', '#', '@'), the search returns no results or incorrect items. The search should handle special characters properly and return relevant matches.",
                Priority.HIGH,
                "jane_smith",
                mike != null ? mike.getId() : admin.getId()
            },
            {
                "User profile picture upload fails for large files",
                "Users cannot upload profile pictures larger than 2MB. The upload progress bar reaches 100% but then shows an error: 'Upload failed. Please try again.' This should be increased to at least 5MB with proper validation.",
                Priority.MEDIUM,
                "bob_dev",
                john != null ? john.getId() : admin.getId()
            },
            {
                "Email notifications not sent for password reset",
                "Users requesting password reset are not receiving the reset email. Checked spam folders and email server logs - emails are not being sent from the application. SMTP configuration may need review.",
                Priority.HIGH,
                "jane_smith",
                mike != null ? mike.getId() : admin.getId()
            },
            {
                "Dashboard charts display incorrect data",
                "The analytics dashboard is showing incorrect revenue data for the last 7 days. The total revenue doesn't match the sum of individual transactions. Possible issue with the data aggregation query.",
                Priority.MEDIUM,
                "john_doe",
                sarah != null ? sarah.getId() : admin.getId()
            },
            {
                "Mobile app crashes on Android 12+ devices",
                "Android users on version 12 and above are experiencing app crashes when navigating to the 'My Orders' screen. Crash logs indicate a NullPointerException in OrderAdapter.java line 156.",
                Priority.CRITICAL,
                "bob_dev",
                mike != null ? mike.getId() : admin.getId()
            },
            {
                "Inconsistent button styling across pages",
                "Primary action buttons have inconsistent colors and sizes across different pages. Some pages use blue (#0066CC) while others use green (#00AA00). Need to standardize according to design system.",
                Priority.LOW,
                "jane_smith",
                john != null ? john.getId() : admin.getId()
            },
            {
                "API rate limiting not working correctly",
                "The API rate limiting mechanism is not enforcing the 100 requests/minute limit per API key. Some clients are making 500+ requests/minute without being throttled. This is causing server load issues.",
                Priority.HIGH,
                "john_doe",
                admin.getId()
            },
            {
                "Shopping cart items disappear after session timeout",
                "When a user's session times out (after 30 minutes of inactivity), all items in their shopping cart are lost. Cart contents should be persisted either in database or local storage to prevent this.",
                Priority.MEDIUM,
                "jane_smith",
                jane != null ? jane.getId() : admin.getId()
            },
            {
                "Footer links broken on mobile devices",
                "Footer navigation links are not clickable on mobile devices (iOS and Android). The footer appears to be covered by another element. Tested on iPhone 13 Pro and Samsung Galaxy S22.",
                Priority.LOW,
                "bob_dev",
                mike != null ? mike.getId() : admin.getId()
            },
            {
                "Export to CSV function generates corrupted files",
                "When users export report data to CSV format, the generated file is corrupted and cannot be opened in Excel or Google Sheets. File shows 'File format is not valid' error. Works fine for exports under 1000 rows.",
                Priority.MEDIUM,
                "john_doe",
                sarah != null ? sarah.getId() : admin.getId()
            },
            {
                "Social media login not working with Facebook",
                "Users attempting to login using 'Continue with Facebook' option receive an 'OAuth error' message. The Facebook login was working until yesterday. May be related to Facebook API version update.",
                Priority.HIGH,
                "jane_smith",
                mike != null ? mike.getId() : admin.getId()
            },
            {
                "Pagination breaks on products page for large datasets",
                "When viewing product categories with more than 500 items, the pagination component stops working after page 10. Users cannot navigate to pages 11+. Console shows 'Maximum call stack size exceeded' error.",
                Priority.MEDIUM,
                "bob_dev",
                john != null ? john.getId() : admin.getId()
            },
            {
                "Typo in welcome email template",
                "The welcome email sent to new users contains a typo: 'Wellcome to our platform' instead of 'Welcome to our platform'. Also needs to update the company name in the footer.",
                Priority.LOW,
                "jane_smith",
                admin.getId()
            },
            {
                "Performance degradation on inventory management page",
                "The inventory management page takes 10-15 seconds to load when there are more than 10,000 products. Need to implement pagination or virtual scrolling to improve performance.",
                Priority.HIGH,
                "john_doe",
                sarah != null ? sarah.getId() : admin.getId()
            },
            {
                "Dark mode toggle not saving user preference",
                "When users enable dark mode and then logout, their preference is not saved. After logging back in, the interface reverts to light mode. User preferences should be stored in the database.",
                Priority.LOW,
                "bob_dev",
                jane != null ? jane.getId() : admin.getId()
            },
            {
                "Invoice PDF generation includes wrong tax calculation",
                "Generated invoice PDFs show incorrect sales tax amounts for orders from California. The tax rate used is 7.5% instead of the correct 9.5%. This is affecting financial reporting.",
                Priority.CRITICAL,
                "john_doe",
                admin.getId()
            },
            {
                "Notification badge not updating in real-time",
                "The notification badge in the header doesn't update when new notifications arrive. Users must refresh the page to see the updated count. Should implement WebSocket or polling for real-time updates.",
                Priority.MEDIUM,
                "jane_smith",
                mike != null ? mike.getId() : admin.getId()
            }
        };
        
        int created = 0;
        for (Object[] bugData : bugs) {
            try {
                Bug bug = new Bug(
                    (String) bugData[0],        // title
                    (String) bugData[1],        // description
                    (Priority) bugData[2],      // priority
                    (String) bugData[3]         // assignedTo
                );
                
                Bug createdBug = BugDAO.createBug(bug, (int) bugData[4]);
                
                if (createdBug != null) {
                    String priorityEmoji = switch (bug.getPriority()) {
                        case CRITICAL -> "🔴";
                        case HIGH -> "🟠";
                        case MEDIUM -> "🟡";
                        case LOW -> "🟢";
                    };
                    
                    System.out.println("  ✓ " + priorityEmoji + " [" + bug.getPriority() + "] " + 
                                     bugData[0] + " → Assigned to: " + bugData[3]);
                    created++;
                } else {
                    System.out.println("  ✗ Failed to create bug: " + bugData[0]);
                }
            } catch (Exception e) {
                System.err.println("  ✗ Error creating bug: " + e.getMessage());
            }
        }
        
        System.out.println("\nCreated " + created + " sample bugs.");
        System.out.println("\nBug Priority Distribution:");
        System.out.println("  🔴 CRITICAL: 4 bugs");
        System.out.println("  🟠 HIGH: 5 bugs");
        System.out.println("  🟡 MEDIUM: 7 bugs");
        System.out.println("  🟢 LOW: 4 bugs");
    }
}
