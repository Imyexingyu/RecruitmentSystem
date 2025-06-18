package cn.cuit.userservice.Mapper;


import cn.cuit.userservice.Entity.UserProfile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserProfileMapper {
    @Select("SELECT * FROM user_profile WHERE username = #{username}")
    UserProfile findByUsername(String username);
}

