<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Users - Project Management Tool</title>
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
        .sidebar a:hover {
            background: rgba(255, 255, 255, 0.2);
        }
        .main-content {
            padding: 30px;
        }
        table {
            background: white;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }
    </style>
</head>
<body>
    <div class="container-fluid">
        <div class="row">
            <div class="col-md-2 sidebar">
                <h4>📊 Admin Panel</h4>
                <a href="${pageContext.request.contextPath}/admin-dashboard?action=view"><i class="fas fa-home"></i> Dashboard</a>
                <a href="${pageContext.request.contextPath}/admin-dashboard?action=manage-users"><i class="fas fa-users"></i> Manage Users</a>
                <a href="${pageContext.request.contextPath}/login?action=logout"><i class="fas fa-sign-out-alt"></i> Logout</a>
            </div>

            <div class="col-md-10 main-content">
                <h2>Manage Users</h2>

                <button class="btn btn-primary mb-3" data-bs-toggle="modal" data-bs-target="#addUserModal">
                    <i class="fas fa-plus"></i> Add New User
                </button>

                <table class="table table-striped table-hover">
                    <thead class="table-dark">
                        <tr>
                            <th>ID</th>
                            <th>Name</th>
                            <th>Email</th>
                            <th>Role</th>
                            <th>Department</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="user" items="${users}">
                            <tr>
                                <td>${user.userId}</td>
                                <td>${user.name}</td>
                                <td>${user.email}</td>
                                <td><span class="badge bg-info">${user.role.displayName}</span></td>
                                <td>${user.department}</td>
                                <td>${user.isActive ? 'Active' : 'Inactive'}</td>
                                <td>
                                    <button class="btn btn-sm btn-warning">Edit</button>
                                    <form method="POST" action="${pageContext.request.contextPath}/admin-dashboard" style="display: inline;">
                                        <input type="hidden" name="action" value="delete-user">
                                        <input type="hidden" name="userId" value="${user.userId}">
                                        <button type="submit" class="btn btn-sm btn-danger" onclick="return confirm('Are you sure?')">Delete</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <!-- Add User Modal -->
    <div class="modal fade" id="addUserModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Add New User</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <form method="POST" action="${pageContext.request.contextPath}/admin-dashboard">
                    <div class="modal-body">
                        <input type="hidden" name="action" value="create-user">
                        <div class="mb-3">
                            <label>Name</label>
                            <input type="text" class="form-control" name="name" required>
                        </div>
                        <div class="mb-3">
                            <label>Email</label>
                            <input type="email" class="form-control" name="email" required>
                        </div>
                        <div class="mb-3">
                            <label>Password</label>
                            <input type="password" class="form-control" name="password" required>
                        </div>
                        <div class="mb-3">
                            <label>Role</label>
                            <select class="form-control" name="role" required>
                                <option value="ADMIN">Admin</option>
                                <option value="PROJECT_MANAGER">Project Manager</option>
                                <option value="TEAM_MEMBER">Team Member</option>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label>Department</label>
                            <input type="text" class="form-control" name="department">
                        </div>
                        <div class="mb-3">
                            <label>Phone</label>
                            <input type="tel" class="form-control" name="phone">
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="submit" class="btn btn-primary">Add User</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
