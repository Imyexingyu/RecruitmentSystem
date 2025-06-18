package cn.cuit.userservice.Entity;

import lombok.Data;

@Data
public class UserProfile {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private String avatar;
    private String bio;
}
