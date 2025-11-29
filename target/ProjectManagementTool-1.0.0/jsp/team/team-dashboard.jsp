<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Team Member Dashboard - Project Management Tool</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .sidebar {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            padding: 20px;
            color: white;
        }
        .sidebar a {
            color: white;
            text-decoration: none;
            display: block;
            padding: 10px 15px;
            margin: 5px 0;
            border-radius: 5px;
        }
        .main-content {
            padding: 30px;
        }
        .stat-card {
            background: white;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            text-align: center;
            margin-bottom: 20px;
        }
        .stat-card .value {
            font-size: 2.5em;
            font-weight: 700;
            color: #667eea;
        }
        .task-card {
            background: white;
            padding: 15px;
            border-radius: 8px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            margin-bottom: 15px;
            border-left: 4px solid #667eea;
        }
    </style>
</head>
<body>
    <div class="container-fluid">
        <div class="row">
            <div class="col-md-2 sidebar">
                <h4>👤 Team Dashboard</h4>
                <a href="${pageContext.request.contextPath}/team-dashboard?action=view">
                    <i class="fas fa-home"></i> Dashboard
                </a>
                <a href="${pageContext.request.contextPath}/team-dashboard?action=view-tasks">
                    <i class="fas fa-tasks"></i> My Tasks
                </a>
                <a href="${pageContext.request.contextPath}/team-dashboard?action=view-profile">
                    <i class="fas fa-user"></i> Profile
                </a>
                <a href="${pageContext.request.contextPath}/team-dashboard?action=view-activity">
                    <i class="fas fa-history"></i> Activity Feed
                </a>
                <hr style="border-color: rgba(255, 255, 255, 0.3);">
                <a href="${pageContext.request.contextPath}/login?action=logout">
                    <i class="fas fa-sign-out-alt"></i> Logout
                </a>
            </div>

            <div class="col-md-10 main-content">
                <h2>Team Member Dashboard</h2>
                <span class="badge bg-primary">Welcome, ${userName}</span>

                <!-- Statistics -->
                <div class="row mt-4">
                    <div class="col-md-3">
                        <div class="stat-card">
                            <h3>Assigned Tasks</h3>
                            <div class="value">${totalAssignedTasks}</div>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="stat-card">
                            <h3>Completed</h3>
                            <div class="value">${completedTasks}</div>
                        </div>
                    </div>
                </div>

                <!-- Recent Tasks -->
                <div style="background: white; padding: 20px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1); margin-top: 30px;">
                    <h4>My Tasks</h4>
                    <c:forEach var="task" items="${assignedTasks}">
                        <div class="task-card">
                            <h5>${task.taskName}</h5>
                            <p>${task.description}</p>
                            <div>
                                <span class="badge bg-info">${task.priority.displayName}</span>
                                <span class="badge bg-warning">${task.status.displayName}</span>
                                <small class="text-muted">Due: ${task.dueDate}</small>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
