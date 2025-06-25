package cn.cuit.authservice.entity;

import lombok.Data;

@Data
public class User {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String role;
    private String status;
}

