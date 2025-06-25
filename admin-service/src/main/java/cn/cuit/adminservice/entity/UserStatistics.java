package cn.cuit.adminservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_statistics")
public class UserStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer totalUsers;

    private Integer candidateUsers;

    private Integer employerUsers;

    private Integer blockedUsers;

    private LocalDateTime lastUpdated;
}