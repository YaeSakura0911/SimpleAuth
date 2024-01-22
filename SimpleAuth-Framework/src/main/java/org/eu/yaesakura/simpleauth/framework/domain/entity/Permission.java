package org.eu.yaesakura.simpleauth.framework.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.List;

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
    // 路由路径
    private String path;
    // 图标
    private String icon;
    // 组件路径
    private String component;
    // 权限名称
    private String name;
    // 权限标题
    private String title;
    // 父级ID
    private Integer parent;
}
