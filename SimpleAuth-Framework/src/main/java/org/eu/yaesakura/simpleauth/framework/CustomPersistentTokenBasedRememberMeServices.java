package org.eu.yaesakura.simpleauth.framework;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

/**
 * 自定义记住我服务
 *
 * @author YaeSakura
 */

public class CustomPersistentTokenBasedRememberMeServices extends PersistentTokenBasedRememberMeServices {

    public CustomPersistentTokenBasedRememberMeServices(String key, UserDetailsService userDetailsService, PersistentTokenRepository tokenRepository) {
        super(key, userDetailsService, tokenRepository);
    }

    @Override
    protected boolean rememberMeRequested(HttpServletRequest request, String parameter) {
        return (Boolean) request.getAttribute("remember-me");
    }
}
