package cn.cuit.resumeservice.Mapper;

import cn.cuit.resumeservice.Entity.CampusExperience;
import cn.cuit.resumeservice.Entity.SkillCertificate;
import cn.cuit.resumeservice.Entity.*;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface PersonalInfoMapper {

    @Insert("INSERT INTO personal_info (user_id, name, gender, birth_date, phone, email, address, avatar_path, self_introduction, resume_file_path) " +
            "VALUES (#{userId}, #{name}, #{gender}, #{birthDate}, #{phone}, #{email}, #{address}, #{avatarPath}, #{selfIntroduction}, #{resumeFilePath})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertPersonalInfo(PersonalInfo personalInfo);

    @Update("UPDATE personal_info SET user_id = #{userId}, name = #{name}, gender = #{gender}, birth_date = #{birthDate}, " +
            "phone = #{phone}, email = #{email}, address = #{address}, " +
            "self_introduction = #{selfIntroduction} WHERE id = #{id}")
    void updatePersonalInfo(PersonalInfo personalInfo);

    @Select("SELECT * FROM personal_info WHERE id = #{id}")
    @Results(id = "PersonalInfoResultMap", value = {
            @Result(property = "id", column = "id", id = true),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "name", column = "name"),
            @Result(property = "gender", column = "gender"),
            @Result(property = "birthDate", column = "birth_date"),
            @Result(property = "phone", column = "phone"),
            @Result(property = "email", column = "email"),
            @Result(property = "address", column = "address"),
            @Result(property = "avatarPath", column = "avatar_path"),
            @Result(property = "selfIntroduction", column = "self_introduction"),
            @Result(property = "resumeFilePath", column = "resume_file_path"),
            @Result(property = "educations", column = "id", many = @Many(
                    select = "selectEducationsByPersonalId",
                    fetchType = FetchType.EAGER)),
            @Result(property = "workExperiences", column = "id", many = @Many(
                    select = "selectWorkExperiencesByPersonalId",
                    fetchType = FetchType.EAGER)),
            @Result(property = "campusExperiences", column = "id", many = @Many(
                    select = "selectCampusExperiencesByPersonalId",
                    fetchType = FetchType.EAGER)),
            @Result(property = "skillCertificates", column = "id", many = @Many(
                    select = "selectSkillCertificatesByPersonalId",
                    fetchType = FetchType.EAGER))
    })
    PersonalInfo findById(Long id);

    @Delete("DELETE FROM personal_info WHERE id = #{id}")
    void deletePersonalInfo(Long id);

    @Insert("INSERT INTO education (personal_id, school_name, major, degree, start_date, end_date, description) " +
            "VALUES (#{personalInfo.id}, #{schoolName}, #{major}, #{degree}, #{startDate}, #{endDate}, #{description})")
    void insertEducation(Education education);

    @Delete("DELETE FROM education WHERE personal_id = #{personalId}")
    void deleteEducationsByPersonalId(Long personalId);

    @Select("SELECT id, personal_id, school_name, major, degree, start_date, end_date, description " +
            "FROM education WHERE personal_id = #{personalId}")
    List<Education> selectEducationsByPersonalId(Long personalId);

    @Insert("INSERT INTO work_experience (personal_id, company_name, position, start_date, end_date, description) " +
            "VALUES (#{personalInfo.id}, #{companyName}, #{position}, #{startDate}, #{endDate}, #{description})")
    void insertWorkExperience(WorkExperience workExperience);

    @Delete("DELETE FROM work_experience WHERE personal_id = #{personalId}")
    void deleteWorkExperiencesByPersonalId(Long personalId);

    @Select("SELECT id, personal_id, company_name, position, start_date, end_date, description " +
            "FROM work_experience WHERE personal_id = #{personalId}")
    List<WorkExperience> selectWorkExperiencesByPersonalId(Long personalId);

    @Insert("INSERT INTO campus_experience (personal_id, activity_name, role, start_date, end_date, description) " +
            "VALUES (#{personalInfo.id}, #{activityName}, #{role}, #{startDate}, #{endDate}, #{description})")
    void insertCampusExperience(CampusExperience campusExperience);

    @Delete("DELETE FROM campus_experience WHERE personal_id = #{personalId}")
    void deleteCampusExperiencesByPersonalId(Long personalId);

    @Select("SELECT id, personal_id, activity_name, role, start_date, end_date, description " +
            "FROM campus_experience WHERE personal_id = #{personalId}")
    List<CampusExperience> selectCampusExperiencesByPersonalId(Long personalId);

    @Insert("INSERT INTO skill_certificate (personal_id, skill_name, proficiency, certificate_name) " +
            "VALUES (#{personalInfo.id}, #{skillName}, #{proficiency}, #{certificateName})")
    void insertSkillCertificate(SkillCertificate skillCertificate);

    @Delete("DELETE FROM skill_certificate WHERE personal_id = #{personalId}")
    void deleteSkillCertificatesByPersonalId(Long personalId);

    @Select("SELECT id, personal_id, skill_name, proficiency, certificate_name " +
            "FROM skill_certificate WHERE personal_id = #{personalId}")
    List<SkillCertificate> selectSkillCertificatesByPersonalId(Long personalId);

    @Select("SELECT user_id FROM personal_info WHERE id = #{resumeId}")
    Integer getUserIdByResumeId(Long resumeId);

    @Select("SELECT * FROM personal_info WHERE user_id = #{userId} LIMIT 1")
    PersonalInfo selectByUserId(@Param("userId") Integer userId);

    @Select("SELECT * FROM personal_info WHERE user_id = #{userId} LIMIT 1")
    PersonalInfo findByUserId(@Param("userId") Integer userId);

    @Select("SELECT * FROM education WHERE personal_id = #{personalId}")
    List<Education> findEducationsByPersonalId(@Param("personalId") Long personalId);

    @Select("SELECT * FROM work_experience WHERE personal_id = #{personalId}")
    List<WorkExperience> findWorkExperiencesByPersonalId(@Param("personalId") Long personalId);

    @Select("SELECT * FROM campus_experience WHERE personal_id = #{personalId}")
    List<CampusExperience> findCampusExperiencesByPersonalId(@Param("personalId") Long personalId);

    @Select("SELECT * FROM skill_certificate WHERE personal_id = #{personalId}")
    List<SkillCertificate> findSkillCertificatesByPersonalId(@Param("personalId") Long personalId);


}