package org.eu.yaesakura.simpleauth.framework.domain.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * 查询登录日志表单
 *
 * @author YaeSakura
 */

@Data
public class QueryLoginLogDTO {
    // 当前页
    private Long current;
    // 分页大小
    private Long pageSize;
    // 登录主体
    private String principal;
    // IP地址
    private String ip;
    // 登录状态
    private Boolean status;
    // 日期范围
    private LocalDate[] dateRange;
}
