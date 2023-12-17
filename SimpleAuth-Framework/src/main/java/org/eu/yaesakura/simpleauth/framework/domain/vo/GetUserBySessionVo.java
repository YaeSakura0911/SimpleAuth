package org.eu.yaesakura.simpleauth.framework.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * @author YaeSakura
 */

@Data
public class GetUserBySessionVo {
    // 用户昵称
    private String name;
    // 权限列表
    private List<String> permissions;
}
