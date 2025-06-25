-- 面试表
CREATE TABLE IF NOT EXISTS interviews (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    job_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    scheduled_time DATETIME NOT NULL,
    interview_type VARCHAR(20) NOT NULL,
    location VARCHAR(255) NOT NULL,
    interviewer VARCHAR(100) NOT NULL,
    feedback TEXT,
    status VARCHAR(20) NOT NULL,
    created_by BIGINT NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    INDEX idx_job_id (job_id),
    INDEX idx_user_id (user_id),
    INDEX idx_scheduled_time (scheduled_time),
    INDEX idx_status (status)
); 