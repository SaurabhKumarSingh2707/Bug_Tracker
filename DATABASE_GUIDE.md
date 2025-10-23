# Database Viewing Guide - Bug Tracker

## 📊 Current Database Summary

Your database contains:
- **9 Users** (including 7 sample users + 2 real users)
- **40 Bugs** (20 original + 20 duplicates from running the populator twice)
- **16 Activity Logs** (including 2 bug fixes you performed!)
- **4 Hours** of time logged across 2 bug fixes
- **2 Bugs Fixed**: Bug #1 and #2 (both marked as CLOSED/RESOLVED)

---

## 🔧 Method 1: Using the Built-in Database Viewer (Easiest)

### **Quick Command**
```powershell
java -cp "bin;lib/*" util.DatabaseViewer
```

### **What It Shows**
✅ **Users Table** - All registered users with roles
✅ **Bugs Table** - All bugs with status, priority, assignments
✅ **Activity Logs** - Recent user activities (latest 20)
✅ **Time Logs** - Hours spent on bug fixes
✅ **Comments** - Bug comments (if any)
✅ **Attachments** - File attachments (if any)
✅ **Tags** - Bug tags (if any)
✅ **Summary Statistics** - Counts by status and priority

### **Creating a Batch Script for Easy Access**

Create a file named `view-database.bat`:
```batch
@echo off
echo ========================================
echo Bug Tracker - Database Viewer
echo ========================================
echo.
java -cp "bin;lib/*" util.DatabaseViewer
echo.
pause
```

Then just double-click `view-database.bat` to view the database anytime!

---

## 🗄️ Method 2: Using DB Browser for SQLite (GUI Tool)

### **Step 1: Download DB Browser**
1. Visit: https://sqlitebrowser.org/dl/
2. Download "DB Browser for SQLite" for Windows
3. Install the software

### **Step 2: Open Your Database**
1. Launch DB Browser for SQLite
2. Click **"Open Database"**
3. Navigate to your project folder
4. Select `bugtracker.db`
5. Click **"Open"**

### **Step 3: Explore Tables**
- Click **"Browse Data"** tab
- Select table from dropdown:
  - `users` - See all user accounts
  - `bugs` - See all bugs
  - `activity_logs` - See user actions
  - `time_logs` - See time tracking
  - `comments` - See bug comments
  - `attachments` - See file attachments
  - `tags` - See bug tags
  - `bug_tags` - See bug-tag relationships

### **Step 4: Run Custom Queries**
- Click **"Execute SQL"** tab
- Type your SQL query, for example:
  ```sql
  -- Get all CRITICAL bugs
  SELECT * FROM bugs WHERE priority = 'CRITICAL';
  
  -- Get bugs assigned to john_doe
  SELECT * FROM bugs WHERE assigned_to = 'john_doe';
  
  -- Get user login history
  SELECT * FROM activity_logs WHERE action = 'USER_LOGIN';
  
  -- Get total hours per user
  SELECT username, SUM(hours_spent) as total_hours 
  FROM time_logs 
  GROUP BY username;
  ```

---

## 💻 Method 3: Using SQLite Command Line

### **Step 1: Download SQLite Tools**
1. Visit: https://www.sqlite.org/download.html
2. Download "sqlite-tools-win32-x86-*.zip"
3. Extract to a folder (e.g., `C:\sqlite`)
4. Add to PATH or use full path

### **Step 2: Open Database**
```powershell
# Navigate to your project folder
cd "C:\Users\saura\Documents\GitHub\Bug_Tracker"

# Open the database
sqlite3 bugtracker.db
```

### **Step 3: Run Commands**
```sql
-- List all tables
.tables

-- Show table structure
.schema bugs

-- View data
SELECT * FROM users;
SELECT * FROM bugs WHERE status = 'NEW';
SELECT * FROM activity_logs ORDER BY timestamp DESC LIMIT 10;

-- Pretty output
.mode column
.headers on
SELECT id, title, priority, status FROM bugs;

-- Export to CSV
.mode csv
.output bugs_export.csv
SELECT * FROM bugs;
.output stdout

-- Exit
.exit
```

---

## 📝 Method 4: Using PowerShell Script

