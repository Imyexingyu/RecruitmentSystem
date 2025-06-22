package cn.cuit.jobservice.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class JobApplication {
    private Long id;
    private Long jobId;
    private Long candidateId;
    private String resume;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date applyTime;
    private String status;
}
