- 登录获取JWT令牌:
   ```shell
   curl -X POST -H "Content-Type: application/json" -d '{"username":"admin","password":"123456"}' http://localhost:8701/auth/login
   ```

- 创建面试（验证职位和用户）:
  ```shell
  curl -H "Authorization: Bearer {token}" -X POST -H "Content-Type: application/json" -d '{"jobId": 7, "userId": 7, "scheduledTime": "2025-07-10T10:00:00", "location": "北京市海淀区", "interviewType": "ONLINE", "interviewer": "HR张三", "status": "SCHEDULED"}' "http://localhost:8084/interviews"
  ```

- 获取所有面试:
  ```shell
  curl -H "Authorization: Bearer {token}" "http://localhost:8084/interviews/all"
  ```

- 按职位ID获取面试列表:
  ```shell
  curl -H "Authorization: Bearer {token}" "http://localhost:8084/interviews/job/7"
  ```

- 按用户ID获取面试列表:
  ```shell
  curl -H "Authorization: Bearer {token}" "http://localhost:8084/interviews/user/7"
  ```

- 更新面试状态:
  ```shell
  curl -H "Authorization: Bearer {token}" -X PUT "http://localhost:8084/interviews/6/status?status=IN_PROGRESS"
  ```

- 添加面试反馈:
  ```shell
  curl -H "Authorization: Bearer {token}" -X PUT "http://localhost:8084/interviews/6/feedback?feedback=%E9%9D%A2%E8%AF%95%E8%A1%A8%E7%8E%B0%E8%89%AF%E5%A5%BD%EF%BC%8C%E6%8A%80%E6%9C%AF%E8%83%BD%E5%8A%9B%E4%BC%98%E7%A7%80%EF%BC%8C%E6%B2%9F%E9%80%9A%E8%83%BD%E5%8A%9B%E5%BC%BA%E3%80%82"
  ```

- 查看未读通知:
  ```shell
  curl -H "Authorization: Bearer {token}" "http://localhost:8085/api/notifications/user/7/unread"
  ```

- 标记通知为已读:
  ```shell
  curl -H "Authorization: Bearer {token}" -X PUT "http://localhost:8085/api/notifications/9/read"
  ```

- 标记所有通知为已读:
  ```shell
  curl -H "Authorization: Bearer {token}" -X PUT "http://localhost:8085/api/notifications/user/7/read-all"
  ```

- 获取所有通知（已读和未读）:
  ```shell
  curl -H "Authorization: Bearer {token}" "http://localhost:8085/api/notifications/user/7?page=0&size=10"
  ```

## 测试结论

经过全面测试，interview-service和notify-service的所有功能都正常工作。两个服务之间的消息队列集成也已成功修复，面试创建、状态更新和反馈添加都能正确触发通知，并且通知可以被正确地标记为已读。

系统安全性方面，JWT令牌验证和授权控制也正常工作，未授权访问会被正确拒绝。错误处理机制也正常工作，对于不存在的用户ID或职位ID，系统会返回适当的错误消息。

总体而言，interview-service和notify-service已经完全满足需求，可以正式部署使用。