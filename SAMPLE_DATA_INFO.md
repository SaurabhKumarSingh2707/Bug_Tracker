# Sample Data Added to Bug Tracker

## Summary

Successfully populated the Bug Tracker database with **20 diverse sample bugs** and **7 sample users**.

## Sample Users Created

All users have been created with the following credentials:

| Username | Password | Full Name | Role | Email |
|----------|----------|-----------|------|-------|
| admin | Admin123 | System Administrator | ADMIN | admin@bugtracker.com |
| john_doe | John123 | John Doe | DEVELOPER | john.doe@company.com |
| jane_smith | Jane123 | Jane Smith | DEVELOPER | jane.smith@company.com |
| mike_tester | Mike123 | Mike Wilson | TESTER | mike.test@company.com |
| sarah_manager | Sarah123 | Sarah Johnson | MANAGER | sarah.mgr@company.com |
| bob_dev | Bob123 | Bob Anderson | DEVELOPER | bob.dev@company.com |
| alice_qa | Alice123 | Alice Brown | TESTER | alice.qa@company.com |

## Sample Bugs Created

### 🔴 CRITICAL Priority (4 bugs)

1. **Login page not loading on Safari browser**
   - Assigned to: john_doe
   - Affects 15% of users on Safari 16.0+

2. **Database connection timeout in production**
   - Assigned to: jane_smith
   - Intermittent timeouts during peak hours

3. **Mobile app crashes on Android 12+ devices**
   - Assigned to: bob_dev
   - NullPointerException in OrderAdapter

4. **Invoice PDF generation includes wrong tax calculation**
   - Assigned to: john_doe
   - Incorrect California sales tax (7.5% vs 9.5%)

### 🟠 HIGH Priority (6 bugs)

5. **Payment gateway integration returning 500 errors**
   - Assigned to: john_doe
   - 3% of Stripe transactions failing

6. **Search functionality returns incorrect results**
   - Assigned to: jane_smith
   - Special characters not handled properly

7. **Email notifications not sent for password reset**
   - Assigned to: jane_smith
   - SMTP configuration issue

8. **API rate limiting not working correctly**
   - Assigned to: john_doe
   - Clients exceeding 100 req/min limit

9. **Social media login not working with Facebook**
   - Assigned to: jane_smith
   - OAuth error with Facebook API

10. **Performance degradation on inventory management page**
    - Assigned to: john_doe
    - 10-15 second load time for 10,000+ products

### 🟡 MEDIUM Priority (7 bugs)

11. **User profile picture upload fails for large files**
    - Assigned to: bob_dev
    - 2MB file size limit issue

12. **Dashboard charts display incorrect data**
    - Assigned to: john_doe
    - Revenue aggregation query problem

13. **Shopping cart items disappear after session timeout**
    - Assigned to: jane_smith
    - Cart persistence issue

14. **Export to CSV function generates corrupted files**
    - Assigned to: john_doe
    - Files >1000 rows cannot be opened

15. **Pagination breaks on products page for large datasets**
    - Assigned to: bob_dev
    - Cannot navigate beyond page 10

16. **Notification badge not updating in real-time**
    - Assigned to: jane_smith
    - Requires page refresh to see updates

### 🟢 LOW Priority (4 bugs)

17. **Inconsistent button styling across pages**
    - Assigned to: jane_smith
    - Color inconsistency (#0066CC vs #00AA00)

18. **Footer links broken on mobile devices**
    - Assigned to: bob_dev
    - Links not clickable on iOS/Android

19. **Typo in welcome email template**
    - Assigned to: jane_smith
    - "Wellcome" instead of "Welcome"

20. **Dark mode toggle not saving user preference**
    - Assigned to: bob_dev
    - Preference not persisted after logout

## Bug Assignment Distribution

- **john_doe**: 7 bugs (1 Critical, 3 High, 3 Medium)
- **jane_smith**: 8 bugs (1 Critical, 3 High, 3 Medium, 1 Low)
- **bob_dev**: 5 bugs (1 Critical, 4 Medium/Low)

## How to Use

1. **Login** to the Bug Tracker application using any of the sample user credentials above
2. **View bugs** assigned to you in the dashboard
3. **Test features** like:
   - Searching and filtering bugs
   - Updating bug status
   - Changing priority levels
   - Adding comments
   - Viewing statistics
   - Generating reports

## Re-running the Populator

To add more sample data or reset:

```bash
# Compile
javac -cp "lib/*" -d bin src/util/SampleDataPopulator.java

# Run
java -cp "bin;lib/*" util.SampleDataPopulator
```

**Note**: The script is idempotent for users (won't create duplicates) but will add new bugs each time it's run.

## Deleting Sample Data

If you want to start fresh, delete the `bugtracker.db` file and restart the application. The database will be recreated automatically.

---

**Last Updated**: October 21, 2025
**Total Sample Bugs**: 20
**Total Sample Users**: 7
