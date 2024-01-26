package org.eu.yaesakura.simpleauth.framework.domain.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * 导出登录日志表单
 *
 * @author YaeSakura
 */

@Data
public class ExportLoginLogDTO {
    // 登录主体
    private String principal;
    // IP地址
    private String ip;
    // 登录状态
    private Boolean status;
    // 日期范围
    private List<LocalDate> dateRange;
}
