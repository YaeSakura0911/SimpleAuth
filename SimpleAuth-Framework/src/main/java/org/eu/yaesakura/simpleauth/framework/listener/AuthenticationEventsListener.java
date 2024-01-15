package org.eu.yaesakura.simpleauth.framework.listener;

import org.eu.yaesakura.simpleauth.framework.CustomAuthenticationDetails;
import org.eu.yaesakura.simpleauth.framework.domain.entity.LoginLog;
import org.eu.yaesakura.simpleauth.framework.service.LoginLogService;
import org.eu.yaesakura.simpleauth.framework.util.BrowserUtil;
import org.eu.yaesakura.simpleauth.framework.util.IpUtil;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 认证事件监听器
 *
 * @author YaeSakura
 */

@Component
public class AuthenticationEventsListener {

    private final LoginLogService loginLogService;

    public AuthenticationEventsListener(LoginLogService loginLogService, IpUtil ipUtil, BrowserUtil browserUtil) {
        this.loginLogService = loginLogService;
    }

    @EventListener
    public void onSuccess(InteractiveAuthenticationSuccessEvent success) {

        CustomAuthenticationDetails details = (CustomAuthenticationDetails) success.getAuthentication().getDetails();

        LoginLog loginLog = new LoginLog();
        loginLog.setPrincipal(details.getPrincipal());
        loginLog.setIp(details.getIp());
        loginLog.setLocate(details.getLocate());
        loginLog.setBrowser(details.getBrowser());
        loginLog.setPlatform(details.getPlatform());
        loginLog.setStatus(true);
        loginLog.setTime(LocalDateTime.now());

        loginLogService.insertLoginLog(loginLog);
    }

    @EventListener
    public void onFailure(AbstractAuthenticationFailureEvent failures) {

        CustomAuthenticationDetails details = (CustomAuthenticationDetails) failures.getAuthentication().getDetails();

        LoginLog loginLog = new LoginLog();
        loginLog.setPrincipal(details.getPrincipal());
        loginLog.setIp(details.getIp());
        loginLog.setLocate(details.getLocate());
        loginLog.setBrowser(details.getBrowser());
        loginLog.setPlatform(details.getPlatform());
        loginLog.setStatus(false);
        loginLog.setRemark(failures.getException().getMessage());
        loginLog.setTime(LocalDateTime.now());

        loginLogService.insertLoginLog(loginLog);
    }
}