Create a file named `query-database.ps1`:
```powershell
param(
    [string]$Query = "SELECT * FROM bugs"
)

Add-Type -Path "lib\sqlite-jdbc-3.44.1.0.jar"

$conn = [System.Data.SQLite.SQLiteConnection]::new("Data Source=bugtracker.db")
$conn.Open()

$cmd = $conn.CreateCommand()
$cmd.CommandText = $Query

$reader = $cmd.ExecuteReader()

# Display results
while ($reader.Read()) {
    $row = @{}
    for ($i = 0; $i -lt $reader.FieldCount; $i++) {
        $row[$reader.GetName($i)] = $reader.GetValue($i)
    }
    [PSCustomObject]$row
}

$reader.Close()
$conn.Close()
```

Usage:
```powershell
# View all bugs
.\query-database.ps1 -Query "SELECT * FROM bugs"

# View users
.\query-database.ps1 -Query "SELECT id, username, email, user_type FROM users"

# View activity logs
.\query-database.ps1 -Query "SELECT * FROM activity_logs ORDER BY timestamp DESC LIMIT 20"
```

---

## 🔍 Useful SQL Queries

### **User Queries**
```sql
-- List all users
SELECT id, username, full_name, user_type FROM users;

-- Count users by type
SELECT user_type, COUNT(*) as count 
FROM users 
GROUP BY user_type;

-- Find specific user
SELECT * FROM users WHERE username = 'john_doe';
```

### **Bug Queries**
```sql
-- All bugs with details
SELECT id, title, priority, status, assigned_to 
FROM bugs 
ORDER BY priority DESC, id ASC;

-- Bugs by status
SELECT status, COUNT(*) as count 
FROM bugs 
GROUP BY status;

-- Critical bugs assigned to john_doe
SELECT id, title, description 
FROM bugs 
WHERE priority = 'CRITICAL' AND assigned_to = 'john_doe';

-- Recently updated bugs
SELECT id, title, status, updated_date 
FROM bugs 
ORDER BY updated_date DESC 
LIMIT 10;

-- Bugs created today
SELECT * FROM bugs 
WHERE DATE(created_date) = DATE('now');
```

### **Activity Log Queries**
```sql
-- Recent activities
SELECT username, action, details, timestamp 
FROM activity_logs 
ORDER BY timestamp DESC 
LIMIT 20;

-- User login history
SELECT username, timestamp 
FROM activity_logs 
WHERE action = 'USER_LOGIN' 
ORDER BY timestamp DESC;

-- Bug fix history
SELECT al.*, b.title 
FROM activity_logs al
JOIN bugs b ON al.bug_id = b.id
WHERE al.action = 'BUG_FIXED'
ORDER BY al.timestamp DESC;
```

### **Time Tracking Queries**
```sql
-- Total hours by user
SELECT username, 
       COUNT(*) as entries, 
       SUM(hours_spent) as total_hours,
       AVG(hours_spent) as avg_hours
FROM time_logs 
GROUP BY username;

-- Time spent per bug
SELECT bug_id, 
       SUM(hours_spent) as total_hours 
FROM time_logs 
GROUP BY bug_id 
ORDER BY total_hours DESC;

-- Daily time log
SELECT DATE(log_date) as date, 
       SUM(hours_spent) as hours 
FROM time_logs 
GROUP BY DATE(log_date);
```

### **Complex Queries**
```sql
-- Bugs with user details
SELECT b.id, b.title, b.priority, b.status,
       u.full_name as assigned_to_name,
       creator.full_name as created_by_name
FROM bugs b
LEFT JOIN users u ON b.assigned_to = u.username
LEFT JOIN users creator ON b.created_by = creator.id;

-- User productivity
SELECT u.username, u.full_name,
       COUNT(DISTINCT b.id) as bugs_assigned,
       SUM(tl.hours_spent) as total_hours_spent
FROM users u
LEFT JOIN bugs b ON u.username = b.assigned_to
LEFT JOIN time_logs tl ON u.username = tl.username
GROUP BY u.id, u.username, u.full_name;

-- Bug resolution rate
SELECT 
    COUNT(*) as total_bugs,
    SUM(CASE WHEN status = 'CLOSED' THEN 1 ELSE 0 END) as closed_bugs,
    SUM(CASE WHEN status = 'RESOLVED' THEN 1 ELSE 0 END) as resolved_bugs,
    ROUND(100.0 * SUM(CASE WHEN status IN ('CLOSED', 'RESOLVED') THEN 1 ELSE 0 END) / COUNT(*), 2) as resolution_rate_percent
FROM bugs;
```

