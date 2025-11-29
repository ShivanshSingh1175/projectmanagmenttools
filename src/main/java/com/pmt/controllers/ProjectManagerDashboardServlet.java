package com.pmt.controllers;

import com.pmt.models.Project;
import com.pmt.models.Task;
import com.pmt.service.ProjectService;
import com.pmt.service.TaskService;
import com.pmt.exception.PMTException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * ProjectManagerDashboardServlet - Handles PM dashboard operations
 */
@WebServlet("/pm-dashboard")
public class ProjectManagerDashboardServlet extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;
    private ProjectService projectService;
    private TaskService taskService;

    @Override
    public void init() throws ServletException {
        this.projectService = new ProjectService();
        this.taskService = new TaskService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        
        if (session == null || !isPM(session)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");

        try {
            if (action == null || "view".equals(action)) {
                displayDashboard(request, response, session);
            } else if ("manage-projects".equals(action)) {
                displayProjects(request, response, session);
            } else if ("view-tasks".equals(action)) {
                displayTasks(request, response, session);
            } else {
                displayDashboard(request, response, session);
            }
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Error loading dashboard: " + e.getMessage());
            request.getRequestDispatcher("/jsp/pm/pm-dashboard.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        
        if (session == null || !isPM(session)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");

        try {
            if ("create-project".equals(action)) {
                handleCreateProject(request, response, session);
            } else if ("create-task".equals(action)) {
                handleCreateTask(request, response, session);
            } else if ("update-task".equals(action)) {
                handleUpdateTask(request, response, session);
            }
        } catch (PMTException e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/jsp/pm/pm-dashboard.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Error: " + e.getMessage());
            request.getRequestDispatcher("/jsp/pm/pm-dashboard.jsp").forward(request, response);
        }
    }

    /**
     * Display PM dashboard
     */
    private void displayDashboard(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws Exception {
        try {
            int userId = (int) session.getAttribute("userId");
            List<Project> projects = projectService.getProjectsByCreator(userId);
            int activeProjectCount = projectService.getActiveProjectsCount();
            int totalTasksCount = taskService.getTotalTasksCount();

            request.setAttribute("projects", projects);
            request.setAttribute("activeProjectCount", activeProjectCount);
            request.setAttribute("totalTasksCount", totalTasksCount);
            request.setAttribute("userName", session.getAttribute("userName"));

            request.getRequestDispatcher("/jsp/pm/pm-dashboard.jsp").forward(request, response);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Display projects
     */
    private void displayProjects(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws Exception {
        int userId = (int) session.getAttribute("userId");
        List<Project> projects = projectService.getProjectsByCreator(userId);
        request.setAttribute("projects", projects);
        request.getRequestDispatcher("/jsp/pm/manage-projects.jsp").forward(request, response);
    }

    /**
     * Display tasks
     */
    private void displayTasks(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws Exception {
        List<Task> tasks = taskService.getAllTasks();
        request.setAttribute("tasks", tasks);
        request.getRequestDispatcher("/jsp/pm/task-list.jsp").forward(request, response);
    }

    /**
     * Handle create project
     */
    private void handleCreateProject(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws PMTException, ServletException, IOException, Exception {
        int userId = (int) session.getAttribute("userId");
        String projectName = request.getParameter("projectName");
        String description = request.getParameter("description");
        LocalDate startDate = LocalDate.parse(request.getParameter("startDate"));
        LocalDate endDate = LocalDate.parse(request.getParameter("endDate"));
        double budget = Double.parseDouble(request.getParameter("budget"));

        Project project = new Project(projectName, description, userId, startDate, endDate);
        project.setBudget(budget);

        if (projectService.createProject(project)) {
            request.setAttribute("successMessage", "Project created successfully");
        } else {
            request.setAttribute("errorMessage", "Failed to create project");
        }

        displayProjects(request, response, session);
    }

    /**
     * Handle create task
     */
    private void handleCreateTask(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws PMTException, ServletException, IOException, Exception {
        int userId = (int) session.getAttribute("userId");
        int projectId = Integer.parseInt(request.getParameter("projectId"));
        String taskName = request.getParameter("taskName");
        int assignedTo = Integer.parseInt(request.getParameter("assignedTo"));
        LocalDate dueDate = LocalDate.parse(request.getParameter("dueDate"));

        Task task = new Task(taskName, projectId, assignedTo, userId, dueDate);
        task.setDescription(request.getParameter("description"));
        task.setPriority(Task.TaskPriority.valueOf(request.getParameter("priority")));

        if (taskService.createTask(task)) {
            request.setAttribute("successMessage", "Task created successfully");
        } else {
            request.setAttribute("errorMessage", "Failed to create task");
        }

        displayTasks(request, response, session);
    }

    /**
     * Handle update task
     */
    private void handleUpdateTask(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws PMTException, ServletException, IOException, Exception {
        int taskId = Integer.parseInt(request.getParameter("taskId"));
        Task task = taskService.getTaskById(taskId);

        task.setTaskName(request.getParameter("taskName"));
        task.setStatus(Task.TaskStatus.valueOf(request.getParameter("status")));
        task.setCompletionPercentage(Integer.parseInt(request.getParameter("completionPercentage")));

        if (taskService.updateTask(task)) {
            request.setAttribute("successMessage", "Task updated successfully");
        } else {
            request.setAttribute("errorMessage", "Failed to update task");
        }

        displayTasks(request, response, session);
    }

    /**
     * Check if user is PM
     */
    private boolean isPM(HttpSession session) {
        String role = (String) session.getAttribute("userRole");
        return "PROJECT_MANAGER".equals(role);
    }
}
