package org.eu.yaesakura.simpleauth.framework.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.eu.yaesakura.simpleauth.framework.domain.PageResult;
import org.eu.yaesakura.simpleauth.framework.domain.dto.QueryLoginLogDTO;
import org.eu.yaesakura.simpleauth.framework.domain.entity.LoginLog;
import org.eu.yaesakura.simpleauth.framework.mapper.LoginLogMapper;
import org.eu.yaesakura.simpleauth.framework.service.LoginLogService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 登录日志服务实现类
 *
 * @author YaeSakura
 */

@Service
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog> implements LoginLogService {

    private final LoginLogMapper loginLogMapper;

    public LoginLogServiceImpl(LoginLogMapper loginLogMapper) {
        this.loginLogMapper = loginLogMapper;
    }

    /**
     * 插入登录日志
     * @param loginLog 登录日志实体类
     */
    public void insertLoginLog(LoginLog loginLog) {
        loginLogMapper.insertLoginLog(loginLog);
    }

    /**
     * 获取登录日志
     * @param dto 查询登录日志表单
     * @return
     */
    @Override
    public PageResult<LoginLog> getLoginLogByPage(QueryLoginLogDTO dto) {

        LambdaQueryWrapper<LoginLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(dto.getPrincipal() != null && !dto.getPrincipal().isEmpty(), LoginLog::getPrincipal, dto.getPrincipal());
        queryWrapper.eq(dto.getIp() != null && !dto.getIp().isEmpty(), LoginLog::getIp, dto.getIp());
        queryWrapper.eq(dto.getStatus() != null, LoginLog::getStatus, dto.getStatus());
        if (dto.getDateRange() != null) {
            queryWrapper.between(LoginLog::getTime, LocalDateTime.of(dto.getDateRange()[0], LocalTime.MIN), LocalDateTime.of(dto.getDateRange()[1], LocalTime.MAX) );
        }

        queryWrapper.orderByDesc(LoginLog::getTime);
        Page<LoginLog> page = page(new Page<>(dto.getCurrent(), dto.getPageSize()), queryWrapper);

        PageResult<LoginLog> loginLogPageResult = new PageResult<>(dto.getCurrent(), dto.getPageSize(), page.getTotal());
        loginLogPageResult.setData(page.getRecords());

        return loginLogPageResult;
    }
}
