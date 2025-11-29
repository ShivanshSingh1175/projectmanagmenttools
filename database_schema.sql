-- Project Management Tool Database Schema
-- MySQL Script for creating all required tables

CREATE DATABASE IF NOT EXISTS project_management_tool;
USE project_management_tool;

-- 1. Users Table
CREATE TABLE IF NOT EXISTS users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'PROJECT_MANAGER', 'TEAM_MEMBER') NOT NULL DEFAULT 'TEAM_MEMBER',
    department VARCHAR(50),
    phone VARCHAR(15),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_role (role),
    INDEX idx_is_active (is_active)
);

-- 2. Projects Table
CREATE TABLE IF NOT EXISTS projects (
    project_id INT PRIMARY KEY AUTO_INCREMENT,
    project_name VARCHAR(150) NOT NULL,
    description TEXT,
    created_by INT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    status ENUM('ACTIVE', 'COMPLETED', 'ON_HOLD', 'CANCELLED') DEFAULT 'ACTIVE',
    budget DECIMAL(12, 2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES users(user_id) ON DELETE RESTRICT,
    INDEX idx_status (status),
    INDEX idx_created_by (created_by),
    INDEX idx_start_date (start_date),
    INDEX idx_end_date (end_date)
);

-- 3. Project Members (Many-to-Many relationship)
CREATE TABLE IF NOT EXISTS project_members (
    project_member_id INT PRIMARY KEY AUTO_INCREMENT,
    project_id INT NOT NULL,
    user_id INT NOT NULL,
    role VARCHAR(50), -- Can be 'Lead', 'Developer', 'QA', etc.
    joined_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (project_id) REFERENCES projects(project_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    UNIQUE KEY unique_project_member (project_id, user_id),
    INDEX idx_project_id (project_id),
    INDEX idx_user_id (user_id)
);

-- 4. Tasks Table
CREATE TABLE IF NOT EXISTS tasks (
    task_id INT PRIMARY KEY AUTO_INCREMENT,
    project_id INT NOT NULL,
    task_name VARCHAR(150) NOT NULL,
    description TEXT,
    assigned_to INT,
    assigned_by INT NOT NULL,
    priority ENUM('LOW', 'MEDIUM', 'HIGH', 'CRITICAL') DEFAULT 'MEDIUM',
    status ENUM('TODO', 'IN_PROGRESS', 'IN_REVIEW', 'COMPLETED', 'BLOCKED') DEFAULT 'TODO',
    start_date DATE,
    due_date DATE NOT NULL,
    estimated_hours INT,
    actual_hours INT,
    completion_percentage INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (project_id) REFERENCES projects(project_id) ON DELETE CASCADE,
    FOREIGN KEY (assigned_to) REFERENCES users(user_id) ON DELETE SET NULL,
    FOREIGN KEY (assigned_by) REFERENCES users(user_id) ON DELETE RESTRICT,
    INDEX idx_project_id (project_id),
    INDEX idx_assigned_to (assigned_to),
    INDEX idx_status (status),
    INDEX idx_priority (priority),
    INDEX idx_due_date (due_date)
);

-- 5. Activity Log Table
CREATE TABLE IF NOT EXISTS activity_log (
    log_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    activity_type VARCHAR(50) NOT NULL,
    entity_type VARCHAR(50), -- 'USER', 'PROJECT', 'TASK'
    entity_id INT,
    description TEXT,
    old_value VARCHAR(255),
    new_value VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_created_at (created_at),
    INDEX idx_entity_type (entity_type),
    INDEX idx_activity_type (activity_type)
);

-- 6. System Settings Table
CREATE TABLE IF NOT EXISTS system_settings (
    setting_id INT PRIMARY KEY AUTO_INCREMENT,
    setting_key VARCHAR(100) UNIQUE NOT NULL,
    setting_value VARCHAR(500),
    data_type VARCHAR(50), -- 'STRING', 'INT', 'BOOLEAN'
    description TEXT,
    updated_by INT,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (updated_by) REFERENCES users(user_id) ON DELETE SET NULL,
    INDEX idx_setting_key (setting_key)
);

-- 7. Notifications Table
CREATE TABLE IF NOT EXISTS notifications (
    notification_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    title VARCHAR(150),
    message TEXT,
    notification_type VARCHAR(50), -- 'TASK_ASSIGNED', 'TASK_UPDATED', 'PROJECT_UPDATE'
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_is_read (is_read),
    INDEX idx_created_at (created_at)
);

-- 8. Task Comments/Updates Table
CREATE TABLE IF NOT EXISTS task_updates (
    update_id INT PRIMARY KEY AUTO_INCREMENT,
    task_id INT NOT NULL,
    user_id INT NOT NULL,
    comment TEXT,
    hours_worked INT,
    update_type VARCHAR(50), -- 'COMMENT', 'STATUS_CHANGE', 'HOURS_UPDATE'
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (task_id) REFERENCES tasks(task_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_task_id (task_id),
    INDEX idx_user_id (user_id),
    INDEX idx_created_at (created_at)
);

-- Default System Settings
INSERT INTO system_settings (setting_key, setting_value, data_type, description) VALUES
('APP_NAME', 'Project Management Tool', 'STRING', 'Application Name'),
('APP_VERSION', '1.0.0', 'STRING', 'Application Version'),
('MAX_LOGIN_ATTEMPTS', '5', 'INT', 'Maximum login attempts before lockout'),
('SESSION_TIMEOUT', '1800', 'INT', 'Session timeout in seconds (30 minutes)'),
('EMAIL_NOTIFICATIONS_ENABLED', 'true', 'BOOLEAN', 'Enable email notifications'),
('MAX_FILE_SIZE', '10485760', 'INT', 'Maximum file upload size in bytes (10MB)');

-- Sample Admin User (Password: admin@123 - hashed)
INSERT INTO users (name, email, password, role, is_active) VALUES
('Administrator', 'admin@pmt.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36P4.zO6', 'ADMIN', TRUE);

-- Sample Project Manager User
INSERT INTO users (name, email, password, role, is_active) VALUES
('John Manager', 'john.manager@pmt.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36P4.zO6', 'PROJECT_MANAGER', TRUE);

-- Sample Team Members
INSERT INTO users (name, email, password, role, is_active) VALUES
('Alice Developer', 'alice@pmt.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36P4.zO6', 'TEAM_MEMBER', TRUE),
('Bob Tester', 'bob@pmt.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36P4.zO6', 'TEAM_MEMBER', TRUE),
('Carol Designer', 'carol@pmt.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36P4.zO6', 'TEAM_MEMBER', TRUE);

-- Sample Project
INSERT INTO projects (project_name, description, created_by, start_date, end_date, status, budget) VALUES
('Web Portal Redesign', 'Redesign the company web portal with modern UI/UX', 2, '2025-01-15', '2025-06-30', 'ACTIVE', 50000.00);

-- Add team members to project
INSERT INTO project_members (project_id, user_id, role) VALUES
(1, 2, 'Project Lead'),
(1, 3, 'Developer'),
(1, 4, 'QA'),
(1, 5, 'Designer');

-- Sample Tasks
INSERT INTO tasks (project_id, task_name, description, assigned_to, assigned_by, priority, status, due_date, estimated_hours) VALUES
(1, 'Design Homepage', 'Create mockups and design for homepage', 5, 2, 'HIGH', 'IN_PROGRESS', '2025-02-15', 40),
(1, 'Setup Database', 'Configure and setup project database', 3, 2, 'HIGH', 'COMPLETED', '2025-02-10', 20),
(1, 'Backend API Development', 'Develop RESTful APIs for user management', 3, 2, 'HIGH', 'IN_PROGRESS', '2025-03-15', 60),
(1, 'QA Testing', 'Perform testing on completed features', 4, 2, 'MEDIUM', 'TODO', '2025-03-20', 30);

-- Grant privileges (create user if needed)
CREATE USER IF NOT EXISTS 'pmt_user'@'localhost' IDENTIFIED BY 'pmt_password_123';
GRANT ALL PRIVILEGES ON project_management_tool.* TO 'pmt_user'@'localhost';
FLUSH PRIVILEGES;
