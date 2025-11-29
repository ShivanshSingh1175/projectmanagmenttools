package com.pmt.dao;

import com.pmt.models.Task;
import com.pmt.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Task DAO Implementation - Implements JDBC operations for Task
 */
public class TaskDAO implements ITaskDAO {

    @Override
    public boolean createTask(Task task) throws Exception {
        String sql = "INSERT INTO tasks (project_id, task_name, description, assigned_to, assigned_by, priority, status, start_date, due_date, estimated_hours, created_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())";
        
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, task.getProjectId());
            pstmt.setString(2, task.getTaskName());
            pstmt.setString(3, task.getDescription());
            pstmt.setInt(4, task.getAssignedTo());
            pstmt.setInt(5, task.getAssignedBy());
            pstmt.setString(6, task.getPriority().name());
            pstmt.setString(7, task.getStatus().name());
            
            if (task.getStartDate() != null) {
                pstmt.setDate(8, Date.valueOf(task.getStartDate()));
            } else {
                pstmt.setNull(8, java.sql.Types.DATE);
            }
            
            pstmt.setDate(9, Date.valueOf(task.getDueDate()));
            pstmt.setInt(10, task.getEstimatedHours());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } finally {
            DBConnection.closeAllResources(conn, pstmt, null);
        }
    }

    @Override
    public Task getTaskById(int taskId) throws Exception {
        String sql = "SELECT * FROM tasks WHERE task_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, taskId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToTask(rs);
            }
        } finally {
            DBConnection.closeAllResources(conn, pstmt, rs);
        }
        return null;
    }

    @Override
    public List<Task> getAllTasks() throws Exception {
        String sql = "SELECT * FROM tasks ORDER BY due_date ASC";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Task> tasks = new ArrayList<>();

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                tasks.add(mapResultSetToTask(rs));
            }
        } finally {
            DBConnection.closeAllResources(conn, pstmt, rs);
        }
        return tasks;
    }

    @Override
    public List<Task> getTasksByProject(int projectId) throws Exception {
        String sql = "SELECT * FROM tasks WHERE project_id = ? ORDER BY priority DESC, due_date ASC";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Task> tasks = new ArrayList<>();

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, projectId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                tasks.add(mapResultSetToTask(rs));
            }
        } finally {
            DBConnection.closeAllResources(conn, pstmt, rs);
        }
        return tasks;
    }

    @Override
    public List<Task> getTasksByAssignee(int userId) throws Exception {
        String sql = "SELECT * FROM tasks WHERE assigned_to = ? ORDER BY due_date ASC";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Task> tasks = new ArrayList<>();

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                tasks.add(mapResultSetToTask(rs));
            }
        } finally {
            DBConnection.closeAllResources(conn, pstmt, rs);
        }
        return tasks;
    }

    @Override
    public List<Task> getTasksByStatus(String status) throws Exception {
        String sql = "SELECT * FROM tasks WHERE status = ? ORDER BY due_date ASC";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Task> tasks = new ArrayList<>();

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, status);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                tasks.add(mapResultSetToTask(rs));
            }
        } finally {
            DBConnection.closeAllResources(conn, pstmt, rs);
        }
        return tasks;
    }

    @Override
    public List<Task> getTasksByPriority(String priority) throws Exception {
        String sql = "SELECT * FROM tasks WHERE priority = ? ORDER BY due_date ASC";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Task> tasks = new ArrayList<>();

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, priority);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                tasks.add(mapResultSetToTask(rs));
            }
        } finally {
            DBConnection.closeAllResources(conn, pstmt, rs);
        }
        return tasks;
    }

    @Override
    public boolean updateTask(Task task) throws Exception {
        String sql = "UPDATE tasks SET task_name=?, description=?, assigned_to=?, priority=?, status=?, start_date=?, due_date=?, estimated_hours=?, actual_hours=?, completion_percentage=? WHERE task_id=?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, task.getTaskName());
            pstmt.setString(2, task.getDescription());
            pstmt.setInt(3, task.getAssignedTo());
            pstmt.setString(4, task.getPriority().name());
            pstmt.setString(5, task.getStatus().name());
            
            if (task.getStartDate() != null) {
                pstmt.setDate(6, Date.valueOf(task.getStartDate()));
            } else {
                pstmt.setNull(6, java.sql.Types.DATE);
            }
            
            pstmt.setDate(7, Date.valueOf(task.getDueDate()));
            pstmt.setInt(8, task.getEstimatedHours());
            pstmt.setInt(9, task.getActualHours());
            pstmt.setInt(10, task.getCompletionPercentage());
            pstmt.setInt(11, task.getTaskId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } finally {
            DBConnection.closeAllResources(conn, pstmt, null);
        }
    }

    @Override
    public boolean deleteTask(int taskId) throws Exception {
        String sql = "DELETE FROM tasks WHERE task_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, taskId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } finally {
            DBConnection.closeAllResources(conn, pstmt, null);
        }
    }

    @Override
    public List<Task> getOverdueTasks() throws Exception {
        String sql = "SELECT * FROM tasks WHERE due_date < CURDATE() AND status != 'COMPLETED' ORDER BY due_date ASC";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Task> tasks = new ArrayList<>();

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                tasks.add(mapResultSetToTask(rs));
            }
        } finally {
            DBConnection.closeAllResources(conn, pstmt, rs);
        }
        return tasks;
    }

    @Override
    public int getTotalTasksCount() throws Exception {
        String sql = "SELECT COUNT(*) as count FROM tasks";
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
    public int getCompletedTasksCount() throws Exception {
        String sql = "SELECT COUNT(*) as count FROM tasks WHERE status = 'COMPLETED'";
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

    /**
     * Helper method to map ResultSet to Task object
     */
    private Task mapResultSetToTask(ResultSet rs) throws SQLException {
        Task task = new Task();
        task.setTaskId(rs.getInt("task_id"));
        task.setProjectId(rs.getInt("project_id"));
        task.setTaskName(rs.getString("task_name"));
        task.setDescription(rs.getString("description"));
        task.setAssignedTo(rs.getInt("assigned_to"));
        task.setAssignedBy(rs.getInt("assigned_by"));
        task.setPriority(Task.TaskPriority.valueOf(rs.getString("priority")));
        task.setStatus(Task.TaskStatus.valueOf(rs.getString("status")));
        
        Date startDate = rs.getDate("start_date");
        if (startDate != null) {
            task.setStartDate(startDate.toLocalDate());
        }
        
        task.setDueDate(rs.getDate("due_date").toLocalDate());
        task.setEstimatedHours(rs.getInt("estimated_hours"));
        task.setActualHours(rs.getInt("actual_hours"));
        task.setCompletionPercentage(rs.getInt("completion_percentage"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            task.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            task.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return task;
    }
}
