package com.pmt.service;

import com.pmt.dao.UserDAO;
import com.pmt.exception.PMTException;
import com.pmt.models.User;
import java.util.List;

/**
 * UserService - Business logic layer for User operations
 * Demonstrates Service layer pattern and abstraction
 */
public class UserService {
    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    /**
     * Register a new user
     */
    public boolean registerUser(User user) throws PMTException {
        try {
            // Validate user input
            if (user == null || user.getName() == null || user.getEmail() == null) {
                throw new PMTException("User data is invalid", "INVALID_USER", 400);
            }

            // Check if email already exists
            if (userDAO.emailExists(user.getEmail())) {
                throw new PMTException("Email already exists in the system", "EMAIL_EXISTS", 400);
            }

            return userDAO.createUser(user);
        } catch (Exception e) {
            throw new PMTException("Failed to register user: " + e.getMessage(), "REG_ERROR", 500);
        }
    }

    /**
     * Authenticate user with email and password
     */
    public User authenticateUser(String email, String password) throws PMTException {
        try {
            if (email == null || password == null) {
                throw new PMTException("Email and password are required", "INVALID_INPUT", 400);
            }

            User user = userDAO.authenticateUser(email, password);
            if (user == null) {
                throw new PMTException("Invalid email or password", "AUTH_FAILED", 401);
            }

            return user;
        } catch (PMTException e) {
            throw e;
        } catch (Exception e) {
            throw new PMTException("Authentication failed: " + e.getMessage(), "AUTH_ERROR", 500);
        }
    }

    /**
     * Get user by ID
     */
    public User getUserById(int userId) throws PMTException {
        try {
            User user = userDAO.getUserById(userId);
            if (user == null) {
                throw new PMTException("User not found", "USER_NOT_FOUND", 404);
            }
            return user;
        } catch (Exception e) {
            throw new PMTException("Failed to retrieve user: " + e.getMessage(), "RETRIEVE_ERROR", 500);
        }
    }

    /**
     * Get all users
     */
    public List<User> getAllUsers() throws PMTException {
        try {
            return userDAO.getAllUsers();
        } catch (Exception e) {
            throw new PMTException("Failed to retrieve users: " + e.getMessage(), "RETRIEVE_ERROR", 500);
        }
    }

    /**
     * Update user information
     */
    public boolean updateUser(User user) throws PMTException {
        try {
            if (user == null || user.getUserId() <= 0) {
                throw new PMTException("Invalid user data", "INVALID_USER", 400);
            }

            return userDAO.updateUser(user);
        } catch (Exception e) {
            throw new PMTException("Failed to update user: " + e.getMessage(), "UPDATE_ERROR", 500);
        }
    }

    /**
     * Delete user
     */
    public boolean deleteUser(int userId) throws PMTException {
        try {
            if (userId <= 0) {
                throw new PMTException("Invalid user ID", "INVALID_ID", 400);
            }

            return userDAO.deleteUser(userId);
        } catch (Exception e) {
            throw new PMTException("Failed to delete user: " + e.getMessage(), "DELETE_ERROR", 500);
        }
    }

    /**
     * Get users by role
     */
    public List<User> getUsersByRole(String role) throws PMTException {
        try {
            if (role == null || role.isEmpty()) {
                throw new PMTException("Role is required", "INVALID_ROLE", 400);
            }

            return userDAO.getUsersByRole(role);
        } catch (Exception e) {
            throw new PMTException("Failed to retrieve users: " + e.getMessage(), "RETRIEVE_ERROR", 500);
        }
    }

    /**
     * Get active users count
     */
    public int getActiveUsersCount() throws PMTException {
        try {
            return userDAO.getActiveUsersCount();
        } catch (Exception e) {
            throw new PMTException("Failed to get user count: " + e.getMessage(), "COUNT_ERROR", 500);
        }
    }
}
