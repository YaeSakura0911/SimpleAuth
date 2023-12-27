package org.eu.yaesakura.simpleauth.framework.domain.dto;

import lombok.Data;

/**
 * 邮箱注册表单
 *
 * @author YaeSakura
 */

@Data
public class EmailRegisterDTO {
    // 邮箱
    private String email;
    // 密码
    private String password;
    // 验证码
    private String code;
}
