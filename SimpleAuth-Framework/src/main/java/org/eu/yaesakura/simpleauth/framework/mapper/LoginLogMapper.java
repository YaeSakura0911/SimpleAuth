package org.eu.yaesakura.simpleauth.framework.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.eu.yaesakura.simpleauth.framework.domain.entity.LoginLog;

/**
 * 登录日志持久层
 *
 * @author YaeSakura
 */

@Mapper
public interface LoginLogMapper extends BaseMapper<LoginLog> {
    /**
     * 插入登录日志
     *
     * @param loginLog 登录日志实体类
     */
    @Insert("INSERT INTO login_log VALUE (null, #{principal}, #{ip}, #{locate}, #{browser}, #{platform}, #{status}, #{remark}, #{time})")
    void insertLoginLog(LoginLog loginLog);
}
