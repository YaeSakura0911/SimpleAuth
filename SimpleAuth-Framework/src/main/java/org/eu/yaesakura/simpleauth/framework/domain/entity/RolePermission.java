package org.eu.yaesakura.simpleauth.framework.domain.entity;

import lombok.Data;

/**
 * 角色权限关系实体类
 *
 * @author YaeSakura
 */

@Data
public class RolePermission {
    // 角色ID
    private Integer roleId;
    // 权限ID
    private Integer permissionId;
}
