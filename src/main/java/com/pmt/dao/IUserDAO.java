package com.pmt.dao;

import com.pmt.models.User;
import java.util.List;

/**
 * User DAO Interface - Defines contract for user data operations
 * Demonstrates Interface abstraction principle
 */
public interface IUserDAO {
    /**
     * Create a new user
     */
    boolean createUser(User user) throws Exception;

    /**
     * Get user by ID
     */
    User getUserById(int userId) throws Exception;

    /**
     * Get user by email
     */
    User getUserByEmail(String email) throws Exception;

    /**
     * Get all users
     */
    List<User> getAllUsers() throws Exception;

    /**
     * Update user information
     */
    boolean updateUser(User user) throws Exception;

    /**
     * Delete user by ID
     */
    boolean deleteUser(int userId) throws Exception;

    /**
     * Get users by role
     */
    List<User> getUsersByRole(String role) throws Exception;

    /**
     * Check if email exists
     */
    boolean emailExists(String email) throws Exception;

    /**
     * Authenticate user
     */
    User authenticateUser(String email, String password) throws Exception;

    /**
     * Get active users count
     */
    int getActiveUsersCount() throws Exception;
}
