package org.eu.yaesakura.simpleauth.framework.domain.entity;

import lombok.Data;

/**
 * 用户角色关系实体类
 *
 * @author YaeSakura
 */
@Data
public class UserRole {
    // 用户ID
    private Long userId;
    // 角色ID
    private Integer roleId;
}
