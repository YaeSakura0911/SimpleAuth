package org.eu.yaesakura.simpleauth.framework.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eu.yaesakura.simpleauth.framework.domain.PageResult;
import org.eu.yaesakura.simpleauth.framework.domain.dto.ExportLoginLogDTO;
import org.eu.yaesakura.simpleauth.framework.domain.dto.QueryLoginLogDTO;
import org.eu.yaesakura.simpleauth.framework.domain.entity.LoginLog;
import org.eu.yaesakura.simpleauth.framework.mapper.LoginLogMapper;
import org.eu.yaesakura.simpleauth.framework.service.LoginLogService;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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

        PageResult<LoginLog> loginLogPageResult = new PageResult<>(dto.getCurrent(), dto.getPageSize(), (int) page.getTotal());
        loginLogPageResult.setData(page.getRecords());

        return loginLogPageResult;
    }

    /**
     * 导出登录日志
     *
     * @param response
     * @param dto      查询登录日志表单
     * @return
     */
    @Override
    public void exportLoginLog(HttpServletResponse response, ExportLoginLogDTO dto) {
        List<LoginLog> loginLogList = loginLogMapper.selectLoginLog(dto);

        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet();

            // 冻结首行
            sheet.createFreezePane(0, 1);

            // 创建表头
            XSSFRow header = sheet.createRow(0);
            header.createCell(0).setCellValue("序号");
            header.createCell(1).setCellValue("登录凭证");
            header.createCell(2).setCellValue("IP地址");
            header.createCell(3).setCellValue("登录地点");
            header.createCell(4).setCellValue("浏览器");
            header.createCell(5).setCellValue("操作系统");
            header.createCell(6).setCellValue("登录状态");
            header.createCell(7).setCellValue("备注");
            header.createCell(8).setCellValue("登录时间");

            // 创建内容
            for (int i = 0; i < loginLogList.size(); i++) {
                XSSFRow row = sheet.createRow(i + 1);

                LoginLog loginLog = loginLogList.get(i);

                row.createCell(0).setCellValue(loginLog.getId());
                row.createCell(1).setCellValue(loginLog.getPrincipal());
                row.createCell(2).setCellValue(loginLog.getIp());
                row.createCell(3).setCellValue(loginLog.getLocate());
                row.createCell(4).setCellValue(loginLog.getBrowser());
                row.createCell(5).setCellValue(loginLog.getPlatform());
                row.createCell(6).setCellValue(loginLog.getStatus());
                row.createCell(7).setCellValue(loginLog.getRemark());
                row.createCell(8).setCellValue(loginLog.getTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }

//            FileOutputStream fileOutputStream = new FileOutputStream("登录日志.xlsx");

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("登录日志.xlsx", StandardCharsets.UTF_8));
            ServletOutputStream outputStream = response.getOutputStream();

            workbook.write(outputStream);

            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
