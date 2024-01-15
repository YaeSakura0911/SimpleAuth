package org.eu.yaesakura.simpleauth.framework.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eu.yaesakura.simpleauth.framework.service.LoginLogService;
import org.eu.yaesakura.simpleauth.framework.util.BrowserUtil;
import org.eu.yaesakura.simpleauth.framework.util.IpUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * 自定义认证失败处理器
 *
 * @author YaeSakura
 */

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final LoginLogService loginLogService;
    private final IpUtil ipUtil;
    private final BrowserUtil browserUtil;

    public CustomAuthenticationFailureHandler(LoginLogService loginLogService, IpUtil ipUtil, BrowserUtil browserUtil) {
        this.loginLogService = loginLogService;
        this.ipUtil = ipUtil;
        this.browserUtil = browserUtil;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

//        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//
//        // 获取IP地址
//        String ip = request.getHeader("x-forwarded-for");
//        if (ip == null || ip.isEmpty() || ip.equalsIgnoreCase("unknown")) {
//            ip = request.getHeader("Proxy-Client-IP");
//        }
//        if (ip == null || ip.isEmpty() || ip.equalsIgnoreCase("unknown")) {
//            ip = request.getHeader("WL-Proxy-Client-IP");
//        }
//        if (ip == null || ip.isEmpty() || ip.equalsIgnoreCase("unknown")) {
//            ip = request.getRemoteAddr();
//        }
//
//        String userAgent = request.getHeader("user-agent");
//
//        LoginLog loginLog = new LoginLog();
//        loginLog.setUserId(user.getId());
//        loginLog.setIp(ip);
//        loginLog.setLocate(ipUtil.ip2geo(ip));
//        loginLog.setBrowser(browserUtil.getBrowser(userAgent));
//        loginLog.setPlatform(browserUtil.getPlatform(userAgent));
//        loginLog.setStatus(false);
//        loginLog.setRemark(exception.getMessage());
//        loginLog.setTime(LocalDateTime.now());
//
//        loginLogService.insertLoginLog(loginLog);

        response.setContentType("application/json;charset=utf-8");
        response.setStatus(500);

//        ResponseResult<Object> responseResult = ResponseResult.error(500, exception.getMessage());
//
//        String s = new ObjectMapper().writeValueAsString(responseResult);

        PrintWriter writer = response.getWriter();
        writer.write(exception.getMessage());
        writer.close();
    }
}
