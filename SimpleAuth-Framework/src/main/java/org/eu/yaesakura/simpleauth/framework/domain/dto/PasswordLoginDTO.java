package org.eu.yaesakura.simpleauth.framework.domain.dto;

import lombok.Data;

/**
 * 密码登录表单
 *
 * @author YaeSakura
 */

@Data
public class PasswordLoginDTO {
    // 账号
    private String usernameOrEmailOrPhone;
    // 密码
    private String password;
    // 记住我
    private Boolean remember;
}
