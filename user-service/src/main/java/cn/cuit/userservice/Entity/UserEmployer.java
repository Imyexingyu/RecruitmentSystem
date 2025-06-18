package cn.cuit.userservice.Entity;

import lombok.Data;
import java.util.Date;

@Data
public class UserEmployer {
    private Long id;
    private Long userId;       // 关联 user 表主键
    private String companyName;
    private String address;
    private String industry;
    private String introduction;
    private String website;
    private String logo;
    private Date createdAt;
    private Date updatedAt;
}