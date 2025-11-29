package com.pmt.models;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * TaskUpdate Model Class - Represents updates/comments on a task
 */
public class TaskUpdate implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public enum UpdateType {
        COMMENT("Comment"),
        STATUS_CHANGE("Status Change"),
        HOURS_UPDATE("Hours Updated");

        private final String displayName;

        UpdateType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    private int updateId;
    private int taskId;
    private int userId;
    private String comment;
    private int hoursWorked;
    private UpdateType updateType;
    private LocalDateTime createdAt;

    // Default Constructor
    public TaskUpdate() {
    }

    // Constructor with essential fields
    public TaskUpdate(int taskId, int userId, String comment, UpdateType updateType) {
        this.taskId = taskId;
        this.userId = userId;
        this.comment = comment;
        this.updateType = updateType;
    }

    // Getters and Setters
    public int getUpdateId() {
        return updateId;
    }

    public void setUpdateId(int updateId) {
        this.updateId = updateId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getHoursWorked() {
        return hoursWorked;
    }

    public void setHoursWorked(int hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

    public UpdateType getUpdateType() {
        return updateType;
    }

    public void setUpdateType(UpdateType updateType) {
        this.updateType = updateType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "TaskUpdate{" +
                "updateId=" + updateId +
                ", taskId=" + taskId +
                ", userId=" + userId +
                ", updateType=" + updateType +
                ", createdAt=" + createdAt +
                '}';
    }
}
