# Bug Tracker Application

A professional Bug Tracking System built with Java Swing GUI framework and SQLite database.

## Features

- **User Authentication**: Login and registration system with password hashing
- **User-Based Bug Management**: Each user can create and manage bugs
- **Bug Management**: Create, view, edit, and delete bugs
- **Priority Levels**: Critical, High, Medium, Low
- **Status Tracking**: New, In Progress, Resolved, Closed, Reopened
- **Search & Filter**: Search bugs by keywords and filter by status/priority
- **Statistics Dashboard**: Real-time statistics of bugs by status and priority
- **SQLite Database**: Persistent storage for users and bugs
- **User-Friendly GUI**: Clean and intuitive interface

## Project Structure

```
Bug_Tracker/
├── lib/
│   └── sqlite-jdbc-3.44.1.0.jar  # SQLite JDBC driver
├── src/
│   ├── main/
│   │   └── Main.java              # Application entry point
│   ├── model/
│   │   ├── Bug.java               # Bug data model
│   │   └── User.java              # User data model
│   ├── database/
│   │   ├── DatabaseManager.java   # Database connection and initialization
│   │   ├── UserDAO.java           # User data access operations
│   │   └── BugDAO.java            # Bug data access operations
│   ├── service/
│   │   ├── AuthService.java       # Authentication service
│   │   └── BugService.java        # Bug business logic
│   └── ui/
│       ├── LoginFrame.java        # Login window
│       ├── RegisterDialog.java    # Registration dialog
│       ├── BugTrackerFrame.java   # Main application window
│       ├── BugListPanel.java      # Bug list with search/filter
│       ├── AddBugPanel.java       # Create new bugs
│       ├── StatisticsPanel.java   # Statistics dashboard
│       ├── BugDetailsDialog.java  # View bug details
│       └── EditBugDialog.java     # Edit existing bugs
├── bugtracker.db                   # SQLite database (created on first run)
└── README.md
```

## How to Run

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- SQLite JDBC driver (already included in `lib/` folder)

### Compilation
```bash
# Navigate to the project directory
cd Bug_Tracker

# Compile all Java files with SQLite library
javac -cp "lib/*" -d bin src/main/*.java src/model/*.java src/database/*.java src/service/*.java src/ui/*.java
```

### Execution
```bash
# Run the application
java -cp "bin;lib/*" main.Main
```

**Note for Windows PowerShell**: Use semicolon (`;`) as path separator
**Note for Linux/Mac**: Use colon (`:`) as path separator: `java -cp "bin:lib/*" main.Main`

## First Time Setup

1. Run the application
2. Click "Register" button on the login screen
3. Fill in the registration form:
   - Full Name
   - Username (unique)
   - Email (unique)
   - Password (minimum 6 characters)
4. Click "Register"
5. Login with your new credentials

## Usage Guide

### Login Screen
- **Login**: Enter username and password to access the application
- **Register**: Create a new user account

### Main Application

#### 1. Bug List Tab
- View all bugs in a table format
- **Search**: Enter keywords to search in bug titles and descriptions
- **Filter**: Filter bugs by status or priority
- **View Details**: Select a bug and click "View Details" to see full information
- **Edit**: Select a bug and click "Edit Bug" to modify it
- **Delete**: Select a bug and click "Delete Bug" to remove it
- **Refresh**: Update the bug list

#### 2. Add New Bug Tab
- **Title**: Enter a descriptive title for the bug
- **Description**: Provide detailed information about the bug
- **Priority**: Select the priority level (Low, Medium, High, Critical)
- **Assigned To**: Enter the name of the person responsible for fixing the bug
- Click "Submit Bug" to create the bug or "Clear Form" to reset

#### 3. Statistics Tab
- View real-time statistics:
  - Total number of bugs
  - Bugs by status (New, In Progress, Resolved, Closed)
  - Critical priority bugs count
- Click "Refresh Statistics" to update the numbers

### Logout
- Click the "Logout" button in the top-right corner
- Confirms before logging out
- Returns to the login screen

## Database Schema

### Users Table
- `id`: Primary key (auto-increment)
- `username`: Unique username
- `email`: Unique email address
- `password_hash`: SHA-256 hashed password
- `full_name`: User's full name
- `created_date`: Account creation timestamp
- `last_login_date`: Last login timestamp

### Bugs Table
- `id`: Primary key (auto-increment)
- `title`: Bug title
- `description`: Detailed bug description
- `priority`: LOW, MEDIUM, HIGH, CRITICAL
- `status`: NEW, IN_PROGRESS, RESOLVED, CLOSED, REOPENED
- `assigned_to`: Person responsible for fixing
- `created_by`: User ID who created the bug (foreign key)
- `created_date`: Bug creation timestamp
- `updated_date`: Last update timestamp

## Security Features

- **Password Hashing**: Passwords are hashed using SHA-256 before storage
- **Unique Constraints**: Usernames and emails must be unique
- **Session Management**: Only authenticated users can access the bug tracker
- **Input Validation**: All user inputs are validated before processing

## Technical Details

### Architecture
- **MVC Pattern**: Separation of Model, View, and Controller logic
- **DAO Pattern**: Data Access Objects for database operations
- **Service Layer**: Business logic separated from UI and data access
- **SQLite Database**: Lightweight, file-based database for persistence

### Technologies Used
- **Java Swing**: GUI framework
- **SQLite**: Embedded database
- **JDBC**: Database connectivity
- **SHA-256**: Password hashing algorithm

## Database File

The SQLite database file (`bugtracker.db`) is automatically created in the project root directory on first run. This file contains all users and bugs data.

## Troubleshooting

### Cannot find SQLite driver
If you get an error about missing SQLite driver, ensure:
1. The `lib/sqlite-jdbc-3.44.1.0.jar` file exists
2. You're including it in the classpath with `-cp "lib/*"`

### Database locked error
If the database is locked:
1. Close all instances of the application
2. Delete `bugtracker.db` file (WARNING: This will delete all data)
3. Restart the application

## Future Enhancements

Possible improvements for future versions:
- Email notifications for bug assignments
- Attachment support for screenshots
- Comments/discussion on bugs
- Bug history/audit trail
- Export reports to PDF/Excel
- User roles and permissions (admin, developer, tester)
- Dashboard with charts and graphs
- Multi-language support
- Dark mode theme
- Email verification for registration
- Password reset functionality

## Author

Created as a professional Java Swing demonstration project with SQLite integration.

## License

This project is open-source and available for educational purposes.
