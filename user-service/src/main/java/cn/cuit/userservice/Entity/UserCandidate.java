package cn.cuit.userservice.Entity;

import lombok.Data;
import java.util.Date;

@Data
public class UserCandidate {
    private Long id;
    private Long userId;      // 关联 user 表主键
    private String name;
    private String gender;
    private Date birthday;
    private String education;
    private String experience;  // 工作/校园经历
    private String skills;      // 技能证书
    private String avatar;      // 头像路径
    private Date createdAt;
    private Date updatedAt;
}