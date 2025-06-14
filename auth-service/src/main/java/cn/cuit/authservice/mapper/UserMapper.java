package cn.cuit.authservice.mapper;

import cn.cuit.authservice.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM user WHERE username = #{username}")
    User findByUsername(String username);

    @Insert("INSERT INTO user(username, password, email, phone, role) " +
            "VALUES(#{username}, #{password}, #{email}, #{phone}, #{role})")
    void save(User user);
}
