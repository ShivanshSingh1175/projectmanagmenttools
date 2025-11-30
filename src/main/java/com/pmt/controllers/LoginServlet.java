package com.pmt.controllers;

import java.io.IOException;
import java.io.Serial;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.pmt.exception.PMTException;
import com.pmt.models.User;
import com.pmt.service.UserService;

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
        System.out.println("[DEBUG][LoginServlet] GET " + request.getRequestURI());
        // Prefer forwarding to JSP only if a JSP servlet is available at runtime; otherwise return a fallback HTML page
        try {
            Class.forName("org.eclipse.jetty.jsp.JettyJspServlet");
            // JSP available; try to forward
            if (getServletContext().getResource("/jsp/login.jsp") != null) {
                request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
                return;
            }
        } catch (ClassNotFoundException ignored) {
            // JSP not available; use fallback HTML
        }

        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write("<!doctype html><html><head><meta charset='utf-8'><title>Login</title></head><body>\n");
        response.getWriter().write("<h2>Login (fallback)</h2>\n");
        response.getWriter().write("<form method='post' action='" + request.getContextPath() + "/login'>\n");
        response.getWriter().write("<label>Email: <input name='email' type='email'></label><br/>\n");
        response.getWriter().write("<label>Password: <input name='password' type='password'></label><br/>\n");
        response.getWriter().write("<button type='submit'>Login</button>\n");
        response.getWriter().write("</form></body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("[DEBUG][LoginServlet] POST " + request.getRequestURI());
        
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
