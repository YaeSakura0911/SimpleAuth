package org.eu.yaesakura.simpleauth.framework.service.impl;

import org.eu.yaesakura.simpleauth.framework.service.AuthenticationService;
import org.eu.yaesakura.simpleauth.framework.util.CodeUtil;
import org.eu.yaesakura.simpleauth.framework.util.SMSUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * 认证服务实现类
 * @author YaeSakura
 */

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Value("${simple-auth.config.sms-code-expire}")
    private Long expire;

    private final SMSUtil smsUtil;
    private final CodeUtil codeUtil;
    private final StringRedisTemplate redisTemplate;

    @Autowired
    public AuthenticationServiceImpl(SMSUtil smsUtil, CodeUtil codeUtil, StringRedisTemplate redisTemplate) {
        this.smsUtil = smsUtil;
        this.codeUtil = codeUtil;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 发送短信验证码
     *
     * @param phone 手机号
     */
    @Override
    public void sendSmsCode(String phone) {

        if (phone.isEmpty()) {
            throw new RuntimeException("手机号不能为空！");
        }

        String code = codeUtil.generateCode();

        // 将验证码存入Redis
        redisTemplate.opsForValue().set(phone, code, expire, TimeUnit.SECONDS);
    }
}
