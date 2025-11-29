package com.pmt.dao;

import com.pmt.models.User;
import com.pmt.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User DAO Implementation - Implements JDBC operations for User
 * Uses prepared statements for security (SQL injection prevention)
 */
public class UserDAO implements IUserDAO {

    @Override
    public boolean createUser(User user) throws Exception {
        String sql = "INSERT INTO users (name, email, password, role, department, phone, is_active, created_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, NOW())";
        
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getRole().name());
            pstmt.setString(5, user.getDepartment());
            pstmt.setString(6, user.getPhone());
            pstmt.setBoolean(7, user.isActive());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } finally {
            DBConnection.closeAllResources(conn, pstmt, null);
        }
    }

    @Override
    public User getUserById(int userId) throws Exception {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } finally {
            DBConnection.closeAllResources(conn, pstmt, rs);
        }
        return null;
    }

    @Override
    public User getUserByEmail(String email) throws Exception {
        String sql = "SELECT * FROM users WHERE email = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } finally {
            DBConnection.closeAllResources(conn, pstmt, rs);
        }
        return null;
    }

    @Override
    public List<User> getAllUsers() throws Exception {
        String sql = "SELECT * FROM users ORDER BY created_at DESC";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<User> users = new ArrayList<>();

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        } finally {
            DBConnection.closeAllResources(conn, pstmt, rs);
        }
        return users;
    }

    @Override
    public boolean updateUser(User user) throws Exception {
        String sql = "UPDATE users SET name=?, email=?, role=?, department=?, phone=?, is_active=? WHERE user_id=?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getRole().name());
            pstmt.setString(4, user.getDepartment());
            pstmt.setString(5, user.getPhone());
            pstmt.setBoolean(6, user.isActive());
            pstmt.setInt(7, user.getUserId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } finally {
            DBConnection.closeAllResources(conn, pstmt, null);
        }
    }

    @Override
    public boolean deleteUser(int userId) throws Exception {
        String sql = "DELETE FROM users WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } finally {
            DBConnection.closeAllResources(conn, pstmt, null);
        }
    }

    @Override
    public List<User> getUsersByRole(String role) throws Exception {
        String sql = "SELECT * FROM users WHERE role = ? ORDER BY name ASC";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<User> users = new ArrayList<>();

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, role);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        } finally {
            DBConnection.closeAllResources(conn, pstmt, rs);
        }
        return users;
    }

    @Override
    public boolean emailExists(String email) throws Exception {
        String sql = "SELECT COUNT(*) as count FROM users WHERE email = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        } finally {
            DBConnection.closeAllResources(conn, pstmt, rs);
        }
        return false;
    }

    @Override
    public User authenticateUser(String email, String password) throws Exception {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ? AND is_active = TRUE";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } finally {
            DBConnection.closeAllResources(conn, pstmt, rs);
        }
        return null;
    }

    @Override
    public int getActiveUsersCount() throws Exception {
        String sql = "SELECT COUNT(*) as count FROM users WHERE is_active = TRUE";
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
     * Helper method to map ResultSet to User object
     */
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setRole(User.UserRole.valueOf(rs.getString("role")));
        user.setDepartment(rs.getString("department"));
        user.setPhone(rs.getString("phone"));
        user.setActive(rs.getBoolean("is_active"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            user.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            user.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return user;
    }
}
