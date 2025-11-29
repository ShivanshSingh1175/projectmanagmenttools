/* Project Management Tool - JavaScript Functions */

/**
 * Format date in readable format
 */
function formatDate(dateString) {
    const options = { year: 'numeric', month: 'long', day: 'numeric' };
    return new Date(dateString).toLocaleDateString('en-US', options);
}

/**
 * Show success message
 */
function showSuccess(message) {
    const alert = document.createElement('div');
    alert.className = 'alert alert-success alert-dismissible fade show';
    alert.innerHTML = `
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;
    document.body.insertBefore(alert, document.body.firstChild);
}

/**
 * Show error message
 */
function showError(message) {
    const alert = document.createElement('div');
    alert.className = 'alert alert-danger alert-dismissible fade show';
    alert.innerHTML = `
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;
    document.body.insertBefore(alert, document.body.firstChild);
}

/**
 * Get status badge HTML
 */
function getStatusBadge(status) {
    const statuses = {
        'TODO': 'secondary',
        'IN_PROGRESS': 'warning',
        'IN_REVIEW': 'info',
        'COMPLETED': 'success',
        'BLOCKED': 'danger'
    };
    const color = statuses[status] || 'secondary';
    return `<span class="badge bg-${color}">${status}</span>`;
}

/**
 * Get priority badge HTML
 */
function getPriorityBadge(priority) {
    const priorities = {
        'LOW': 'info',
        'MEDIUM': 'warning',
        'HIGH': 'danger',
        'CRITICAL': 'danger'
    };
    const color = priorities[priority] || 'secondary';
    return `<span class="badge bg-${color}">${priority}</span>`;
}

/**
 * Validate email format
 */
function isValidEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

/**
 * Validate password strength
 */
function isValidPassword(password) {
    return password.length >= 8 &&
           /[A-Z]/.test(password) &&
           /[a-z]/.test(password) &&
           /[0-9]/.test(password) &&
           /[@$!%*?&]/.test(password);
}

/**
 * Confirm action
 */
function confirmDelete(itemName) {
    return confirm(`Are you sure you want to delete "${itemName}"?`);
}

/**
 * Calculate days remaining
 */
function daysRemaining(dueDate) {
    const today = new Date();
    const due = new Date(dueDate);
    const diff = Math.floor((due - today) / (1000 * 60 * 60 * 24));
    return diff;
}

/**
 * Format percentage for display
 */
function formatPercentage(value) {
    return Math.round(value) + '%';
}

/**
 * Auto-dismiss alerts after 5 seconds
 */
document.addEventListener('DOMContentLoaded', function() {
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(alert => {
        setTimeout(() => {
            alert.remove();
        }, 5000);
    });
});
