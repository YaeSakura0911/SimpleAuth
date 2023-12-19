package org.eu.yaesakura.simpleauth.framework.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 持续登录实体类
 *
 * @author Cuifa03
 */

@Data
public class PersistentLogin {
    // 用户名
    private String username;
    private String series;
    private String token;
    // 上次使用时间
    private LocalDateTime lastUsed;
}
