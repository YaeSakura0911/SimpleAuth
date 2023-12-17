package org.eu.yaesakura.simpleauth.framework.domain.entity;

import lombok.Data;

/**
 * 权限实体类
 *
 * @author YaeSakura
 */

@Data
public class Permission {
    // 权限ID
    private Integer id;
    // 权限代码
    private String code;
    // 权限名称
    private String name;
}
