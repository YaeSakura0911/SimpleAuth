package org.eu.yaesakura.simpleauth.framework.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.eu.yaesakura.simpleauth.framework.domain.entity.User;

/**
 * 用户持久层
 *
 * @author YaeSakura
 */

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM user WHERE username = #{username}")
    User getUserByUsername(String username);

    @Select("SELECT * FROM user WHERE email = #{email}")
    User getUserByEmail(String email);

    @Select("SELECT * FROM user WHERE phone = #{phone}")
    User getUserByPhone(String phone);

    @Insert("INSERT INTO user VALUES (#{id}, #{username}, #{email}, #{phone}, #{password}, true)")
    void insertUser(User user);
}
