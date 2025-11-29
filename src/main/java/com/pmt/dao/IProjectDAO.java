package com.pmt.dao;

import com.pmt.models.Project;
import java.util.List;

/**
 * Project DAO Interface - Defines contract for project data operations
 */
public interface IProjectDAO {
    /**
     * Create a new project
     */
    boolean createProject(Project project) throws Exception;

    /**
     * Get project by ID
     */
    Project getProjectById(int projectId) throws Exception;

    /**
     * Get all projects
     */
    List<Project> getAllProjects() throws Exception;

    /**
     * Get projects by status
     */
    List<Project> getProjectsByStatus(String status) throws Exception;

    /**
     * Get projects created by a user
     */
    List<Project> getProjectsByCreator(int userId) throws Exception;

    /**
     * Update project
     */
    boolean updateProject(Project project) throws Exception;

    /**
     * Delete project
     */
    boolean deleteProject(int projectId) throws Exception;

    /**
     * Get total projects count
     */
    int getTotalProjectsCount() throws Exception;

    /**
     * Get active projects count
     */
    int getActiveProjectsCount() throws Exception;

    /**
     * Add member to project
     */
    boolean addProjectMember(int projectId, int userId, String role) throws Exception;

    /**
     * Remove member from project
     */
    boolean removeProjectMember(int projectId, int userId) throws Exception;

    /**
     * Get projects by team member
     */
    List<Project> getProjectsByTeamMember(int userId) throws Exception;
}
