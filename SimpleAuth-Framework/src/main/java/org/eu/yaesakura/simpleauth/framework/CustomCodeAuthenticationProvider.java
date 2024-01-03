package org.eu.yaesakura.simpleauth.framework;

import lombok.Setter;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Objects;

/**
 * 自定义验证码认证提供者
 *
 * @author YaeSakura
 */

@Setter
public class CustomCodeAuthenticationProvider implements AuthenticationProvider {

    private StringRedisTemplate stringRedisTemplate;
    private UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String emailOrPhone = authentication.getName();

        // 根据key从Redis获取验证码
        String code = stringRedisTemplate.opsForValue().get(emailOrPhone);

        // 根据email或phone从数据库获取用户信息
        UserDetails userDetails = userDetailsService.loadUserByUsername(emailOrPhone);

        if (userDetails == null) {
            throw new BadCredentialsException("邮箱或手机号不存在");
        }

        String presentedCode = authentication.getCredentials().toString();

        if (!Objects.equals(presentedCode, code)) {
            throw new BadCredentialsException("验证码错误");
        }

        // 从Redis删除key
        stringRedisTemplate.delete(emailOrPhone);

        CustomCodeAuthenticationToken result = CustomCodeAuthenticationToken.authenticated(userDetails, authentication.getCredentials(), userDetails.getAuthorities());
        result.setDetails(authentication);

        return result;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (CustomCodeAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
