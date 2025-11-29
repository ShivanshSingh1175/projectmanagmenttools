package com.pmt.dao;

import com.pmt.models.ActivityLog;
import com.pmt.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ActivityLog DAO Implementation - Tracks all system activities
 */
public class ActivityLogDAO {

    /**
     * Log activity
     */
    public boolean logActivity(ActivityLog log) throws Exception {
        String sql = "INSERT INTO activity_log (user_id, activity_type, entity_type, entity_id, description, old_value, new_value, created_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, NOW())";
        
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, log.getUserId());
            pstmt.setString(2, log.getActivityType().name());
            pstmt.setString(3, log.getEntityType());
            pstmt.setInt(4, log.getEntityId());
            pstmt.setString(5, log.getDescription());
            pstmt.setString(6, log.getOldValue());
            pstmt.setString(7, log.getNewValue());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } finally {
            DBConnection.closeAllResources(conn, pstmt, null);
        }
    }

    /**
     * Get activities by user
     */
    public List<ActivityLog> getActivitiesByUser(int userId, int limit) throws Exception {
        String sql = "SELECT * FROM activity_log WHERE user_id = ? ORDER BY created_at DESC LIMIT ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<ActivityLog> logs = new ArrayList<>();

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            pstmt.setInt(2, limit);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                logs.add(mapResultSetToActivityLog(rs));
            }
        } finally {
            DBConnection.closeAllResources(conn, pstmt, rs);
        }
        return logs;
    }

    /**
     * Get recent activities
     */
    public List<ActivityLog> getRecentActivities(int limit) throws Exception {
        String sql = "SELECT * FROM activity_log ORDER BY created_at DESC LIMIT ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<ActivityLog> logs = new ArrayList<>();

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, limit);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                logs.add(mapResultSetToActivityLog(rs));
            }
        } finally {
            DBConnection.closeAllResources(conn, pstmt, rs);
        }
        return logs;
    }

    /**
     * Get activities for specific entity
     */
    public List<ActivityLog> getActivitiesByEntity(String entityType, int entityId) throws Exception {
        String sql = "SELECT * FROM activity_log WHERE entity_type = ? AND entity_id = ? ORDER BY created_at DESC";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<ActivityLog> logs = new ArrayList<>();

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, entityType);
            pstmt.setInt(2, entityId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                logs.add(mapResultSetToActivityLog(rs));
            }
        } finally {
            DBConnection.closeAllResources(conn, pstmt, rs);
        }
        return logs;
    }

    /**
     * Helper method to map ResultSet to ActivityLog object
     */
    private ActivityLog mapResultSetToActivityLog(ResultSet rs) throws SQLException {
        ActivityLog log = new ActivityLog();
        log.setLogId(rs.getInt("log_id"));
        log.setUserId(rs.getInt("user_id"));
        log.setActivityType(ActivityLog.ActivityType.valueOf(rs.getString("activity_type")));
        log.setEntityType(rs.getString("entity_type"));
        log.setEntityId(rs.getInt("entity_id"));
        log.setDescription(rs.getString("description"));
        log.setOldValue(rs.getString("old_value"));
        log.setNewValue(rs.getString("new_value"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            log.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        return log;
    }
}
