package org.eu.yaesakura.simpleauth.framework;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eu.yaesakura.simpleauth.framework.domain.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import java.util.Date;

/**
 * 自定义记住我服务
 *
 * @author YaeSakura
 */

public class CustomPersistentTokenBasedRememberMeServices extends PersistentTokenBasedRememberMeServices {

    private final PersistentTokenRepository tokenRepository;

    public CustomPersistentTokenBasedRememberMeServices(String key, UserDetailsService userDetailsService, PersistentTokenRepository tokenRepository) {
        super(key, userDetailsService, tokenRepository);
        this.tokenRepository = tokenRepository;
    }

    @Override
    protected boolean rememberMeRequested(HttpServletRequest request, String parameter) {
        return (Boolean) request.getAttribute("remember");
    }

    @Override
    protected void onLoginSuccess(HttpServletRequest request, HttpServletResponse response, Authentication successfulAuthentication) {
        User user = (User) successfulAuthentication.getPrincipal();
        String username = null;

        if (user.getUsername() != null) {
            username = user.getUsername();
        } else if (user.getEmail() != null) {
            username = user.getEmail();
        } else if (user.getPhone() != null) {
            username = user.getPhone();
        }

        PersistentRememberMeToken persistentToken = new PersistentRememberMeToken(username, generateSeriesData(), generateTokenData(), new Date());
        try {
            tokenRepository.createNewToken(persistentToken);
            addCookie(persistentToken, request, response);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        super.logout(request, response, authentication);
        if (authentication != null) {
            User user = (User) authentication.getPrincipal();
            String username = null;

            if (user.getUsername() != null) {
                username = user.getUsername();
            } else if (user.getEmail() != null) {
                username = user.getEmail();
            } else if (user.getPhone() != null) {
                username = user.getPhone();
            }
            tokenRepository.removeUserTokens(username);
        }
    }

    private void addCookie(PersistentRememberMeToken token, HttpServletRequest request, HttpServletResponse response) {
        setCookie(new String[] { token.getSeries(), token.getTokenValue() }, getTokenValiditySeconds(), request,
                response);
    }
}
