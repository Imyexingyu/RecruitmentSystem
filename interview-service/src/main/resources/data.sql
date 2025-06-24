-- 添加面试测试数据
INSERT INTO interviews (job_id, user_id, scheduled_time, interview_type, location, interviewer, status, created_by, created_at, updated_at)
VALUES 
(1, 1, '2025-07-15 10:00:00', 'ONLINE', '线上会议室', '张经理', 'SCHEDULED', 1, NOW(), NOW()),
(2, 2, '2025-07-16 14:30:00', 'OFFLINE', '公司总部5楼会议室', '李总监', 'SCHEDULED', 1, NOW(), NOW()),
(3, 3, '2025-07-17 11:00:00', 'ONLINE', '线上会议室', '王主管', 'PENDING', 1, NOW(), NOW()),
(1, 4, '2025-07-18 15:00:00', 'ONLINE', '线上会议室', '赵经理', 'COMPLETED', 1, NOW(), NOW()),
(2, 5, '2025-07-19 09:30:00', 'OFFLINE', '公司分部3楼会议室', '钱总监', 'CANCELLED', 1, NOW(), NOW()); 