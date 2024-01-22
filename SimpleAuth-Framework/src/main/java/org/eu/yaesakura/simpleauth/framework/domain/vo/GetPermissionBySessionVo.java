package org.eu.yaesakura.simpleauth.framework.domain.vo;

import lombok.Data;
import org.eu.yaesakura.simpleauth.framework.domain.entity.Permission;

import java.util.List;

/**
 * @author YaeSakura
 */

@Data
public class GetPermissionBySessionVo {
    private List<Permission> permissions;
}
