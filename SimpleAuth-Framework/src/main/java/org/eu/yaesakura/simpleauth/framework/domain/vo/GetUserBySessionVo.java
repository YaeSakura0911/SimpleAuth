package org.eu.yaesakura.simpleauth.framework.domain.vo;

import lombok.Data;

/**
 * @author YaeSakura
 */

@Data
public class GetUserBySessionVo {
    // 用户ID
    private Long id;
    // 用户昵称
    private String name;
}
