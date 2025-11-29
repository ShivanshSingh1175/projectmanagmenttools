package com.pmt.dao;

import com.pmt.models.Project;
import com.pmt.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Project DAO Implementation - Implements JDBC operations for Project
 */
public class ProjectDAO implements IProjectDAO {

    @Override
    public boolean createProject(Project project) throws Exception {
        String sql = "INSERT INTO projects (project_name, description, created_by, start_date, end_date, status, budget, created_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, NOW())";
        
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, project.getProjectName());
            pstmt.setString(2, project.getDescription());
            pstmt.setInt(3, project.getCreatedBy());
            pstmt.setDate(4, Date.valueOf(project.getStartDate()));
            pstmt.setDate(5, Date.valueOf(project.getEndDate()));
            pstmt.setString(6, project.getStatus().name());
            pstmt.setDouble(7, project.getBudget());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } finally {
            DBConnection.closeAllResources(conn, pstmt, null);
        }
    }

    @Override
    public Project getProjectById(int projectId) throws Exception {
        String sql = "SELECT * FROM projects WHERE project_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, projectId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToProject(rs);
            }
        } finally {
            DBConnection.closeAllResources(conn, pstmt, rs);
        }
        return null;
    }

    @Override
    public List<Project> getAllProjects() throws Exception {
        String sql = "SELECT * FROM projects ORDER BY created_at DESC";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Project> projects = new ArrayList<>();

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                projects.add(mapResultSetToProject(rs));
            }
        } finally {
            DBConnection.closeAllResources(conn, pstmt, rs);
        }
        return projects;
    }

    @Override
    public List<Project> getProjectsByStatus(String status) throws Exception {
        String sql = "SELECT * FROM projects WHERE status = ? ORDER BY created_at DESC";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Project> projects = new ArrayList<>();

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, status);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                projects.add(mapResultSetToProject(rs));
            }
        } finally {
            DBConnection.closeAllResources(conn, pstmt, rs);
        }
        return projects;
    }

    @Override
    public List<Project> getProjectsByCreator(int userId) throws Exception {
        String sql = "SELECT * FROM projects WHERE created_by = ? ORDER BY created_at DESC";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Project> projects = new ArrayList<>();

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                projects.add(mapResultSetToProject(rs));
            }
        } finally {
            DBConnection.closeAllResources(conn, pstmt, rs);
        }
        return projects;
    }

    @Override
    public boolean updateProject(Project project) throws Exception {
        String sql = "UPDATE projects SET project_name=?, description=?, start_date=?, end_date=?, status=?, budget=? WHERE project_id=?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, project.getProjectName());
            pstmt.setString(2, project.getDescription());
            pstmt.setDate(3, Date.valueOf(project.getStartDate()));
            pstmt.setDate(4, Date.valueOf(project.getEndDate()));
            pstmt.setString(5, project.getStatus().name());
            pstmt.setDouble(6, project.getBudget());
            pstmt.setInt(7, project.getProjectId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } finally {
            DBConnection.closeAllResources(conn, pstmt, null);
        }
    }

    @Override
    public boolean deleteProject(int projectId) throws Exception {
        String sql = "DELETE FROM projects WHERE project_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, projectId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } finally {
            DBConnection.closeAllResources(conn, pstmt, null);
        }
    }

    @Override
    public int getTotalProjectsCount() throws Exception {
        String sql = "SELECT COUNT(*) as count FROM projects";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("count");
            }
        } finally {
            DBConnection.closeAllResources(conn, pstmt, rs);
        }
        return 0;
    }

    @Override
    public int getActiveProjectsCount() throws Exception {
        String sql = "SELECT COUNT(*) as count FROM projects WHERE status = 'ACTIVE'";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("count");
            }
        } finally {
            DBConnection.closeAllResources(conn, pstmt, rs);
        }
        return 0;
    }

    @Override
    public boolean addProjectMember(int projectId, int userId, String role) throws Exception {
        String sql = "INSERT INTO project_members (project_id, user_id, role, joined_date) VALUES (?, ?, ?, NOW())";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, projectId);
            pstmt.setInt(2, userId);
            pstmt.setString(3, role);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } finally {
            DBConnection.closeAllResources(conn, pstmt, null);
        }
    }

    @Override
    public boolean removeProjectMember(int projectId, int userId) throws Exception {
        String sql = "DELETE FROM project_members WHERE project_id = ? AND user_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, projectId);
            pstmt.setInt(2, userId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } finally {
            DBConnection.closeAllResources(conn, pstmt, null);
        }
    }

    @Override
    public List<Project> getProjectsByTeamMember(int userId) throws Exception {
        String sql = "SELECT DISTINCT p.* FROM projects p " +
                     "INNER JOIN project_members pm ON p.project_id = pm.project_id " +
                     "WHERE pm.user_id = ? ORDER BY p.created_at DESC";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Project> projects = new ArrayList<>();

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                projects.add(mapResultSetToProject(rs));
            }
        } finally {
            DBConnection.closeAllResources(conn, pstmt, rs);
        }
        return projects;
    }

    /**
     * Helper method to map ResultSet to Project object
     */
    private Project mapResultSetToProject(ResultSet rs) throws SQLException {
        Project project = new Project();
        project.setProjectId(rs.getInt("project_id"));
        project.setProjectName(rs.getString("project_name"));
        project.setDescription(rs.getString("description"));
        project.setCreatedBy(rs.getInt("created_by"));
        project.setStartDate(rs.getDate("start_date").toLocalDate());
        project.setEndDate(rs.getDate("end_date").toLocalDate());
        project.setStatus(Project.ProjectStatus.valueOf(rs.getString("status")));
        project.setBudget(rs.getDouble("budget"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            project.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            project.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return project;
    }
}
