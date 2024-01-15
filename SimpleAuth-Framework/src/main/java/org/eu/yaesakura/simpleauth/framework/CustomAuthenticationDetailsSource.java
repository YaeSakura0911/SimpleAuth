package org.eu.yaesakura.simpleauth.framework;

import jakarta.servlet.http.HttpServletRequest;
import org.eu.yaesakura.simpleauth.framework.util.BrowserUtil;
import org.eu.yaesakura.simpleauth.framework.util.IpUtil;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.stereotype.Component;

/**
 * 自定义认证详情源
 *
 * @author YaeSakura
 */

@Component
public class CustomAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest, CustomAuthenticationDetails> {

    private final IpUtil ipUtil;
    private final BrowserUtil browserUtil;

    public CustomAuthenticationDetailsSource(IpUtil ipUtil, BrowserUtil browserUtil) {
        this.ipUtil = ipUtil;
        this.browserUtil = browserUtil;
    }

    @Override
    public CustomAuthenticationDetails buildDetails(HttpServletRequest context) {
        return new CustomAuthenticationDetails(context, ipUtil, browserUtil);
    }
}
