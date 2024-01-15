package org.eu.yaesakura.simpleauth.framework.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 登录日志实体类
 *
 * @author YaeSakura
 */

@Data
public class LoginLog {
    // ID
    @TableId(type = IdType.AUTO)
    private Long id;
    // 登录凭证
    private String principal;
    // IP地址
    private String ip;
    // 登录地点
    private String locate;
    // 浏览器
    private String browser;
    // 操作系统
    private String platform;
    // 登录状态
    private Boolean status;
    // 备注
    private String remark;
    // 登录时间
    private LocalDateTime time;
}
