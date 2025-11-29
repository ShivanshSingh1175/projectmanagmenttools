package com.pmt.controllers;

import com.pmt.models.User;
import com.pmt.service.UserService;
import com.pmt.util.CommonUtil;
import com.pmt.exception.PMTException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * LoginServlet - Handles user authentication
 * Demonstrates Servlet and Session management
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
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
        // Redirect to login.jsp
        request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String action = request.getParameter("action");

        try {
            if ("logout".equals(action)) {
                handleLogout(request, response);
                return;
            }

            // Authenticate user
            User user = userService.authenticateUser(email, password);

            if (user != null) {
                // Create session
                HttpSession session = request.getSession(true);
                session.setAttribute("userId", user.getUserId());
                session.setAttribute("userName", user.getName());
                session.setAttribute("userEmail", user.getEmail());
                session.setAttribute("userRole", user.getRole().name());
                session.setMaxInactiveInterval(1800); // 30 minutes

                // Redirect based on role
                redirectByRole(user.getRole().name(), response);
            } else {
                request.setAttribute("errorMessage", "Invalid email or password");
                request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
            }
        } catch (PMTException e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("errorMessage", "An error occurred during login");
            try {
                request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
            } catch (ServletException | IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Handle user logout
     */
    private void handleLogout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
    }

    /**
     * Redirect user based on role
     */
    private void redirectByRole(String role, HttpServletResponse response) throws IOException {
        switch (role) {
            case "ADMIN":
                response.sendRedirect("admin-dashboard");
                break;
            case "PROJECT_MANAGER":
                response.sendRedirect("pm-dashboard");
                break;
            case "TEAM_MEMBER":
                response.sendRedirect("team-dashboard");
                break;
            default:
                response.sendRedirect("login");
        }
    }
}
