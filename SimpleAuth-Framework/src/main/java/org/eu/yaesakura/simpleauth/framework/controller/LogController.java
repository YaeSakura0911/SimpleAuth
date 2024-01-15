package org.eu.yaesakura.simpleauth.framework.controller;

import org.eu.yaesakura.simpleauth.framework.domain.PageResult;
import org.eu.yaesakura.simpleauth.framework.domain.dto.QueryLoginLogDTO;
import org.eu.yaesakura.simpleauth.framework.domain.entity.LoginLog;
import org.eu.yaesakura.simpleauth.framework.service.LoginLogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 日志控制层
 *
 * @author YaeSakura
 */

@RestController
@RequestMapping("/log")
public class LogController {

    private final LoginLogService loginLogService;

    public LogController(LoginLogService loginLogService) {
        this.loginLogService = loginLogService;
    }

    @GetMapping("/login/list")
    public PageResult<LoginLog> getLoginLogByPage(QueryLoginLogDTO dto) {
        return loginLogService.getLoginLogByPage(dto);
    }
}
