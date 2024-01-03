package org.eu.yaesakura.simpleauth.framework.domain.dto;

import lombok.Data;

/**
 * 验证码登录表单
 *
 * @author YaeSakura
 */

@Data
public class CodeLoginDTO {
    // 邮箱或手机号
    private String emailOrPhone;
    // 验证码
    private String code;
    // 记住我
    private Boolean remember;
}
