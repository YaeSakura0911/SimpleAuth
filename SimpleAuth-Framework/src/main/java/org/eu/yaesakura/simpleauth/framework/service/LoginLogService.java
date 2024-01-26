package org.eu.yaesakura.simpleauth.framework.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletResponse;
import org.eu.yaesakura.simpleauth.framework.domain.PageResult;
import org.eu.yaesakura.simpleauth.framework.domain.dto.ExportLoginLogDTO;
import org.eu.yaesakura.simpleauth.framework.domain.dto.QueryLoginLogDTO;
import org.eu.yaesakura.simpleauth.framework.domain.entity.LoginLog;

import java.io.FileOutputStream;

/**
 * 登录日志服务
 *
 * @author YaeSakura
 */

public interface LoginLogService extends IService<LoginLog> {
    /**
     * 插入登录日志
     * @param loginLog 登录日志实体类
     */
    void insertLoginLog(LoginLog loginLog);

    /**
     * 获取登录日志
     * @param dto 查询登录日志表单
     * @return
     */
    PageResult<LoginLog> getLoginLogByPage(QueryLoginLogDTO dto);

    /**
     * 导出登录日志
     *
     * @param response
     * @param dto      查询登录日志表单
     * @return
     */
    void exportLoginLog(HttpServletResponse response, ExportLoginLogDTO dto);
}
