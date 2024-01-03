package org.eu.yaesakura.simpleauth.framework.controller;

import org.eu.yaesakura.simpleauth.framework.domain.entity.Permission;
import org.eu.yaesakura.simpleauth.framework.domain.entity.User;
import org.eu.yaesakura.simpleauth.framework.domain.vo.GetUserBySessionVo;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户控制层
 * @author YaeSakura
 */
@RestController
@RequestMapping("/user")
public class UserController {

    /**
     * 根据 Session 获取用户
     * @param authentication 认证信息
     */
    @GetMapping
    public GetUserBySessionVo getUserBySession(Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        GetUserBySessionVo getUserBySessionVo = new GetUserBySessionVo();
        getUserBySessionVo.setName(principal.getUsername());
        if (principal.getPermissionList() != null && !principal.getPermissionList().isEmpty()) {
            getUserBySessionVo.setPermissions(principal.getPermissionList().stream().map(Permission::getCode).toList());
        }

        return getUserBySessionVo;
    }
}
