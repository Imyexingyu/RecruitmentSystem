package cn.cuit.authservice.mapper;

import cn.cuit.authservice.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM user WHERE username = #{username}")
    User findByUsername(String username);

    @Insert("INSERT INTO user(username, password, email, phone, role) " +
            "VALUES(#{username}, #{password}, #{email}, #{phone}, #{role})")
    void save(User user);

    @Select("SELECT COUNT(*) FROM user")
    int countAll();

    @Select("SELECT COUNT(*) FROM user WHERE role = #{role}")
    int countByRole(@Param("role") String role);

    @Select("SELECT COUNT(*) FROM user WHERE status = #{status}")
    int countByStatus(@Param("status") String status);

    @Update("UPDATE user SET status = #{status} WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") String status);

    @Delete("DELETE FROM user WHERE id = #{id}")
    int deleteById(@Param("id") Long id);

    @Select("SELECT * FROM user WHERE id = #{id}")
    User findById(@Param("id") Long id);

    @Select("SELECT * FROM user")
    List<User> findAll();

}
