package cuit.cn.interviewService.dto;

public class InterviewResponse<T> {
    private int code;
    private String message;
    private T data;

    // 构造函数
    public InterviewResponse() {
    }

    public InterviewResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // 静态工厂方法
    public static <T> InterviewResponse<T> success(T data) {
        return new InterviewResponse<>(200, "操作成功", data);
    }

    public static <T> InterviewResponse<T> success(String message, T data) {
        return new InterviewResponse<>(200, message, data);
    }

    public static <T> InterviewResponse<T> fail(String message) {
        return new InterviewResponse<>(500, message, null);
    }

    public static <T> InterviewResponse<T> fail(int code, String message) {
        return new InterviewResponse<>(code, message, null);
    }

    // Getters and Setters
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
} 