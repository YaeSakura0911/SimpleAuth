package org.eu.yaesakura.simpleauth.framework.controller;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eu.yaesakura.simpleauth.framework.domain.PageResult;
import org.eu.yaesakura.simpleauth.framework.domain.dto.ExportLoginLogDTO;
import org.eu.yaesakura.simpleauth.framework.domain.dto.QueryLoginLogDTO;
import org.eu.yaesakura.simpleauth.framework.domain.entity.LoginLog;
import org.eu.yaesakura.simpleauth.framework.service.LoginLogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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

    @GetMapping("/login/export")
    public void exportLoginLog(HttpServletResponse response, ExportLoginLogDTO dto) {
        loginLogService.exportLoginLog(response, dto);
    }
}
