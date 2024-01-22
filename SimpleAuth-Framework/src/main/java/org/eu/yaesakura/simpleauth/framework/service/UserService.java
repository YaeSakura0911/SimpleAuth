package org.eu.yaesakura.simpleauth.framework.service;

import org.eu.yaesakura.simpleauth.framework.domain.vo.GetUserBySessionVo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 用户服务类
 *
 * @author YaeSakura
 */

public interface UserService extends UserDetailsService {

}
