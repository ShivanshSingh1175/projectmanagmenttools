package com.pmt.models;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Notification Model Class - Represents system notifications
 */
public class Notification implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public enum NotificationType {
        TASK_ASSIGNED("Task Assigned"),
        TASK_UPDATED("Task Updated"),
        PROJECT_UPDATE("Project Update"),
        DEADLINE_APPROACHING("Deadline Approaching"),
        TASK_COMPLETED("Task Completed");

        private final String displayName;

        NotificationType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    private int notificationId;
    private int userId;
    private String title;
    private String message;
    private NotificationType notificationType;
    private boolean isRead;
    private LocalDateTime createdAt;

    // Default Constructor
    public Notification() {
    }

    // Constructor with essential fields
    public Notification(int userId, String title, String message, NotificationType notificationType) {
        this.userId = userId;
        this.title = title;
        this.message = message;
        this.notificationType = notificationType;
        this.isRead = false;
    }

    // Getters and Setters
    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "notificationId=" + notificationId +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", isRead=" + isRead +
                ", createdAt=" + createdAt +
                '}';
    }
}