---

## 📁 Database File Location

**File**: `bugtracker.db`
**Location**: `C:\Users\saura\Documents\GitHub\Bug_Tracker\bugtracker.db`
**Size**: ~60-100 KB (varies with data)
**Type**: SQLite 3 Database

---

## 🛠️ Database Maintenance

### **Backup Database**
```powershell
# Create backup
Copy-Item bugtracker.db bugtracker_backup_$(Get-Date -Format 'yyyyMMdd_HHmmss').db

# Or using sqlite3
sqlite3 bugtracker.db ".backup bugtracker_backup.db"
```

### **Restore Database**
```powershell
# Restore from backup
Copy-Item bugtracker_backup.db bugtracker.db -Force
```

### **Reset Database**
```powershell
# Delete database (will be recreated on next app launch)
Remove-Item bugtracker.db

# Run app to create fresh database
java -cp "bin;lib/*" main.Main
```

### **Optimize Database**
```sql
-- In sqlite3 command line
VACUUM;
ANALYZE;
```

### **Check Database Integrity**
```sql
-- In sqlite3 command line
PRAGMA integrity_check;
```

---

## 📊 Database Schema Overview

### **Tables**
1. **users** - User accounts
2. **bugs** - Bug reports
3. **activity_logs** - User actions audit trail
4. **time_logs** - Time tracking entries
5. **comments** - Bug comments
6. **attachments** - File attachments
7. **tags** - Bug tags
8. **bug_tags** - Many-to-many relationship

### **Relationships**
```
users (1) ←→ (N) bugs (created_by)
users (1) ←→ (N) bugs (assigned via username)
users (1) ←→ (N) activity_logs
users (1) ←→ (N) time_logs
bugs (1) ←→ (N) activity_logs
bugs (1) ←→ (N) time_logs
bugs (1) ←→ (N) comments
bugs (1) ←→ (N) attachments
bugs (N) ←→ (N) tags (via bug_tags)
```

---

## 🎯 Quick Reference

| Task | Command |
|------|---------|
| **View all data** | `java -cp "bin;lib/*" util.DatabaseViewer` |
| **Open in GUI** | Open `bugtracker.db` in DB Browser for SQLite |
| **Query CLI** | `sqlite3 bugtracker.db` then run SQL |
| **Backup** | `Copy-Item bugtracker.db bugtracker_backup.db` |
| **Export CSV** | Use DB Browser → File → Export → CSV |
| **Check size** | `(Get-Item bugtracker.db).Length / 1MB` MB |

---

## 🐛 Troubleshooting

### **Problem**: Can't find bugtracker.db
**Solution**: Make sure you're in the project directory:
```powershell
cd "C:\Users\saura\Documents\GitHub\Bug_Tracker"
dir bugtracker.db
```

### **Problem**: Database locked error
**Solution**: Close the application first, then try again

### **Problem**: Empty tables
**Solution**: Run the sample data populator:
```powershell
java -cp "bin;lib/*" util.SampleDataPopulator
```

### **Problem**: Duplicate data
**Solution**: The populator was run twice. You can either:
- Keep it (doesn't hurt)
- Delete database and recreate
- Manually delete duplicates in DB Browser

---

## 📚 Additional Resources

- **SQLite Official Docs**: https://www.sqlite.org/docs.html
- **DB Browser Guide**: https://sqlitebrowser.org/
- **SQL Tutorial**: https://www.w3schools.com/sql/
- **Java JDBC Tutorial**: https://docs.oracle.com/javase/tutorial/jdbc/

---

## 🎓 Learning Tips

1. **Start Simple**: Use the built-in DatabaseViewer first
2. **Use GUI Tools**: DB Browser is great for beginners
3. **Practice SQL**: Try different queries in the Execute SQL tab
4. **Backup First**: Always backup before making changes
5. **Learn by Doing**: Experiment with different queries

---

**Last Updated**: October 21, 2025
**Database Version**: SQLite 3
**Total Records**: ~70+ (9 users + 40 bugs + 16 activity logs + more)
