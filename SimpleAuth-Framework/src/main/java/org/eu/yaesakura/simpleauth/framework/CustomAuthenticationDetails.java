package org.eu.yaesakura.simpleauth.framework;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.eu.yaesakura.simpleauth.framework.util.BrowserUtil;
import org.eu.yaesakura.simpleauth.framework.util.IpUtil;

import java.io.Serializable;

/**
 * 自定义认证详情
 *
 * @author YaeSakura
 */

@Getter
public class CustomAuthenticationDetails implements Serializable {

    private final String principal;
    private final String ip;
    private final String locate;
    private final String browser;
    private final String platform;

    private final IpUtil ipUtil;
    private final BrowserUtil browserUtil;

    public CustomAuthenticationDetails(HttpServletRequest request, IpUtil ipUtil, BrowserUtil browserUtil) {
        this.ipUtil = ipUtil;
        this.browserUtil = browserUtil;

        this.principal = extractPrincipal(request);
        this.ip = extractIp(request);
        this.locate = extractLocate(request);
        this.browser = extractBrowser(request);
        this.platform = extractPlatform(request);
    }

    private String extractPrincipal(HttpServletRequest request) {
        String principal = (String) request.getAttribute("principal");
        request.removeAttribute("principal");
        return principal;
    }

    private String extractIp(HttpServletRequest request) {
        return ipUtil.getRealIp(request);
    }

    private String extractLocate(HttpServletRequest request) {
        String realIp = ipUtil.getRealIp(request);
        return ipUtil.ip2geo(realIp);
    }

    private String extractBrowser(HttpServletRequest request) {
        String userAgent = request.getHeader("user-agent");
        return browserUtil.getBrowser(userAgent);
    }

    private String extractPlatform(HttpServletRequest request) {
        String userAgent = request.getHeader("user-agent");
        return browserUtil.getPlatform(userAgent);
    }
}
