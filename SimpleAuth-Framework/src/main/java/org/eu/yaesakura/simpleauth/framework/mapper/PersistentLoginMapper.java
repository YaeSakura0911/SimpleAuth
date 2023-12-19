package org.eu.yaesakura.simpleauth.framework.mapper;

import org.apache.ibatis.annotations.*;
import org.eu.yaesakura.simpleauth.framework.domain.entity.PersistentLogin;

import java.time.LocalDateTime;

/**
 * 持续登录持久层
 *
 * @author YaeSakura
 */

@Mapper
public interface PersistentLoginMapper {

    @Select("SELECT * FROM persistent_logins where series = #{series}")
    PersistentLogin getTokenBySeries(String series);

    /**
     * 插入令牌
     * @param username 用户名
     * @param series
     * @param token 令牌
     * @param lastUsed 上次使用时间
     */
    @Insert("INSERT INTO persistent_logins (username, series, token, last_used) values(#{username},#{series},#{token},#{lastUsed})")
    void insertToken(String username, String series, String token, LocalDateTime lastUsed);

    /**
     * 更新令牌
     * @param token 令牌
     * @param lastUsed 上次使用时间
     * @param series
     */
    @Update("UPDATE persistent_logins SET token = #{token}, last_used = #{lastUsed} WHERE series = #{series}")
    void updateToken(String token, LocalDateTime lastUsed, String series);

    /**
     * 根据用户名删除令牌
     * @param username 用户名
     */
    @Delete("DELETE FROM persistent_logins WHERE username = #{username}")
    void deleteTokenByUsername(String username);
}
