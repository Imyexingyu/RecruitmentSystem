DROP DATABASE IF EXISTS resume_db;
CREATE DATABASE resume_db CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE resume_db;

-- 个人信息表
CREATE TABLE personal_info (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               user_id INT NOT NULL,
                               name VARCHAR(50) NOT NULL,
                               gender VARCHAR(10),
                               birth_date DATE,
                               phone VARCHAR(20),
                               email VARCHAR(100),
                               address VARCHAR(200),
                               avatar_path VARCHAR(255),
                               self_introduction TEXT,
                               resume_file_path VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 教育背景表
CREATE TABLE education (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           personal_id BIGINT,
                           school_name VARCHAR(100) NOT NULL,
                           major VARCHAR(100),
                           degree VARCHAR(50),
                           start_date DATE,
                           end_date DATE,
                           description TEXT,
                           FOREIGN KEY (personal_id) REFERENCES personal_info(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 工作经历表
CREATE TABLE work_experience (
                                 id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 personal_id BIGINT,
                                 company_name VARCHAR(100) NOT NULL,
                                 position VARCHAR(100),
                                 start_date DATE,
                                 end_date DATE,
                                 description TEXT,
                                 FOREIGN KEY (personal_id) REFERENCES personal_info(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 校园经历表
CREATE TABLE campus_experience (
                                   id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                   personal_id BIGINT,
                                   activity_name VARCHAR(100) NOT NULL,
                                   role VARCHAR(100),
                                   start_date DATE,
                                   end_date DATE,
                                   description TEXT,
                                   FOREIGN KEY (personal_id) REFERENCES personal_info(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 技能证书表
CREATE TABLE skill_certificate (
                                   id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                   personal_id BIGINT,
                                   skill_name VARCHAR(100) NOT NULL,
                                   proficiency VARCHAR(50),
                                   certificate_name VARCHAR(100),
                                   FOREIGN KEY (personal_id) REFERENCES personal_info(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
