package org.eu.yaesakura.simpleauth.framework.service;

/**
 * 认证服务接口
 *
 * @author YaeSakura
 */

public interface AuthenticationService {

    /**
     * 发送短信验证码
     * @param phone 手机号
     */
    void sendSmsCode(String phone);
}
