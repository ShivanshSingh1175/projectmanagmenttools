package com.pmt.models;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * ActivityLog Model Class - Tracks all user activities in the system
 */
public class ActivityLog implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public enum ActivityType {
        CREATE("Create"),
        UPDATE("Update"),
        DELETE("Delete"),
        LOGIN("Login"),
        LOGOUT("Logout"),
        ASSIGN("Assign"),
        COMPLETE("Complete");

        private final String displayName;

        ActivityType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    private int logId;
    private int userId;
    private ActivityType activityType;
    private String entityType; // USER, PROJECT, TASK
    private int entityId;
    private String description;
    private String oldValue;
    private String newValue;
    private LocalDateTime createdAt;

    // Default Constructor
    public ActivityLog() {
    }

    // Constructor with essential fields
    public ActivityLog(int userId, ActivityType activityType, String entityType, 
                       int entityId, String description) {
        this.userId = userId;
        this.activityType = activityType;
        this.entityType = entityType;
        this.entityId = entityId;
        this.description = description;
    }

    // Getters and Setters
    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "ActivityLog{" +
                "logId=" + logId +
                ", userId=" + userId +
                ", activityType=" + activityType +
                ", entityType='" + entityType + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
