# 🐛 Advanced Bug Tracker - Pure Java Swing

A comprehensive, professional bug tracking system built with **pure Java Swing** - no frameworks, no dependencies, just clean Java code!

## ✨ Latest Updates (v2.0)

- 🎨 **Modern Color Scheme** - Professional UI with carefully selected colors
- 🖱️ **Enhanced Buttons** - Hand cursor on hover, improved visual feedback
- 🎯 **Better UX** - Opaque buttons with solid colors for all platforms
- 📁 **Clean Structure** - Organized project with no unnecessary files
- 🚀 **Optimized Performance** - Faster rendering and better responsiveness

## 📋 Features

### 🔐 User Management
- **User Authentication** - Secure login and registration
- **Multiple User Roles** - Admin, Manager, Developer, Tester, Viewer
- **User Profiles** - Customizable personal information
- **Password Management** - Change password securely

### 🐛 Bug Tracking
- **Create, Edit, Delete Bugs** - Full CRUD operations
- **Bug Status Tracking** - New, Open, In Progress, Resolved, Closed, Reopened
- **Priority Levels** - Critical, High, Medium, Low
- **Severity Levels** - Blocker, Critical, Major, Minor, Trivial
- **Bug Assignment** - Assign bugs to team members
- **Comments System** - Add comments and updates to bugs
- **Complete Bug History** - Track all changes and updates

### 📊 Dashboard & Analytics
- **Real-time Statistics** - Live bug counts and metrics
- **Advanced Filtering** - Filter by status, priority, or assignment
- **Search Functionality** - Quick search by ID, title, or description
- **My Bugs View** - See bugs you reported
- **Assigned to Me** - View bugs assigned to you
- **Clean Table View** - Easy-to-read bug list

### 💾 Data Persistence
- **Automatic Saving** - All data saved automatically
- **File-based Storage** - No database required
- **Java Serialization** - Fast and reliable data storage

---

## 🚀 Getting Started

### Prerequisites
- **Java JDK 8 or higher** installed on your system
- **No additional dependencies required!** Pure Java only

### Quick Start (Windows)

1. **Compile the application:**
   ```bash
   compile.bat
   ```

2. **Run the application:**
   ```bash
   run.bat
   ```

### Quick Start (Linux/Mac)

1. **Make scripts executable:**
   ```bash
   chmod +x compile.sh run.sh
   ```

2. **Compile the application:**
   ```bash
   ./compile.sh
   ```

3. **Run the application:**
   ```bash
   ./run.sh
   ```

### Manual Compilation

```bash
# Navigate to src directory
cd src

# Compile all Java files
javac com/bugtracker/model/*.java
javac com/bugtracker/util/*.java
javac com/bugtracker/service/*.java
javac com/bugtracker/view/*.java
javac com/bugtracker/BugTrackerApp.java

# Run the application
java com.bugtracker.BugTrackerApp
```

---

## 🔑 Default Credentials

The application comes with pre-configured demo accounts:

| Username | Password | Role | Description |
|----------|----------|------|-------------|
| **admin** | admin123 | Administrator | Full system access |
| **manager** | manager123 | Project Manager | Manage projects and assign bugs |
| **developer** | dev123 | Developer | Fix bugs and update status |
| **tester** | test123 | Tester | Report and verify bugs |

---

## 📁 Project Structure

```
Bug_Tracker/
├── src/
│   └── com/
│       └── bugtracker/
│           ├── BugTrackerApp.java          # Main application entry point
│           ├── model/                       # Data models
│           │   ├── Bug.java                 # Bug entity
│           │   ├── BugPriority.java         # Priority enum
│           │   ├── BugSeverity.java         # Severity enum
│           │   ├── BugStatus.java           # Status enum
│           │   ├── Comment.java             # Comment entity
│           │   ├── User.java                # User entity
│           │   └── UserRole.java            # User role enum
│           ├── service/                     # Business logic layer
│           │   ├── AuthService.java         # Authentication service
│           │   ├── BugService.java          # Bug management service
│           │   └── UserService.java         # User management service
│           ├── util/                        # Utility classes
│           │   └── DataStore.java           # Data persistence
│           └── view/                        # GUI components (Swing)
│               ├── CreateBugDialog.java     # Create bug dialog
│               ├── EditBugDialog.java       # Edit bug dialog
│               ├── LoginFrame.java          # Login screen
│               ├── MainFrame.java           # Main application window
│               ├── ProfileDialog.java       # User profile dialog
│               └── RegisterFrame.java       # Registration screen
├── data/                                    # Auto-generated data storage
│   ├── bugs.dat                             # Bug data file
│   └── users.dat                            # User data file
├── compile.bat                              # Windows compilation script
├── run.bat                                  # Windows run script
├── compile.sh                               # Linux/Mac compilation script
├── run.sh                                   # Linux/Mac run script
└── README.md                                # This file
```

---

## 📖 Usage Guide

### 1. Creating a New Bug

1. Login with your credentials
2. Click the **"➕ New Bug"** button
3. Fill in the required information:
   - **Title** (required)
   - **Description** (required)
   - **Priority** (Critical, High, Medium, Low)
   - **Severity** (Blocker, Critical, Major, Minor, Trivial)
   - **Project Name** (optional)
   - **Assign To** (optional - select a team member)
   - **Steps to Reproduce** (optional)
