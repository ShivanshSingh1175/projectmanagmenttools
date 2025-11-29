<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - Project Management Tool</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
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
            transition: 0.3s;
        }
        .sidebar a:hover {
            background: rgba(255, 255, 255, 0.2);
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
        .stat-card h3 {
            color: #667eea;
            font-weight: 600;
        }
        .stat-card .value {
            font-size: 2.5em;
            font-weight: 700;
            color: #333;
        }
        .navbar-brand {
            color: white;
            font-weight: 600;
        }
    </style>
</head>
<body>
    <div class="container-fluid">
        <div class="row">
            <!-- Sidebar -->
            <div class="col-md-2 sidebar">
                <h4>📊 Admin Panel</h4>
                <a href="${pageContext.request.contextPath}/admin-dashboard?action=view">
                    <i class="fas fa-home"></i> Dashboard
                </a>
                <a href="${pageContext.request.contextPath}/admin-dashboard?action=manage-users">
                    <i class="fas fa-users"></i> Manage Users
                </a>
                <a href="${pageContext.request.contextPath}/admin-dashboard?action=manage-projects">
                    <i class="fas fa-project-diagram"></i> Manage Projects
                </a>
                <a href="${pageContext.request.contextPath}/admin-dashboard?action=view-analytics">
                    <i class="fas fa-chart-bar"></i> Analytics
                </a>
                <hr style="border-color: rgba(255, 255, 255, 0.3);">
                <a href="${pageContext.request.contextPath}/login?action=logout">
                    <i class="fas fa-sign-out-alt"></i> Logout
                </a>
            </div>

            <!-- Main Content -->
            <div class="col-md-10 main-content">
                <div class="d-flex justify-content-between align-items-center mb-4">
                    <h2>Admin Dashboard</h2>
                    <span class="badge bg-primary">Welcome, ${userName}</span>
                </div>

                <c:if test="${not empty successMessage}">
                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                        ${successMessage}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>

                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        ${errorMessage}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>

                <!-- Statistics -->
                <div class="row">
                    <div class="col-md-3">
                        <div class="stat-card">
                            <h3>Total Users</h3>
                            <div class="value">${totalUsers}</div>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="stat-card">
                            <h3>Admins</h3>
                            <div class="value">${adminCount}</div>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="stat-card">
                            <h3>Project Managers</h3>
                            <div class="value">${pmCount}</div>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="stat-card">
                            <h3>Team Members</h3>
                            <div class="value">${memberCount}</div>
                        </div>
                    </div>
                </div>

                <!-- Chart -->
                <div style="background: white; padding: 20px; border-radius: 10px; margin-top: 30px; box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);">
                    <h5>User Distribution</h5>
                    <canvas id="userChart" style="max-width: 400px;"></canvas>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // User Distribution Chart
        const ctx = document.getElementById('userChart').getContext('2d');
        const userChart = new Chart(ctx, {
            type: 'doughnut',
            data: {
                labels: ['Admins', 'Project Managers', 'Team Members'],
                datasets: [{
                    data: [${adminCount}, ${pmCount}, ${memberCount}],
                    backgroundColor: ['#667eea', '#764ba2', '#f093fb'],
                    borderColor: '#fff',
                    borderWidth: 2
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        position: 'bottom'
                    }
                }
            }
        });
    </script>
</body>
</html>
