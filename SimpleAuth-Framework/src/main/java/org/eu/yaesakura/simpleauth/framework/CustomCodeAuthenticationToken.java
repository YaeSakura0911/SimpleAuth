package org.eu.yaesakura.simpleauth.framework;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 自定义验证码认证令牌
 *
 * @author YaeSakura
 */
public class CustomCodeAuthenticationToken extends AbstractAuthenticationToken {

    public final Object principal;

    public Object credentials;

    public CustomCodeAuthenticationToken(Object principal, Object credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(false);
    }

    public CustomCodeAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true);
    }

    public static CustomCodeAuthenticationToken unauthenticated(Object principal, Object credentials) {
        return new CustomCodeAuthenticationToken(principal, credentials);
    }

    public static CustomCodeAuthenticationToken authenticated(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        return new CustomCodeAuthenticationToken(principal, credentials, authorities);
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }
}
