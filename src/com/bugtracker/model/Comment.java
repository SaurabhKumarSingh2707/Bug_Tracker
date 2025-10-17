package com.bugtracker.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a comment on a bug
 */
public class Comment implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String commentId;
    private String bugId;
    private String userId;
    private String username;
    private String content;
    private LocalDateTime createdAt;

    public Comment() {
        this.createdAt = LocalDateTime.now();
    }

    public Comment(String commentId, String bugId, String userId, String username, String content) {
        this();
        this.commentId = commentId;
        this.bugId = bugId;
        this.userId = userId;
        this.username = username;
        this.content = content;
    }

    // Getters and Setters
    public String getCommentId() { return commentId; }
    public void setCommentId(String commentId) { this.commentId = commentId; }

    public String getBugId() { return bugId; }
    public void setBugId(String bugId) { this.bugId = bugId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(commentId, comment.commentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commentId);
    }

    @Override
    public String toString() {
        return "[" + username + "] " + content;
    }
}
