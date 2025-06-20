package cn.cuit.userservice.Mapper;

import cn.cuit.userservice.Entity.UserCandidate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserCandidateMapper {

    @Select("SELECT * FROM user_candidate WHERE user_id = #{user_id}")
    UserCandidate findByUserId(Long userId);

    @Insert("INSERT INTO user_candidate(user_id, name, gender, birthday, education, experience, skills, avatar) " +
            "VALUES(#{user_id}, #{name}, #{gender}, #{birthday}, #{education}, #{experience}, #{skills}, #{avatar})")
    int insert(UserCandidate candidate);

    @Update("UPDATE user_candidate SET name=#{name}, gender=#{gender}, birthday=#{birthday}, education=#{education}, " +
            "experience=#{experience}, skills=#{skills}, avatar=#{avatar} WHERE user_id=#{user_id}")
    int update(UserCandidate candidate);
}
