package org.eu.yaesakura.simpleauth.framework.controller;

import org.eu.yaesakura.simpleauth.framework.domain.dto.EmailRegisterDTO;
import org.eu.yaesakura.simpleauth.framework.domain.dto.PhoneRegisterDTO;
import org.eu.yaesakura.simpleauth.framework.domain.dto.UsernameRegisterDTO;
import org.eu.yaesakura.simpleauth.framework.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 账号控制层
 *
 * @author YaeSakura
 */

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * 用户名注册
     * @param dto 用户名注册表单
     */
    @PostMapping("/register/username")
    public void registerUsername(@RequestBody UsernameRegisterDTO dto) {
        authenticationService.registerUsernameUser(dto);
    }

    /**
     * 邮箱注册
     * @param dto 邮箱注册表单
     */
    @PostMapping("/register/email")
    public void registerEmail(@RequestBody EmailRegisterDTO dto) {
        authenticationService.registerEmailUser(dto);
    }

    /**
     * 手机号注册
     * @param dto 手机号注册表单
     */
    @PostMapping("/register/phone")
    public void registerPhoneUser(@RequestBody PhoneRegisterDTO dto) {
        authenticationService.registerPhoneUser(dto);
    }

    /**
     * 发送邮箱验证码
     */
    @PostMapping("/code/email")
    public void sendEmailCode(@RequestBody Map<String, String> dto) {
        authenticationService.sendEmailCode(dto.get("email"));
    }

    /**
     * 发送短信验证码
     */
    @PostMapping("/code/sms")
    public void sendSmsCode(@RequestBody Map<String, String> dto) {
        authenticationService.sendSmsCode(dto.get("phone"));
    }
}
