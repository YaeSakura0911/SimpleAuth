package org.eu.yaesakura.simpleauth.framework.controller;

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
     * 发送短信验证码
     */
    @PostMapping("/smsCode")
    public void sendSmsCode(@RequestBody Map<String, String> dto) {
        authenticationService.sendSmsCode(dto.get("phone"));
    }
}
