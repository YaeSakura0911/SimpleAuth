package org.eu.yaesakura.simpleauth.framework.domain.dto;

import lombok.Data;

/**
 * 用户名注册表单
 *
 * @author YaeSakura
 */

@Data
public class UsernameRegisterDTO {
    // 用户名
    private String username;
    // 密码
    private String password;
}
