package org.eu.yaesakura.simpleauth.framework.controller;

import org.eu.yaesakura.simpleauth.framework.domain.entity.User;
import org.eu.yaesakura.simpleauth.framework.domain.vo.GetPermissionBySessionVo;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author YaeSakura
 */

@RestController
@RequestMapping("/permission")
public class PermissionController {

    /**
     * 根据Session获取权限信息
     * @param authentication 认证信息
     * @return
     */
    @GetMapping
    public GetPermissionBySessionVo getPermissionBySession(Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        GetPermissionBySessionVo getPermissionBySessionVo = new GetPermissionBySessionVo();
        getPermissionBySessionVo.setPermissions(principal.getPermissionList());
        return getPermissionBySessionVo;
    }
}
