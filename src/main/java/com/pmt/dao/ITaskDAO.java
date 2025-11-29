package com.pmt.dao;

import com.pmt.models.Task;
import java.util.List;

/**
 * Task DAO Interface - Defines contract for task data operations
 */
public interface ITaskDAO {
    /**
     * Create a new task
     */
    boolean createTask(Task task) throws Exception;

    /**
     * Get task by ID
     */
    Task getTaskById(int taskId) throws Exception;

    /**
     * Get all tasks
     */
    List<Task> getAllTasks() throws Exception;

    /**
     * Get tasks by project
     */
    List<Task> getTasksByProject(int projectId) throws Exception;

    /**
     * Get tasks assigned to user
     */
    List<Task> getTasksByAssignee(int userId) throws Exception;

    /**
     * Get tasks by status
     */
    List<Task> getTasksByStatus(String status) throws Exception;

    /**
     * Get tasks by priority
     */
    List<Task> getTasksByPriority(String priority) throws Exception;

    /**
     * Update task
     */
    boolean updateTask(Task task) throws Exception;

    /**
     * Delete task
     */
    boolean deleteTask(int taskId) throws Exception;

    /**
     * Get overdue tasks
     */
    List<Task> getOverdueTasks() throws Exception;

    /**
     * Get total tasks count
     */
    int getTotalTasksCount() throws Exception;

    /**
     * Get completed tasks count
     */
    int getCompletedTasksCount() throws Exception;
}