4. Click **"Create Bug"** to save

### 2. Managing Bugs

- **View Details**: Click on any bug in the table to see full details on the right panel
- **Edit Bug**: Select a bug and click **"✏️ Edit Bug"** to modify it
- **Add Comment**: Select a bug and click **"💬 Add Comment"** to add notes
- **Delete Bug**: Select a bug and click **"🗑️ Delete Bug"** to remove it
- **Change Status**: Edit the bug and update its status

### 3. Filtering & Searching

- **Search Bar**: Type in the search box and click **"🔍 Search"** to find bugs by:
  - Bug ID
  - Title
  - Description
- **My Bugs**: Click **"📋 My Bugs"** to see bugs you reported
- **Assigned to Me**: Click **"✓ Assigned to Me"** to see your assigned bugs
- **Refresh**: Click **"🔄 Refresh"** to reload all bugs

### 4. Profile Management

1. Click the **"Profile"** button in the top right corner
2. Update your personal information:
   - Full Name
   - Email
   - Department
3. Change your password (optional):
   - Enter old password
   - Enter new password
   - Confirm new password
4. Click **"Save Profile"** to save changes

---

## 🎯 Features in Detail

### User Roles & Permissions

| Role | Permissions |
|------|------------|
| **Administrator** | Full system access, user management |
| **Manager** | Create/edit/delete bugs, assign bugs, manage projects |
| **Developer** | Update bug status, add comments, view assignments |
| **Tester** | Create bugs, add comments, verify fixes |
| **Viewer** | Read-only access to bugs |

### Bug Workflow

```
📌 NEW → 📂 OPEN → ⏳ IN_PROGRESS → ✅ RESOLVED → 🔒 CLOSED
                        ↓
                    🔄 REOPENED (if issues persist)
```

### Priority Levels

- 🔴 **Critical** - System-breaking issues, immediate attention required
- 🟠 **High** - Important features affected, high priority
- 🟡 **Medium** - Moderate impact on functionality
- 🔵 **Low** - Minor issues, cosmetic problems

### Severity Levels

- **Blocker** - System is unusable, blocks all work
- **Critical** - Major functionality is broken
- **Major** - Significant impact on functionality
- **Minor** - Small impact on functionality
- **Trivial** - Cosmetic issues, very low impact

---

## 💾 Data Storage

All application data is stored locally in the `data/` directory:

- **`users.dat`** - User accounts, profiles, and authentication data
- **`bugs.dat`** - Bug reports, comments, and history

The data files are automatically created on first run and updated whenever changes are made. No database setup required!

---

## 🎨 Screenshots

The application features a modern, clean interface with:
- **Login Screen** - Simple and secure authentication
- **Main Dashboard** - Complete bug overview with statistics
- **Bug Details Panel** - Comprehensive bug information display
- **Create/Edit Dialogs** - Easy-to-use forms
- **Profile Management** - User settings and preferences

---

## 🛠️ Technical Details

### Architecture
- **MVC Pattern** - Clean separation of concerns
- **Service Layer** - Business logic abstraction
- **Data Layer** - File-based persistence

### Technologies
- **Java SE** - Core Java without frameworks
- **Swing** - Native Java GUI toolkit
- **Java Serialization** - Object persistence

### Design Patterns
- **Singleton** - DataStore management
- **Observer** - GUI event handling
- **Factory** - Object creation

---

## 🤝 Contributing

Contributions are welcome! Here's how you can help:

1. **Fork** the repository
2. **Create** a feature branch (`git checkout -b feature/AmazingFeature`)
3. **Commit** your changes (`git commit -m 'Add some AmazingFeature'`)
4. **Push** to the branch (`git push origin feature/AmazingFeature`)
5. **Open** a Pull Request

---

## 📝 Future Enhancements

Potential features for future versions:
- [ ] Email notifications
- [ ] File attachments for bugs
- [ ] Export reports to PDF/Excel
- [ ] Bug analytics and charts
- [ ] Dark mode theme
- [ ] Multi-language support
- [ ] API integration
- [ ] Database support (MySQL, PostgreSQL)

---

## 📄 License

This project is open source and available for educational and commercial purposes.

---

## 💬 Support

For questions, issues, or suggestions:
- Create an issue in the GitHub repository
- Contact the development team
- Check the documentation

---

## 👨‍💻 Developer Notes

### Code Quality
- Clean, readable code with comments
- Object-oriented design principles
- Error handling and validation
- User-friendly error messages

### Testing
- Test with multiple user roles
- Verify data persistence
- Check all CRUD operations
- Test edge cases

---

## 🎉 Acknowledgments

Built with ❤️ using **Pure Java & Swing**

**No frameworks. No dependencies. Just Java!**

---

## 📚 Learning Resources

This project demonstrates:
- Java GUI development with Swing
- Object-oriented programming principles
- File I/O and serialization
- Authentication and authorization
- CRUD operations
- MVC architecture

Perfect for:
- Java learning projects
- Understanding GUI development
- Portfolio demonstrations
- Academic projects
- Small team bug tracking

---

**Happy Bug Tracking! 🐛✨**
