package org.eu.yaesakura.simpleauth.framework.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 用户实体类
 *
 * @author YaeSakura
 */

@Data
public class User implements UserDetails {
    // 用户ID
    private Long id;
    // 用户名
    private String username;
    // 邮箱
    private String email;
    // 手机号
    private String phone;
    // 密码
    private String password;
    // 是否启用
    private Boolean enabled;
    // 权限集合
    @TableField(exist = false)
    private List<Permission> permissionList;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (permissionList != null) {
            return permissionList.stream().map(permission -> new SimpleGrantedAuthority(permission.getCode())).toList();
        }
        else {
            return new ArrayList<>();
        }
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
