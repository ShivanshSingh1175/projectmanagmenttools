package com.pmt.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Task Model Class - Represents a task within a project
 * Implements Serializable for session management
 */
public class Task implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum TaskStatus {
        TODO("To Do"),
        IN_PROGRESS("In Progress"),
        IN_REVIEW("In Review"),
        COMPLETED("Completed"),
        BLOCKED("Blocked");

        private final String displayName;

        TaskStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum TaskPriority {
        LOW("Low"),
        MEDIUM("Medium"),
        HIGH("High"),
        CRITICAL("Critical");

        private final String displayName;

        TaskPriority(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    private int taskId;
    private int projectId;
    private String taskName;
    private String description;
    private int assignedTo;
    private int assignedBy;
    private TaskPriority priority;
    private TaskStatus status;
    private LocalDate startDate;
    private LocalDate dueDate;
    private int estimatedHours;
    private int actualHours;
    private int completionPercentage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<TaskUpdate> updates;

    // Default Constructor
    public Task() {
        this.updates = new ArrayList<>();
    }

    // Constructor with essential fields
    public Task(String taskName, int projectId, int assignedTo, int assignedBy, LocalDate dueDate) {
        this.taskName = taskName;
        this.projectId = projectId;
        this.assignedTo = assignedTo;
        this.assignedBy = assignedBy;
        this.dueDate = dueDate;
        this.status = TaskStatus.TODO;
        this.priority = TaskPriority.MEDIUM;
        this.updates = new ArrayList<>();
    }

    // Full Constructor
    public Task(int taskId, String taskName, int projectId, int assignedTo, int assignedBy,
                TaskPriority priority, TaskStatus status, LocalDate startDate, LocalDate dueDate) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.projectId = projectId;
        this.assignedTo = assignedTo;
        this.assignedBy = assignedBy;
        this.priority = priority;
        this.status = status;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.updates = new ArrayList<>();
    }

    // Getters and Setters
    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(int assignedTo) {
        this.assignedTo = assignedTo;
    }

    public int getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(int assignedBy) {
        this.assignedBy = assignedBy;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public int getEstimatedHours() {
        return estimatedHours;
    }

    public void setEstimatedHours(int estimatedHours) {
        this.estimatedHours = estimatedHours;
    }

    public int getActualHours() {
        return actualHours;
    }

    public void setActualHours(int actualHours) {
        this.actualHours = actualHours;
    }

    public int getCompletionPercentage() {
        return completionPercentage;
    }

    public void setCompletionPercentage(int completionPercentage) {
        this.completionPercentage = completionPercentage;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<TaskUpdate> getUpdates() {
        return updates;
    }

    public void setUpdates(List<TaskUpdate> updates) {
        this.updates = updates;
    }

    public void addUpdate(TaskUpdate update) {
        this.updates.add(update);
    }

    // Utility methods
    public boolean isOverdue() {
        return LocalDate.now().isAfter(dueDate) && status != TaskStatus.COMPLETED;
    }

    public boolean isTimeEfficient() {
        return actualHours <= estimatedHours;
    }

    public int getRemainingHours() {
        return estimatedHours - actualHours;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskId=" + taskId +
                ", taskName='" + taskName + '\'' +
                ", status=" + status +
                ", priority=" + priority +
                ", dueDate=" + dueDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return taskId == task.taskId;
    }

    @Override
    public int hashCode() {
        return taskId;
    }
}
