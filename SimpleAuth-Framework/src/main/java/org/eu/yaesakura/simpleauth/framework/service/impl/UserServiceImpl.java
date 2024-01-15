package org.eu.yaesakura.simpleauth.framework.service.impl;

import org.eu.yaesakura.simpleauth.framework.domain.entity.RolePermission;
import org.eu.yaesakura.simpleauth.framework.domain.entity.User;
import org.eu.yaesakura.simpleauth.framework.domain.entity.UserRole;
import org.eu.yaesakura.simpleauth.framework.mapper.PermissionMapper;
import org.eu.yaesakura.simpleauth.framework.mapper.RolePermissionMapper;
import org.eu.yaesakura.simpleauth.framework.mapper.UserMapper;
import org.eu.yaesakura.simpleauth.framework.mapper.UserRoleMapper;
import org.eu.yaesakura.simpleauth.framework.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 *
 * @author YaeSakura
 */

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRoleMapper userRoleMapper;
    private final RolePermissionMapper rolePermissionMapper;
    private final PermissionMapper permissionMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper,
                           UserRoleMapper userRoleMapper,
                           RolePermissionMapper rolePermissionMapper,
                           PermissionMapper permissionMapper) {
        this.userMapper = userMapper;
        this.userRoleMapper = userRoleMapper;
        this.rolePermissionMapper = rolePermissionMapper;
        this.permissionMapper = permissionMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user;
        String emailRegex = "(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF]+\\.)+[a-zA-Z\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF]{2,}))";
        String phoneRegex = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$";

        if (username.matches(emailRegex)) {
            user = userMapper.getUserByEmail(username);
            if (user == null) {
                throw new UsernameNotFoundException("邮箱不存在");
            }
        }
        else if (username.matches(phoneRegex)) {
            user = userMapper.getUserByPhone(username);
            if (user == null) {
                throw new UsernameNotFoundException("手机号不存在");
            }
        }
        else {
            // 根据用户名查询用户
            user = userMapper.getUserByUsername(username);
            if (user == null) {
                throw new UsernameNotFoundException("用户名不存在");
            }
        }

        // 根据用户ID查询用户角色
        List<UserRole> userRoleList = userRoleMapper.getUserRolesByUserId(user.getId());

        // 如果没有角色
        if (userRoleList.isEmpty()) {
            return user;
        }

        // 提取出角色ID
        List<Integer> roleIdList = userRoleList.stream().map(UserRole::getRoleId).toList();

        // 根据角色ID查询权限ID
        List<RolePermission> rolePermissionList = rolePermissionMapper.getRolePermissionsByRoleIds(roleIdList);

        // 如果没有权限
        if (rolePermissionList.isEmpty()) {
            return user;
        }

        // 提取出权限ID
        Set<Integer> permissionIdSet = rolePermissionList.stream().map(RolePermission::getPermissionId).collect(Collectors.toSet());

        // 根据权限ID查询权限
        user.setPermissionList(permissionMapper.getPermissionsByPermissionIds(permissionIdSet));

        return user;
    }
}
