package cn.cuit.jobservice.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class JobPost {
    private Long id;
    private Long employerId;
    private String title;
    private String description;
    private String location;
    private String salary;
    private String requirements;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createdTime;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date updatedTime;
    private String status;
}

