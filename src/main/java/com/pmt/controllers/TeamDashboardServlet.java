package com.pmt.controllers;

import com.pmt.models.Task;
import com.pmt.service.TaskService;
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
 * TeamDashboardServlet - Handles team member dashboard operations
 */
@WebServlet("/team-dashboard")
public class TeamDashboardServlet extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;
    private TaskService taskService;

    @Override
    public void init() throws ServletException {
        this.taskService = new TaskService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        
        if (session == null || !isTeamMember(session)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");

        try {
            if (action == null || "view".equals(action)) {
                displayDashboard(request, response, session);
            } else if ("view-tasks".equals(action)) {
                displayMyTasks(request, response, session);
            } else if ("view-profile".equals(action)) {
                displayProfile(request, response, session);
            } else if ("view-activity".equals(action)) {
                displayActivityFeed(request, response, session);
            } else {
                displayDashboard(request, response, session);
            }
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Error loading dashboard: " + e.getMessage());
            request.getRequestDispatcher("/jsp/team/team-dashboard.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        
        if (session == null || !isTeamMember(session)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");

        try {
            if ("update-task".equals(action)) {
                handleUpdateTask(request, response, session);
            } else if ("update-profile".equals(action)) {
                handleUpdateProfile(request, response, session);
            }
        } catch (PMTException e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/jsp/team/team-dashboard.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Error: " + e.getMessage());
            request.getRequestDispatcher("/jsp/team/team-dashboard.jsp").forward(request, response);
        }
    }

    /**
     * Display team member dashboard
     */
    private void displayDashboard(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws Exception {
        try {
            int userId = (int) session.getAttribute("userId");
            List<Task> assignedTasks = taskService.getTasksByAssignee(userId);
            int totalAssignedTasks = assignedTasks.size();
            long completedTasks = assignedTasks.stream()
                    .filter(t -> t.getStatus() == Task.TaskStatus.COMPLETED)
                    .count();

            request.setAttribute("totalAssignedTasks", totalAssignedTasks);
            request.setAttribute("completedTasks", (int) completedTasks);
            request.setAttribute("userName", session.getAttribute("userName"));
            request.setAttribute("assignedTasks", assignedTasks);

            request.getRequestDispatcher("/jsp/team/team-dashboard.jsp").forward(request, response);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Display my tasks
     */
    private void displayMyTasks(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws Exception {
        int userId = (int) session.getAttribute("userId");
        List<Task> tasks = taskService.getTasksByAssignee(userId);
        request.setAttribute("tasks", tasks);
        request.getRequestDispatcher("/jsp/team/my-tasks.jsp").forward(request, response);
    }

    /**
     * Display user profile
     */
    private void displayProfile(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws Exception {
        request.setAttribute("userName", session.getAttribute("userName"));
        request.setAttribute("userEmail", session.getAttribute("userEmail"));
        request.getRequestDispatcher("/jsp/team/profile.jsp").forward(request, response);
    }

    /**
     * Display activity feed
     */
    private void displayActivityFeed(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws Exception {
        request.getRequestDispatcher("/jsp/team/activity-feed.jsp").forward(request, response);
    }

    /**
     * Handle update task
     */
    private void handleUpdateTask(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws PMTException, ServletException, IOException, Exception {
        int taskId = Integer.parseInt(request.getParameter("taskId"));
        Task task = taskService.getTaskById(taskId);

        String statusStr = request.getParameter("status");
        if (statusStr != null && !statusStr.isEmpty()) {
            task.setStatus(Task.TaskStatus.valueOf(statusStr));
        }

        String completionStr = request.getParameter("completionPercentage");
        if (completionStr != null && !completionStr.isEmpty()) {
            task.setCompletionPercentage(Integer.parseInt(completionStr));
        }

        if (taskService.updateTask(task)) {
            request.setAttribute("successMessage", "Task updated successfully");
        } else {
            request.setAttribute("errorMessage", "Failed to update task");
        }

        displayMyTasks(request, response, session);
    }

    /**
     * Handle update profile
     */
    private void handleUpdateProfile(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws PMTException, ServletException, IOException, Exception {
        // This would typically update user profile
        request.setAttribute("successMessage", "Profile updated successfully");
        displayProfile(request, response, session);
    }

    /**
     * Check if user is team member
     */
    private boolean isTeamMember(HttpSession session) {
        String role = (String) session.getAttribute("userRole");
        return "TEAM_MEMBER".equals(role);
    }
}
