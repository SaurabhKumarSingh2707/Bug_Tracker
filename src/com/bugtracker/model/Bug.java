package com.bugtracker.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a bug/issue in the system
 */
public class Bug implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String bugId;
    private String title;
    private String description;
    private BugStatus status;
    private BugPriority priority;
    private BugSeverity severity;
    private String projectName;
    private String reportedBy;
    private String assignedTo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime resolvedAt;
    private String stepsToReproduce;
    private List<Comment> comments;
    private List<String> tags;

    public Bug() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.status = BugStatus.NEW;
        this.comments = new ArrayList<>();
        this.tags = new ArrayList<>();
    }

    public Bug(String bugId, String title, String description, BugPriority priority, 
               BugSeverity severity, String reportedBy) {
        this();
        this.bugId = bugId;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.severity = severity;
        this.reportedBy = reportedBy;
    }

    // Getters and Setters
    public String getBugId() { return bugId; }
    public void setBugId(String bugId) { this.bugId = bugId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BugStatus getStatus() { return status; }
    public void setStatus(BugStatus status) { 
        this.status = status;
        this.updatedAt = LocalDateTime.now();
        if (status == BugStatus.RESOLVED || status == BugStatus.CLOSED) {
            this.resolvedAt = LocalDateTime.now();
        }
    }

    public BugPriority getPriority() { return priority; }
    public void setPriority(BugPriority priority) { 
        this.priority = priority;
        this.updatedAt = LocalDateTime.now();
    }

    public BugSeverity getSeverity() { return severity; }
    public void setSeverity(BugSeverity severity) { this.severity = severity; }

    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }

    public String getReportedBy() { return reportedBy; }
    public void setReportedBy(String reportedBy) { this.reportedBy = reportedBy; }

    public String getAssignedTo() { return assignedTo; }
    public void setAssignedTo(String assignedTo) { 
        this.assignedTo = assignedTo;
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public LocalDateTime getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(LocalDateTime resolvedAt) { this.resolvedAt = resolvedAt; }

    public String getStepsToReproduce() { return stepsToReproduce; }
    public void setStepsToReproduce(String stepsToReproduce) { this.stepsToReproduce = stepsToReproduce; }

    public List<Comment> getComments() { return comments; }
    public void setComments(List<Comment> comments) { this.comments = comments; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    public void addComment(Comment comment) {
        this.comments.add(comment);
        this.updatedAt = LocalDateTime.now();
    }

    public void addTag(String tag) {
        if (!this.tags.contains(tag)) {
            this.tags.add(tag);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bug bug = (Bug) o;
        return Objects.equals(bugId, bug.bugId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bugId);
    }

    @Override
    public String toString() {
        return bugId + " - " + title + " [" + status + "]";
    }
}
