<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Project Manager Dashboard - Project Management Tool</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/chart.js@3.7.0/dist/chart.min.js"></script>
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
            margin-bottom: 20px;
            text-align: center;
        }
        .stat-card .value {
            font-size: 2em;
            font-weight: 700;
            color: #667eea;
        }
    </style>
</head>
<body>
    <div class="container-fluid">
        <div class="row">
            <div class="col-md-2 sidebar">
                <h4>📈 PM Dashboard</h4>
                <a href="${pageContext.request.contextPath}/pm-dashboard?action=view">
                    <i class="fas fa-home"></i> Dashboard
                </a>
                <a href="${pageContext.request.contextPath}/pm-dashboard?action=manage-projects">
                    <i class="fas fa-project-diagram"></i> Projects
                </a>
                <a href="${pageContext.request.contextPath}/pm-dashboard?action=view-tasks">
                    <i class="fas fa-tasks"></i> Tasks
                </a>
                <hr style="border-color: rgba(255, 255, 255, 0.3);">
                <a href="${pageContext.request.contextPath}/login?action=logout">
                    <i class="fas fa-sign-out-alt"></i> Logout
                </a>
            </div>

            <div class="col-md-10 main-content">
                <h2>Project Manager Dashboard</h2>
                <span class="badge bg-primary">Welcome, ${userName}</span>

                <!-- Statistics -->
                <div class="row mt-4">
                    <div class="col-md-4">
                        <div class="stat-card">
                            <h3>Active Projects</h3>
                            <div class="value">${activeProjectCount}</div>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="stat-card">
                            <h3>Total Tasks</h3>
                            <div class="value">${totalTasksCount}</div>
                        </div>
                    </div>
                </div>

                <!-- Projects List -->
                <div style="background: white; padding: 20px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1); margin-top: 30px;">
                    <h4>My Projects</h4>
                    <table class="table table-striped">
                        <thead>
                            <tr>
                                <th>Project Name</th>
                                <th>Status</th>
                                <th>Start Date</th>
                                <th>End Date</th>
                                <th>Budget</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="project" items="${projects}">
                                <tr>
                                    <td>${project.projectName}</td>
                                    <td><span class="badge bg-success">${project.status.displayName}</span></td>
                                    <td>${project.startDate}</td>
                                    <td>${project.endDate}</td>
                                    <td>$${project.budget}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
