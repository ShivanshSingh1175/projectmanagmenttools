<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>PM Dashboard - Project Management Tool</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
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
            min-height: calc(100vh - 60px);
            position: fixed;
            left: 0;
            top: 60px;
            width: 250px;
            overflow-y: auto;
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
            margin-top: 60px;
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
        
        .project-card {
            background: white;
            padding: 20px;
            border-radius: 10px;
            margin-bottom: 15px;
            border-left: 4px solid #667eea;
            transition: all 0.3s ease;
        }
        
        .project-card:hover {
            box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
            transform: translateY(-3px);
        }
        
        .progress {
            height: 6px;
            border-radius: 3px;
            background-color: #e2e8f0;
        }
    </style>
</head>
<body>
    <!-- Navigation Bar -->
    <nav class="navbar navbar-expand-lg navbar-light fixed-top">
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
            <a class="nav-link" href="?action=manage-projects">
                <i class="fas fa-project-diagram"></i> Projects
            </a>
            <a class="nav-link" href="?action=view-tasks">
                <i class="fas fa-tasks"></i> Tasks
            </a>
            <a class="nav-link" href="#">
                <i class="fas fa-team"></i> Team
            </a>
        </nav>
    </div>
    
    <!-- Main Content -->
    <div class="main-content">
        <!-- Page Header -->
        <div class="page-header">
            <h1><i class="fas fa-chart-line"></i> Project Manager Dashboard</h1>
        </div>
        
        <!-- Statistics Cards -->
        <div class="row mb-4">
            <div class="col-md-4 mb-4">
                <div class="stat-card">
                    <div class="stat-icon"><i class="fas fa-project-diagram"></i></div>
                    <div class="stat-value">${requestScope.activeProjectCount != null ? requestScope.activeProjectCount : 0}</div>
                    <div class="stat-label">Active Projects</div>
                </div>
            </div>
            <div class="col-md-4 mb-4">
                <div class="stat-card" style="border-top-color: #10b981;">
                    <div class="stat-icon" style="color: #10b981;"><i class="fas fa-tasks"></i></div>
                    <div class="stat-value">${requestScope.totalTasksCount != null ? requestScope.totalTasksCount : 0}</div>
                    <div class="stat-label">Total Tasks</div>
                </div>
            </div>
            <div class="col-md-4 mb-4">
                <div class="stat-card" style="border-top-color: #f59e0b;">
                    <div class="stat-icon" style="color: #f59e0b;"><i class="fas fa-users"></i></div>
                    <div class="stat-value">12</div>
                    <div class="stat-label">Team Members</div>
                </div>
            </div>
        </div>
        
        <!-- Quick Actions -->
        <div class="row mb-4">
            <div class="col-md-12">
                <div class="card">
                    <div class="card-header">
                        <h5 class="card-title mb-0"><i class="fas fa-bolt"></i> Quick Actions</h5>
                    </div>
                    <div class="card-body">
                        <a href="#" class="btn btn-primary me-2" data-bs-toggle="modal" data-bs-target="#projectModal">
                            <i class="fas fa-plus"></i> New Project
                        </a>
                        <a href="?action=manage-projects" class="btn btn-outline-primary me-2">
                            <i class="fas fa-folder-open"></i> View All Projects
                        </a>
                        <a href="?action=view-tasks" class="btn btn-outline-primary">
                            <i class="fas fa-list"></i> Task Management
                        </a>
                    </div>
                </div>
            </div>
        </div>
        
        <!-- Recent Projects -->
        <div class="row">
            <div class="col-md-12">
                <div class="card">
                    <div class="card-header">
                        <h5 class="card-title mb-0"><i class="fas fa-clock"></i> Recent Projects</h5>
                    </div>
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${not empty requestScope.projects}">
                                <c:forEach var="project" items="${requestScope.projects}">
                                    <div class="project-card">
                                        <div class="d-flex justify-content-between align-items-start">
                                            <div>
                                                <h5>${project.projectName}</h5>
                                                <p class="mb-2">${project.description}</p>
                                                <small class="text-muted"><i class="fas fa-calendar"></i> Due: ${project.endDate}</small>
                                            </div>
                                            <span class="badge bg-primary">In Progress</span>
                                        </div>
                                        <div class="mt-3">
                                            <small>Progress</small>
                                            <div class="progress">
                                                <div class="progress-bar" style="width: 65%"></div>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <div class="alert alert-info">
                                    <i class="fas fa-info-circle"></i> No projects yet. <a href="#" data-bs-toggle="modal" data-bs-target="#projectModal">Create your first project</a>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Project Modal -->
    <div class="modal fade" id="projectModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Create New Project</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <form method="POST" action="${pageContext.request.contextPath}/pm-dashboard">
                    <div class="modal-body">
                        <input type="hidden" name="action" value="create-project">
                        <div class="mb-3">
                            <label class="form-label">Project Name</label>
                            <input type="text" class="form-control" name="projectName" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Description</label>
                            <textarea class="form-control" name="description" rows="3"></textarea>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Start Date</label>
                            <input type="date" class="form-control" name="startDate" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">End Date</label>
                            <input type="date" class="form-control" name="endDate" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Budget</label>
                            <input type="number" class="form-control" name="budget" step="0.01">
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="submit" class="btn btn-primary">Create Project</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
