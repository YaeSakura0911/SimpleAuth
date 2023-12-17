package org.eu.yaesakura.simpleauth.framework.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.eu.yaesakura.simpleauth.framework.domain.entity.UserRole;

import java.util.List;

/**
 * 用户角色关系持久层
 *
 * @author YaeSakura
 */

@Mapper
public interface UserRoleMapper {

    /**
     * 根据用户ID查询用户角色
     * @param userId 用户ID
     * @return 包含用户角色的列表
     */
    @Select("SELECT * FROM user_role WHERE user_id = #{userId}")
    List<UserRole> getUserRolesByUserId(Long userId);
}
