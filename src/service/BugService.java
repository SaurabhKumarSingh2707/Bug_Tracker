package service;

import model.Bug;
import model.Bug.Priority;
import model.Bug.Status;
import model.ActivityLog;
import model.User;
import database.BugDAO;
import java.util.List;

/**
 * Service class for managing Bug operations (business logic)
 */
public class BugService {
    private ActivityLogService activityLogService;
    
    public BugService() {
        this.activityLogService = new ActivityLogService();
    }
    
    // Create a new bug
    public Bug createBug(String title, String description, Priority priority, String assignedTo) {
        Bug bug = new Bug(title, description, priority, assignedTo);
        Bug createdBug = BugDAO.createBug(bug, AuthService.getCurrentUser().getId());
        
        if (createdBug != null) {
            // Log activity
            User currentUser = AuthService.getCurrentUser();
            ActivityLog log = new ActivityLog(
                currentUser.getId(),
                currentUser.getUsername(),
                "BUG_CREATED",
                String.format("Created bug: '%s' [Priority: %s, Assigned to: %s]", 
                    title, priority, assignedTo),
                createdBug.getId()
            );
            activityLogService.logActivity(log);
        }
        
        return createdBug;
    }
    
    // Get all bugs
    public List<Bug> getAllBugs() {
        return BugDAO.getAllBugs();
    }
    
    // Get bug by ID
    public Bug getBugById(int id) {
        return BugDAO.getBugById(id);
    }
    
    // Update bug
    public boolean updateBug(int id, String title, String description, 
                            Priority priority, Status status, String assignedTo) {
        Bug bug = getBugById(id);
        if (bug != null) {
            // Track what changed
            StringBuilder changes = new StringBuilder();
            if (!bug.getTitle().equals(title)) {
                changes.append(String.format("Title: '%s' → '%s'; ", bug.getTitle(), title));
            }
            if (bug.getPriority() != priority) {
                changes.append(String.format("Priority: %s → %s; ", bug.getPriority(), priority));
            }
            if (bug.getStatus() != status) {
                changes.append(String.format("Status: %s → %s; ", bug.getStatus(), status));
            }
            if (!bug.getAssignedTo().equals(assignedTo)) {
                changes.append(String.format("Assigned: %s → %s; ", bug.getAssignedTo(), assignedTo));
            }
            
            bug.setTitle(title);
            bug.setDescription(description);
            bug.setPriority(priority);
            bug.setStatus(status);
            bug.setAssignedTo(assignedTo);
            
            boolean updated = BugDAO.updateBug(bug);
            
            if (updated && changes.length() > 0) {
                // Log activity
                User currentUser = AuthService.getCurrentUser();
                ActivityLog log = new ActivityLog(
                    currentUser.getId(),
                    currentUser.getUsername(),
                    "BUG_UPDATED",
                    String.format("Updated bug #%d: %s", id, changes.toString()),
                    id
                );
                activityLogService.logActivity(log);
            }
            
            return updated;
        }
        return false;
    }
    
    // Delete bug
    public boolean deleteBug(int id) {
        Bug bug = getBugById(id);
        boolean deleted = BugDAO.deleteBug(id);
        
        if (deleted && bug != null) {
            // Log activity
            User currentUser = AuthService.getCurrentUser();
            ActivityLog log = new ActivityLog(
                currentUser.getId(),
                currentUser.getUsername(),
                "BUG_DELETED",
                String.format("Deleted bug: '%s'", bug.getTitle()),
                id
            );
            activityLogService.logActivity(log);
        }
        
        return deleted;
    }
    
    // Filter bugs by status
    public List<Bug> filterByStatus(Status status) {
        return BugDAO.filterByStatus(status);
    }
    
    // Filter bugs by priority
    public List<Bug> filterByPriority(Priority priority) {
        return BugDAO.filterByPriority(priority);
    }
    
    // Search bugs by keyword in title or description
    public List<Bug> searchBugs(String keyword) {
        return BugDAO.searchBugs(keyword);
    }
    
    // Get bug statistics
    public BugStats getStatistics() {
        List<Bug> bugs = getAllBugs();
        BugStats stats = new BugStats();
        stats.totalBugs = bugs.size();
        stats.newBugs = (int) bugs.stream().filter(b -> b.getStatus() == Status.NEW).count();
        stats.inProgressBugs = (int) bugs.stream().filter(b -> b.getStatus() == Status.IN_PROGRESS).count();
        stats.resolvedBugs = (int) bugs.stream().filter(b -> b.getStatus() == Status.RESOLVED).count();
        stats.closedBugs = (int) bugs.stream().filter(b -> b.getStatus() == Status.CLOSED).count();
        stats.criticalBugs = (int) bugs.stream().filter(b -> b.getPriority() == Priority.CRITICAL).count();
        return stats;
    }
    
    // Inner class for statistics
    public static class BugStats {
        public int totalBugs;
        public int newBugs;
        public int inProgressBugs;
        public int resolvedBugs;
        public int closedBugs;
        public int criticalBugs;
    }
}
