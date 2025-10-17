package com.bugtracker.service;

import com.bugtracker.model.*;
import com.bugtracker.util.DataStore;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for managing bugs
 */
public class BugService {
    private final DataStore dataStore;

    public BugService(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public Bug createBug(Bug bug) {
        if (bug.getBugId() == null || bug.getBugId().isEmpty()) {
            bug.setBugId(generateBugId());
        }
        dataStore.addBug(bug);
        return bug;
    }

    public Bug updateBug(Bug bug) {
        dataStore.updateBug(bug);
        return bug;
    }

    public boolean deleteBug(String bugId) {
        return dataStore.deleteBug(bugId);
    }

    public Optional<Bug> getBugById(String bugId) {
        return dataStore.getBugs().stream()
                .filter(b -> b.getBugId().equals(bugId))
                .findFirst();
    }

    public List<Bug> getAllBugs() {
        return dataStore.getBugs();
    }

    public List<Bug> getBugsByStatus(BugStatus status) {
        return dataStore.getBugs().stream()
                .filter(b -> b.getStatus() == status)
                .collect(Collectors.toList());
    }

    public List<Bug> getBugsAssignedTo(String userId) {
        return dataStore.getBugs().stream()
                .filter(b -> userId.equals(b.getAssignedTo()))
                .collect(Collectors.toList());
    }

    public List<Bug> getBugsReportedBy(String userId) {
        return dataStore.getBugs().stream()
                .filter(b -> userId.equals(b.getReportedBy()))
                .collect(Collectors.toList());
    }

    public List<Bug> getBugsByPriority(BugPriority priority) {
        return dataStore.getBugs().stream()
                .filter(b -> b.getPriority() == priority)
                .collect(Collectors.toList());
    }

    public List<Bug> searchBugs(String query) {
        String lowerQuery = query.toLowerCase();
        return dataStore.getBugs().stream()
                .filter(b -> b.getTitle().toLowerCase().contains(lowerQuery) ||
                        b.getDescription().toLowerCase().contains(lowerQuery) ||
                        b.getBugId().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
    }

    public void addComment(String bugId, Comment comment) {
        getBugById(bugId).ifPresent(bug -> {
            comment.setCommentId(UUID.randomUUID().toString());
            comment.setBugId(bugId);
            bug.addComment(comment);
            updateBug(bug);
        });
    }

    public void assignBug(String bugId, String userId) {
        getBugById(bugId).ifPresent(bug -> {
            bug.setAssignedTo(userId);
            if (bug.getStatus() == BugStatus.NEW) {
                bug.setStatus(BugStatus.OPEN);
            }
            updateBug(bug);
        });
    }

    public void changeBugStatus(String bugId, BugStatus newStatus) {
        getBugById(bugId).ifPresent(bug -> {
            bug.setStatus(newStatus);
            updateBug(bug);
        });
    }

    public void changeBugPriority(String bugId, BugPriority newPriority) {
        getBugById(bugId).ifPresent(bug -> {
            bug.setPriority(newPriority);
            updateBug(bug);
        });
    }

    private String generateBugId() {
        int count = dataStore.getBugs().size() + 1;
        return String.format("BUG-%05d", count);
    }

    public BugStatistics getStatistics() {
        List<Bug> allBugs = getAllBugs();
        return new BugStatistics(
                allBugs.size(),
                getBugsByStatus(BugStatus.NEW).size(),
                getBugsByStatus(BugStatus.OPEN).size(),
                getBugsByStatus(BugStatus.IN_PROGRESS).size(),
                getBugsByStatus(BugStatus.RESOLVED).size(),
                getBugsByStatus(BugStatus.CLOSED).size(),
                getBugsByPriority(BugPriority.CRITICAL).size(),
                getBugsByPriority(BugPriority.HIGH).size()
        );
    }

    public static class BugStatistics {
        public final int total;
        public final int newBugs;
        public final int open;
        public final int inProgress;
        public final int resolved;
        public final int closed;
        public final int critical;
        public final int high;

        public BugStatistics(int total, int newBugs, int open, int inProgress,
                           int resolved, int closed, int critical, int high) {
            this.total = total;
            this.newBugs = newBugs;
            this.open = open;
            this.inProgress = inProgress;
            this.resolved = resolved;
            this.closed = closed;
            this.critical = critical;
            this.high = high;
        }
    }
}
