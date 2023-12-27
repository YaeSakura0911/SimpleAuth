package org.eu.yaesakura.simpleauth.framework.service;

import org.eu.yaesakura.simpleauth.framework.domain.dto.EmailRegisterDTO;
import org.eu.yaesakura.simpleauth.framework.domain.dto.PhoneRegisterDTO;
import org.eu.yaesakura.simpleauth.framework.domain.dto.UsernameRegisterDTO;

/**
 * 认证服务接口
 *
 * @author YaeSakura
 */

public interface AuthenticationService {

    /**
     * 用户名注册
     * @param dto 用户名注册表单
     */
    void registerUsernameUser(UsernameRegisterDTO dto);

    /**
     * 邮箱注册
     * @param dto 邮箱注册表单
     */
    void registerEmailUser(EmailRegisterDTO dto);

    /**
     * 手机号注册
     * @param dto 手机号注册表单
     */
    void registerPhoneUser(PhoneRegisterDTO dto);

    /**
     * 发送邮箱验证码
     * @param email 邮箱
     */
    void sendEmailCode(String email);

    /**
     * 发送短信验证码
     * @param phone 手机号
     */
    void sendSmsCode(String phone);
}
