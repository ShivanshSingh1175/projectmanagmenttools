package com.pmt.service;

import com.pmt.dao.ProjectDAO;
import com.pmt.exception.PMTException;
import com.pmt.models.Project;
import java.util.List;

/**
 * ProjectService - Business logic layer for Project operations
 */
public class ProjectService {
    private final ProjectDAO projectDAO;

    public ProjectService() {
        this.projectDAO = new ProjectDAO();
    }

    /**
     * Create a new project
     */
    public boolean createProject(Project project) throws PMTException {
        try {
            if (project == null || project.getProjectName() == null) {
                throw new PMTException("Project data is invalid", "INVALID_PROJECT", 400);
            }

            if (!isValidDateRange(project.getStartDate(), project.getEndDate())) {
                throw new PMTException("End date must be after start date", "INVALID_DATE_RANGE", 400);
            }

            return projectDAO.createProject(project);
        } catch (Exception e) {
            throw new PMTException("Failed to create project: " + e.getMessage(), "CREATE_ERROR", 500);
        }
    }

    /**
     * Get project by ID
     */
    public Project getProjectById(int projectId) throws PMTException {
        try {
            if (projectId <= 0) {
                throw new PMTException("Invalid project ID", "INVALID_ID", 400);
            }

            Project project = projectDAO.getProjectById(projectId);
            if (project == null) {
                throw new PMTException("Project not found", "PROJECT_NOT_FOUND", 404);
            }
            return project;
        } catch (Exception e) {
            throw new PMTException("Failed to retrieve project: " + e.getMessage(), "RETRIEVE_ERROR", 500);
        }
    }

    /**
     * Get all projects
     */
    public List<Project> getAllProjects() throws PMTException {
        try {
            return projectDAO.getAllProjects();
        } catch (Exception e) {
            throw new PMTException("Failed to retrieve projects: " + e.getMessage(), "RETRIEVE_ERROR", 500);
        }
    }

    /**
     * Get projects by status
     */
    public List<Project> getProjectsByStatus(String status) throws PMTException {
        try {
            return projectDAO.getProjectsByStatus(status);
        } catch (Exception e) {
            throw new PMTException("Failed to retrieve projects: " + e.getMessage(), "RETRIEVE_ERROR", 500);
        }
    }

    /**
     * Get projects by creator
     */
    public List<Project> getProjectsByCreator(int userId) throws PMTException {
        try {
            return projectDAO.getProjectsByCreator(userId);
        } catch (Exception e) {
            throw new PMTException("Failed to retrieve projects: " + e.getMessage(), "RETRIEVE_ERROR", 500);
        }
    }

    /**
     * Get projects by team member
     */
    public List<Project> getProjectsByTeamMember(int userId) throws PMTException {
        try {
            return projectDAO.getProjectsByTeamMember(userId);
        } catch (Exception e) {
            throw new PMTException("Failed to retrieve projects: " + e.getMessage(), "RETRIEVE_ERROR", 500);
        }
    }

    /**
     * Update project
     */
    public boolean updateProject(Project project) throws PMTException {
        try {
            if (project == null || project.getProjectId() <= 0) {
                throw new PMTException("Invalid project data", "INVALID_PROJECT", 400);
            }

            return projectDAO.updateProject(project);
        } catch (Exception e) {
            throw new PMTException("Failed to update project: " + e.getMessage(), "UPDATE_ERROR", 500);
        }
    }

    /**
     * Delete project
     */
    public boolean deleteProject(int projectId) throws PMTException {
        try {
            if (projectId <= 0) {
                throw new PMTException("Invalid project ID", "INVALID_ID", 400);
            }

            return projectDAO.deleteProject(projectId);
        } catch (Exception e) {
            throw new PMTException("Failed to delete project: " + e.getMessage(), "DELETE_ERROR", 500);
        }
    }

    /**
     * Get total projects count
     */
    public int getTotalProjectsCount() throws PMTException {
        try {
            return projectDAO.getTotalProjectsCount();
        } catch (Exception e) {
            throw new PMTException("Failed to get project count: " + e.getMessage(), "COUNT_ERROR", 500);
        }
    }

    /**
     * Get active projects count
     */
    public int getActiveProjectsCount() throws PMTException {
        try {
            return projectDAO.getActiveProjectsCount();
        } catch (Exception e) {
            throw new PMTException("Failed to get active project count: " + e.getMessage(), "COUNT_ERROR", 500);
        }
    }

    /**
     * Add team member to project
     */
    public boolean addProjectMember(int projectId, int userId, String role) throws PMTException {
        try {
            return projectDAO.addProjectMember(projectId, userId, role);
        } catch (Exception e) {
            throw new PMTException("Failed to add project member: " + e.getMessage(), "ADD_MEMBER_ERROR", 500);
        }
    }

    /**
     * Remove team member from project
     */
    public boolean removeProjectMember(int projectId, int userId) throws PMTException {
        try {
            return projectDAO.removeProjectMember(projectId, userId);
        } catch (Exception e) {
            throw new PMTException("Failed to remove project member: " + e.getMessage(), "REMOVE_MEMBER_ERROR", 500);
        }
    }

    /**
     * Helper method to validate date range
     */
    private boolean isValidDateRange(java.time.LocalDate startDate, java.time.LocalDate endDate) {
        return startDate != null && endDate != null && endDate.isAfter(startDate);
    }
}
