package com.pmt.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Project Model Class - Represents a project in the system
 * Uses Collections to manage project members and tasks
 */
public class Project implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum ProjectStatus {
        ACTIVE("Active"),
        COMPLETED("Completed"),
        ON_HOLD("On Hold"),
        CANCELLED("Cancelled");

        private final String displayName;

        ProjectStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    private int projectId;
    private String projectName;
    private String description;
    private int createdBy;
    private LocalDate startDate;
    private LocalDate endDate;
    private ProjectStatus status;
    private double budget;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<User> projectMembers;
    private List<Task> tasks;

    // Default Constructor
    public Project() {
        this.projectMembers = new ArrayList<>();
        this.tasks = new ArrayList<>();
    }

    // Constructor with essential fields
    public Project(String projectName, String description, int createdBy, 
                   LocalDate startDate, LocalDate endDate) {
        this.projectName = projectName;
        this.description = description;
        this.createdBy = createdBy;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = ProjectStatus.ACTIVE;
        this.projectMembers = new ArrayList<>();
        this.tasks = new ArrayList<>();
    }

    // Full Constructor
    public Project(int projectId, String projectName, String description, int createdBy,
                   LocalDate startDate, LocalDate endDate, ProjectStatus status, double budget) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.description = description;
        this.createdBy = createdBy;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.budget = budget;
        this.projectMembers = new ArrayList<>();
        this.tasks = new ArrayList<>();
    }

    // Getters and Setters
    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
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

    public List<User> getProjectMembers() {
        return projectMembers;
    }

    public void setProjectMembers(List<User> projectMembers) {
        this.projectMembers = projectMembers;
    }

    public void addProjectMember(User user) {
        if (!this.projectMembers.contains(user)) {
            this.projectMembers.add(user);
        }
    }

    public void removeProjectMember(User user) {
        this.projectMembers.remove(user);
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public void addTask(Task task) {
        this.tasks.add(task);
    }

    public void removeTask(Task task) {
        this.tasks.remove(task);
    }

    // Utility methods
    public int getCompletionPercentage() {
        if (tasks.isEmpty()) return 0;
        long completedTasks = tasks.stream()
                .filter(task -> task.getStatus() == Task.TaskStatus.COMPLETED)
                .count();
        return (int) ((completedTasks * 100) / tasks.size());
    }

    public int getTotalTeamMembers() {
        return projectMembers.size();
    }

    public long getActiveTaskCount() {
        return tasks.stream()
                .filter(task -> task.getStatus() != Task.TaskStatus.COMPLETED)
                .count();
    }

    @Override
    public String toString() {
        return "Project{" +
                "projectId=" + projectId +
                ", projectName='" + projectName + '\'' +
                ", status=" + status +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return projectId == project.projectId;
    }

    @Override
    public int hashCode() {
        return projectId;
    }
}
