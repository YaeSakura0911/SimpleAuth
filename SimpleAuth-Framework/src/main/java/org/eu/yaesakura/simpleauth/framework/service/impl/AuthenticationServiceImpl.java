package org.eu.yaesakura.simpleauth.framework.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import org.eu.yaesakura.simpleauth.framework.domain.dto.EmailRegisterDTO;
import org.eu.yaesakura.simpleauth.framework.domain.dto.PhoneRegisterDTO;
import org.eu.yaesakura.simpleauth.framework.domain.dto.UsernameRegisterDTO;
import org.eu.yaesakura.simpleauth.framework.domain.entity.User;
import org.eu.yaesakura.simpleauth.framework.mapper.UserMapper;
import org.eu.yaesakura.simpleauth.framework.service.AuthenticationService;
import org.eu.yaesakura.simpleauth.framework.util.CodeUtil;
import org.eu.yaesakura.simpleauth.framework.util.EmailUtil;
import org.eu.yaesakura.simpleauth.framework.util.SMSUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 认证服务实现类
 *
 * @author YaeSakura
 */

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Value("${simple-auth.config.email-code-expire}")
    private Integer emailCodeExpire;

    private final UserMapper userMapper;
    private final SMSUtil smsUtil;
    private final EmailUtil emailUtil;
    private final CodeUtil codeUtil;
    private final StringRedisTemplate redisTemplate;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationServiceImpl(
            UserMapper userMapper,
            SMSUtil smsUtil,
            EmailUtil emailUtil,
            CodeUtil codeUtil,
            StringRedisTemplate redisTemplate,
            PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.smsUtil = smsUtil;
        this.emailUtil = emailUtil;
        this.codeUtil = codeUtil;
        this.redisTemplate = redisTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 用户名注册
     *
     * @param dto 用户名注册表单
     */
    @Override
    public void registerUsernameUser(UsernameRegisterDTO dto) {
        User user = new User();
        BeanUtils.copyProperties(dto, user);

        // 利用雪花算法生成ID
        user.setId(IdWorker.getId());
        // 利用SpringSecurity加密密码
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        userMapper.insertUser(user);
    }

    /**
     * 邮箱注册
     *
     * @param dto 邮箱注册表单
     */
    @Override
    public void registerEmailUser(EmailRegisterDTO dto) {
        String code = redisTemplate.opsForValue().get(dto.getEmail());

        if (!Objects.equals(dto.getCode(), code)) {
            throw new RuntimeException("验证码错误！");
        }

        User user = new User();
        BeanUtils.copyProperties(dto, user);

        // 利用雪花算法生成ID
        user.setId(IdWorker.getId());
        // 利用SpringSecurity加密密码
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        userMapper.insertUser(user);

        // 删除缓存中的验证码
        redisTemplate.delete(dto.getEmail());
    }

    /**
     * 手机号注册
     *
     * @param dto 手机号注册表单
     */
    @Override
    public void registerPhoneUser(PhoneRegisterDTO dto) {
        // 校验验证码
        String code = redisTemplate.opsForValue().get(dto.getPhone());

        if (!Objects.equals(dto.getCode(), code)) {
            throw new RuntimeException("验证码错误！");
        }

        User user = new User();
        BeanUtils.copyProperties(dto, user);

        // 利用雪花算法生成ID
        user.setId(IdWorker.getId());
        // 利用SpringSecurity加密密码
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        userMapper.insertUser(user);

        // 删除缓存中的验证码
        redisTemplate.delete(dto.getPhone());
    }

    /**
     * 发送邮箱验证码
     *
     * @param email 邮箱
     */
    @Override
    public void sendEmailCode(String email) {
        if (email.isEmpty()) {
            throw new RuntimeException("邮箱不能为空！");
        }

        String code = codeUtil.generateCode();

        redisTemplate.opsForValue().set(email, code, emailCodeExpire, TimeUnit.MINUTES);

        emailUtil.sendEmailCode(email, code);
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

        // 生成验证码
        String code = codeUtil.generateCode();

        // 将验证码存入Redis
        redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);

        // 发送验证码
        smsUtil.sendSMSCode(phone, code);
    }
}
