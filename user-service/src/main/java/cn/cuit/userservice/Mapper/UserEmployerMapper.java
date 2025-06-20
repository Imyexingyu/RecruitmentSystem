package cn.cuit.userservice.Mapper;

import cn.cuit.userservice.Entity.UserEmployer;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserEmployerMapper {

    @Select("SELECT * FROM user_employer WHERE user_id = #{user_id}")
    UserEmployer findByUserId(Long userId);

    @Insert("INSERT INTO user_employer(user_id, company_name, address, industry, introduction, website, logo) " +
            "VALUES(#{user_id}, #{companyName}, #{address}, #{industry}, #{introduction}, #{website}, #{logo})")
    int insert(UserEmployer employer);

    @Update("UPDATE user_employer SET companyName=#{companyName}, address=#{address}, industry=#{industry}, " +
            "introduction=#{introduction}, website=#{website}, logo=#{logo} WHERE user_id=#{user_id}")
    int update(UserEmployer employer);
}
