package org.eu.yaesakura.simpleauth.framework.domain.dto;

import lombok.Data;

/**
 * 手机号注册表单
 *
 * @author YaeSakura
 */

@Data
public class PhoneRegisterDTO {
    // 手机号
    private String phone;
    // 密码
    private String password;
    // 验证码
    private String code;
}
