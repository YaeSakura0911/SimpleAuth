package org.eu.yaesakura.simpleauth.framework.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.eu.yaesakura.simpleauth.framework.domain.entity.Permission;

import java.util.List;
import java.util.Set;

/**
 * 权限持久层
 *
 * @author YaeSakura
 */

@Mapper
public interface PermissionMapper {
    /**
     * 根据权限ID集合查询权限
     * @param permissionIdSet 权限ID集合
     * @return 包含权限的列表
     */
    List<Permission> getPermissionsByPermissionIds(Set<Integer> permissionIdSet);
}
