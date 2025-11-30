<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Projects - Project Manager</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-5">
        <h2>Manage Projects</h2>
        <button class="btn btn-primary mb-3" data-bs-toggle="modal" data-bs-target="#addProjectModal">
            <i class="fas fa-plus"></i> Add New Project
        </button>

        <table class="table table-striped">
            <thead>
                <tr>
                    <th>Project Name</th>
                    <th>Description</th>
                    <th>Status</th>
                    <th>Start Date</th>
                    <th>End Date</th>
                    <th>Budget</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="project" items="${projects}">
                    <tr>
                        <td>${project.projectName}</td>
                        <td>${project.description}</td>
                        <td><span class="badge bg-success">${project.status.displayName}</span></td>
                        <td>${project.startDate}</td>
                        <td>${project.endDate}</td>
                        <td>$${project.budget}</td>
                        <td>
                            <button class="btn btn-sm btn-warning">Edit</button>
                            <button class="btn btn-sm btn-danger">Delete</button>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

    <!-- Add Project Modal -->
    <div class="modal fade" id="addProjectModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Add New Project</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <form method="POST" action="${pageContext.request.contextPath}/pm-dashboard">
                    <div class="modal-body">
                        <input type="hidden" name="action" value="create-project">
                        <div class="mb-3">
                            <label>Project Name</label>
                            <input type="text" class="form-control" name="projectName" required>
                        </div>
                        <div class="mb-3">
                            <label>Description</label>
                            <textarea class="form-control" name="description"></textarea>
                        </div>
                        <div class="mb-3">
                            <label>Start Date</label>
                            <input type="date" class="form-control" name="startDate" required>
                        </div>
                        <div class="mb-3">
                            <label>End Date</label>
                            <input type="date" class="form-control" name="endDate" required>
                        </div>
                        <div class="mb-3">
                            <label>Budget</label>
                            <input type="number" class="form-control" name="budget" step="0.01" required>
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

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
