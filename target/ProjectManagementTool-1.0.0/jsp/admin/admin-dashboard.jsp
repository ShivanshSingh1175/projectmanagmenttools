<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - Project Management Tool</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.0/dist/chart.umd.min.js"></script>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
            font-family: 'Segoe UI', Roboto, sans-serif;
        }
        
        .navbar {
            background: white;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            padding: 1rem 2rem;
        }
        
        .navbar-brand {
            font-weight: 700;
            font-size: 1.5rem;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
        }
        
        .sidebar {
            background: white;
            border-right: 1px solid #e2e8f0;
            min-height: calc(100vh - 70px);
            position: fixed;
            left: 0;
            top: 70px;
            width: 250px;
            overflow-y: auto;
            padding: 20px 0;
        }
        
        .sidebar .nav-link {
            color: #64748b;
            border-left: 3px solid transparent;
            padding: 12px 20px;
            transition: all 0.3s ease;
        }
        
        .sidebar .nav-link:hover {
            background-color: #f1f5f9;
            color: #667eea;
            border-left-color: #667eea;
        }
        
        .sidebar .nav-link.active {
            background-color: #ede9fe;
            color: #667eea;
            border-left-color: #667eea;
        }
        
        .main-content {
            margin-left: 250px;
            margin-top: 70px;
            padding: 30px;
        }
        
        .page-header {
            margin-bottom: 30px;
        }
        
        .page-header h1 {
            font-size: 2rem;
            color: #0f172a;
            font-weight: 700;
        }
        
        .stat-card {
            background: white;
            padding: 25px;
            border-radius: 10px;
            box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
            transition: all 0.3s ease;
            border-top: 4px solid #667eea;
        }
        
        .stat-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 25px rgba(0, 0, 0, 0.15);
        }
        
        .stat-icon {
            font-size: 2.5rem;
            margin-bottom: 15px;
            color: #667eea;
        }
        
        .stat-value {
            font-size: 2rem;
            font-weight: 700;
            color: #0f172a;
        }
        
        .stat-label {
            color: #64748b;
            font-size: 0.9rem;
        }
        
        .card {
            border: none;
            box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
            border-radius: 10px;
        }
        
        .card-header {
            background: white;
            border-bottom: 1px solid #e2e8f0;
            padding: 20px;
        }
        
        .btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border: none;
        }
        
        .btn-primary:hover {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
        }
        
        .alert {
            border: none;
            border-radius: 8px;
        }
        
        @media (max-width: 768px) {
            .sidebar {
                position: relative;
                width: 100%;
                top: 0;
                border-right: none;
                border-bottom: 1px solid #e2e8f0;
                min-height: auto;
            }
            
            .main-content {
                margin-left: 0;
                margin-top: 0;
            }
        }
    </style>
</head>
<body>
    <!-- Navigation Bar -->
    <nav class="navbar navbar-expand-lg navbar-light">
        <div class="container-fluid">
            <a class="navbar-brand" href="#"><i class="fas fa-tasks"></i> PMT</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse justify-content-end" id="navbarNav">
                <div class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="userDropdown" role="button" data-bs-toggle="dropdown">
                        <i class="fas fa-user-circle"></i> ${sessionScope.userName}
                    </a>
                    <ul class="dropdown-menu dropdown-menu-end">
                        <li><a class="dropdown-item" href="#">Settings</a></li>
                        <li><hr class="dropdown-divider"></li>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/logout">Logout</a></li>
                    </ul>
                </div>
            </div>
        </div>
    </nav>
    
    <!-- Sidebar -->
    <div class="sidebar">
        <nav class="nav flex-column">
            <a class="nav-link active" href="#">
                <i class="fas fa-home"></i> Dashboard
            </a>
            <a class="nav-link" href="?action=manage-users">
                <i class="fas fa-users"></i> Manage Users
            </a>
            <a class="nav-link" href="?action=view-analytics">
                <i class="fas fa-chart-bar"></i> Analytics
            </a>
            <a class="nav-link" href="#">
                <i class="fas fa-cog"></i> Settings
            </a>
        </nav>
    </div>
    
    <!-- Main Content -->
    <div class="main-content">
        <!-- Alert Messages -->
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <i class="fas fa-exclamation-circle"></i> ${errorMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>
        
        <c:if test="${not empty successMessage}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <i class="fas fa-check-circle"></i> ${successMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>
        
        <!-- Page Header -->
        <div class="page-header">
            <h1><i class="fas fa-dashboard"></i> Admin Dashboard</h1>
        </div>
        
        <!-- Statistics Cards -->
        <div class="row mb-4">
            <div class="col-md-3 mb-4">
                <div class="stat-card">
                    <div class="stat-icon"><i class="fas fa-users"></i></div>
                    <div class="stat-value">${requestScope.totalUsers != null ? requestScope.totalUsers : 0}</div>
                    <div class="stat-label">Total Users</div>
                </div>
            </div>
            <div class="col-md-3 mb-4">
                <div class="stat-card" style="border-top-color: #10b981;">
                    <div class="stat-icon" style="color: #10b981;"><i class="fas fa-user-tie"></i></div>
                    <div class="stat-value">${requestScope.adminCount != null ? requestScope.adminCount : 0}</div>
                    <div class="stat-label">Administrators</div>
                </div>
            </div>
            <div class="col-md-3 mb-4">
                <div class="stat-card" style="border-top-color: #f59e0b;">
                    <div class="stat-icon" style="color: #f59e0b;"><i class="fas fa-user-tie"></i></div>
                    <div class="stat-value">${requestScope.pmCount != null ? requestScope.pmCount : 0}</div>
                    <div class="stat-label">Project Managers</div>
                </div>
            </div>
            <div class="col-md-3 mb-4">
                <div class="stat-card" style="border-top-color: #ef4444;">
                    <div class="stat-icon" style="color: #ef4444;"><i class="fas fa-users-cog"></i></div>
                    <div class="stat-value">${requestScope.memberCount != null ? requestScope.memberCount : 0}</div>
                    <div class="stat-label">Team Members</div>
                </div>
            </div>
        </div>
        
        <!-- Quick Actions -->
        <div class="row">
            <div class="col-md-12">
                <div class="card">
                    <div class="card-header">
                        <h5 class="card-title mb-0"><i class="fas fa-bolt"></i> Quick Actions</h5>
                    </div>
                    <div class="card-body">
                        <a href="?action=manage-users" class="btn btn-primary me-2">
                            <i class="fas fa-plus"></i> Manage Users
                        </a>
                        <a href="#" class="btn btn-outline-primary me-2">
                            <i class="fas fa-chart-line"></i> View Reports
                        </a>
                        <a href="#" class="btn btn-outline-primary">
                            <i class="fas fa-cog"></i> System Settings
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
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
