package com.pmt.controllers;

import com.pmt.models.User;
import com.pmt.service.UserService;
import com.pmt.exception.PMTException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * AdminDashboardServlet - Handles admin dashboard operations
 * Demonstrates role-based access control
 */
@WebServlet("/admin-dashboard")
public class AdminDashboardServlet extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;
    private UserService userService;

    @Override
    public void init() throws ServletException {
        this.userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        
        // Check if user is logged in and is ADMIN
        if (session == null || !isAdmin(session)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");

        try {
            if (action == null || "view".equals(action)) {
                displayDashboard(request, response, session);
            } else if ("manage-users".equals(action)) {
                displayUserManagement(request, response);
            } else if ("view-analytics".equals(action)) {
                displayAnalytics(request, response);
            } else {
                displayDashboard(request, response, session);
            }
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Error loading dashboard: " + e.getMessage());
            request.getRequestDispatcher("/jsp/admin/admin-dashboard.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        
        if (session == null || !isAdmin(session)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");

        try {
            if ("create-user".equals(action)) {
                handleCreateUser(request, response);
            } else if ("update-user".equals(action)) {
                handleUpdateUser(request, response);
            } else if ("delete-user".equals(action)) {
                handleDeleteUser(request, response);
            }
        } catch (PMTException e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/jsp/admin/admin-dashboard.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Error: " + e.getMessage());
            request.getRequestDispatcher("/jsp/admin/admin-dashboard.jsp").forward(request, response);
        }
    }

    /**
     * Display admin dashboard
     */
    private void displayDashboard(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws Exception {
        try {
            int totalUsers = userService.getActiveUsersCount();
            int adminCount = userService.getUsersByRole("ADMIN").size();
            int pmCount = userService.getUsersByRole("PROJECT_MANAGER").size();
            int memberCount = userService.getUsersByRole("TEAM_MEMBER").size();

            request.setAttribute("totalUsers", totalUsers);
            request.setAttribute("adminCount", adminCount);
            request.setAttribute("pmCount", pmCount);
            request.setAttribute("memberCount", memberCount);
            request.setAttribute("userName", session.getAttribute("userName"));

            request.getRequestDispatcher("/jsp/admin/admin-dashboard.jsp").forward(request, response);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Display user management page
     */
    private void displayUserManagement(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        List<User> users = userService.getAllUsers();
        request.setAttribute("users", users);
        request.getRequestDispatcher("/jsp/admin/manage-users.jsp").forward(request, response);
    }

    /**
     * Handle create user
     */
    private void handleCreateUser(HttpServletRequest request, HttpServletResponse response)
            throws PMTException, ServletException, IOException, Exception {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        User user = new User(name, email, password, User.UserRole.valueOf(role));
        user.setDepartment(request.getParameter("department"));
        user.setPhone(request.getParameter("phone"));

        if (userService.registerUser(user)) {
            request.setAttribute("successMessage", "User created successfully");
        } else {
            request.setAttribute("errorMessage", "Failed to create user");
        }

        displayUserManagement(request, response);
    }

    /**
     * Handle update user
     */
    private void handleUpdateUser(HttpServletRequest request, HttpServletResponse response)
            throws PMTException, ServletException, IOException, Exception {
        int userId = Integer.parseInt(request.getParameter("userId"));
        User user = userService.getUserById(userId);

        user.setName(request.getParameter("name"));
        user.setEmail(request.getParameter("email"));
        user.setRole(User.UserRole.valueOf(request.getParameter("role")));
        user.setDepartment(request.getParameter("department"));
        user.setPhone(request.getParameter("phone"));

        if (userService.updateUser(user)) {
            request.setAttribute("successMessage", "User updated successfully");
        } else {
            request.setAttribute("errorMessage", "Failed to update user");
        }

        displayUserManagement(request, response);
    }

    /**
     * Handle delete user
     */
    private void handleDeleteUser(HttpServletRequest request, HttpServletResponse response)
            throws PMTException, ServletException, IOException, Exception {
        int userId = Integer.parseInt(request.getParameter("userId"));

        if (userService.deleteUser(userId)) {
            request.setAttribute("successMessage", "User deleted successfully");
        } else {
            request.setAttribute("errorMessage", "Failed to delete user");
        }

        displayUserManagement(request, response);
    }

    /**
     * Display analytics/reports
     */
    private void displayAnalytics(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        try {
            int totalUsers = userService.getActiveUsersCount();
            request.setAttribute("totalUsers", totalUsers);
            request.getRequestDispatcher("/jsp/admin/analytics.jsp").forward(request, response);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Check if user is admin
     */
    private boolean isAdmin(HttpSession session) {
        String role = (String) session.getAttribute("userRole");
        return "ADMIN".equals(role);
    }
}
