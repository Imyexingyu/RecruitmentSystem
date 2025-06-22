package cn.cuit.userservice.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;

@Data
public class UserEmployer {
    private Long id;
    private Long user_id;
    private String companyName;
    private String address;
    private String industry;
    private String introduction;
    private String website;
    private String logo;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date updatedAt;
}