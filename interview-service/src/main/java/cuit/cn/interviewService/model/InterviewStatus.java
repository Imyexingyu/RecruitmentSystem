package cuit.cn.interviewService.model;

public enum InterviewStatus {
    SCHEDULED("已安排"),
    IN_PROGRESS("进行中"),
    COMPLETED("已完成"),
    CANCELLED("已取消"),
    NO_SHOW("未到场"),
    PENDING("待处理");

    private final String description;

    InterviewStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
} 