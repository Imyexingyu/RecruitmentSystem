package cuit.cn.notifyService.dto;

public class NotificationResponse<T> {
    private int code;
    private String message;
    private T data;

    // 构造函数
    public NotificationResponse() {
    }

    public NotificationResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // 静态工厂方法
    public static <T> NotificationResponse<T> success(T data) {
        return new NotificationResponse<>(200, "操作成功", data);
    }

    public static <T> NotificationResponse<T> success(String message, T data) {
        return new NotificationResponse<>(200, message, data);
    }

    public static <T> NotificationResponse<T> fail(String message) {
        return new NotificationResponse<>(500, message, null);
    }

    public static <T> NotificationResponse<T> fail(int code, String message) {
        return new NotificationResponse<>(code, message, null);
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