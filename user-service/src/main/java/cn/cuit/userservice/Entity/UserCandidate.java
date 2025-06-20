package cn.cuit.userservice.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;

@Data
public class UserCandidate {
    private Long id;
    private Long user_id;
    private String name;
    private String gender;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date birthday;
    private String education;
    private String experience;  // 工作/校园经历
    private String skills;      // 技能证书
    private String avatar;      // 头像路径
    private Date createdAt;
    private Date updatedAt;
}