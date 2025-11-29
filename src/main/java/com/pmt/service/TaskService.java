package com.pmt.service;

import com.pmt.dao.TaskDAO;
import com.pmt.exception.PMTException;
import com.pmt.models.Task;
import java.util.List;

/**
 * TaskService - Business logic layer for Task operations
 */
public class TaskService {
    private final TaskDAO taskDAO;

    public TaskService() {
        this.taskDAO = new TaskDAO();
    }

    /**
     * Create a new task
     */
    public boolean createTask(Task task) throws PMTException {
        try {
            if (task == null || task.getTaskName() == null) {
                throw new PMTException("Task data is invalid", "INVALID_TASK", 400);
            }

            return taskDAO.createTask(task);
        } catch (Exception e) {
            throw new PMTException("Failed to create task: " + e.getMessage(), "CREATE_ERROR", 500);
        }
    }

    /**
     * Get task by ID
     */
    public Task getTaskById(int taskId) throws PMTException {
        try {
            if (taskId <= 0) {
                throw new PMTException("Invalid task ID", "INVALID_ID", 400);
            }

            Task task = taskDAO.getTaskById(taskId);
            if (task == null) {
                throw new PMTException("Task not found", "TASK_NOT_FOUND", 404);
            }
            return task;
        } catch (Exception e) {
            throw new PMTException("Failed to retrieve task: " + e.getMessage(), "RETRIEVE_ERROR", 500);
        }
    }

    /**
     * Get all tasks
     */
    public List<Task> getAllTasks() throws PMTException {
        try {
            return taskDAO.getAllTasks();
        } catch (Exception e) {
            throw new PMTException("Failed to retrieve tasks: " + e.getMessage(), "RETRIEVE_ERROR", 500);
        }
    }

    /**
     * Get tasks by project
     */
    public List<Task> getTasksByProject(int projectId) throws PMTException {
        try {
            return taskDAO.getTasksByProject(projectId);
        } catch (Exception e) {
            throw new PMTException("Failed to retrieve tasks: " + e.getMessage(), "RETRIEVE_ERROR", 500);
        }
    }

    /**
     * Get tasks assigned to user
     */
    public List<Task> getTasksByAssignee(int userId) throws PMTException {
        try {
            return taskDAO.getTasksByAssignee(userId);
        } catch (Exception e) {
            throw new PMTException("Failed to retrieve tasks: " + e.getMessage(), "RETRIEVE_ERROR", 500);
        }
    }

    /**
     * Get tasks by status
     */
    public List<Task> getTasksByStatus(String status) throws PMTException {
        try {
            return taskDAO.getTasksByStatus(status);
        } catch (Exception e) {
            throw new PMTException("Failed to retrieve tasks: " + e.getMessage(), "RETRIEVE_ERROR", 500);
        }
    }

    /**
     * Get tasks by priority
     */
    public List<Task> getTasksByPriority(String priority) throws PMTException {
        try {
            return taskDAO.getTasksByPriority(priority);
        } catch (Exception e) {
            throw new PMTException("Failed to retrieve tasks: " + e.getMessage(), "RETRIEVE_ERROR", 500);
        }
    }

    /**
     * Update task
     */
    public boolean updateTask(Task task) throws PMTException {
        try {
            if (task == null || task.getTaskId() <= 0) {
                throw new PMTException("Invalid task data", "INVALID_TASK", 400);
            }

            return taskDAO.updateTask(task);
        } catch (Exception e) {
            throw new PMTException("Failed to update task: " + e.getMessage(), "UPDATE_ERROR", 500);
        }
    }

    /**
     * Delete task
     */
    public boolean deleteTask(int taskId) throws PMTException {
        try {
            if (taskId <= 0) {
                throw new PMTException("Invalid task ID", "INVALID_ID", 400);
            }

            return taskDAO.deleteTask(taskId);
        } catch (Exception e) {
            throw new PMTException("Failed to delete task: " + e.getMessage(), "DELETE_ERROR", 500);
        }
    }

    /**
     * Get overdue tasks
     */
    public List<Task> getOverdueTasks() throws PMTException {
        try {
            return taskDAO.getOverdueTasks();
        } catch (Exception e) {
            throw new PMTException("Failed to retrieve overdue tasks: " + e.getMessage(), "RETRIEVE_ERROR", 500);
        }
    }

    /**
     * Get total tasks count
     */
    public int getTotalTasksCount() throws PMTException {
        try {
            return taskDAO.getTotalTasksCount();
        } catch (Exception e) {
            throw new PMTException("Failed to get task count: " + e.getMessage(), "COUNT_ERROR", 500);
        }
    }

    /**
     * Get completed tasks count
     */
    public int getCompletedTasksCount() throws PMTException {
        try {
            return taskDAO.getCompletedTasksCount();
        } catch (Exception e) {
            throw new PMTException("Failed to get completed task count: " + e.getMessage(), "COUNT_ERROR", 500);
        }
    }

    /**
     * Get task completion percentage
     */
    public double getProjectCompletionPercentage(int projectId) throws PMTException {
        try {
            List<Task> tasks = taskDAO.getTasksByProject(projectId);
            if (tasks.isEmpty()) return 0;

            long completedTasks = tasks.stream()
                    .filter(t -> t.getStatus() == Task.TaskStatus.COMPLETED)
                    .count();

            return (completedTasks * 100.0) / tasks.size();
        } catch (Exception e) {
            throw new PMTException("Failed to calculate completion percentage: " + e.getMessage(), "CALC_ERROR", 500);
        }
    }
}
