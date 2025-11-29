<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Tasks - Team Member</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .main-content {
            padding: 30px;
        }
        .task-item {
            background: white;
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 15px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            border-left: 4px solid #667eea;
        }
    </style>
</head>
<body>
    <div class="container-fluid">
        <div class="main-content">
            <h2>My Tasks</h2>
            
            <div class="row">
                <c:forEach var="task" items="${tasks}">
                    <div class="col-md-6">
                        <div class="task-item">
                            <h5>${task.taskName}</h5>
                            <p class="text-muted">${task.description}</p>
                            <div class="mb-3">
                                <span class="badge bg-info">${task.priority.displayName} Priority</span>
                                <span class="badge bg-success">${task.status.displayName}</span>
                            </div>
                            <div class="row">
                                <div class="col-6">
                                    <small><strong>Due Date:</strong> ${task.dueDate}</small>
                                </div>
                                <div class="col-6">
                                    <small><strong>Progress:</strong> ${task.completionPercentage}%</small>
                                </div>
                            </div>
                            <div class="progress mt-2">
                                <div class="progress-bar" style="width: <c:out value='${task.completionPercentage}' />%"></div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
