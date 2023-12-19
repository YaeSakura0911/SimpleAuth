package org.eu.yaesakura.simpleauth.framework.config;

import org.eu.yaesakura.simpleauth.framework.CustomPersistentTokenBasedRememberMeServices;
import org.eu.yaesakura.simpleauth.framework.CustomPersistentTokenRepositoryImpl;
import org.eu.yaesakura.simpleauth.framework.filter.CustomAuthenticationFilter;
import org.eu.yaesakura.simpleauth.framework.handler.*;
import org.eu.yaesakura.simpleauth.framework.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.CompositeSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.List;

/**
 * SpringSecurity配置类
 *
 * @author YaeSakura
 */

@EnableWebSecurity(debug = true)
@EnableMethodSecurity
@Configuration
public class SpringSecurityConfiguration {

    private final UserService userService;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAuthenticationSuccessHandler authenticationSuccessHandler;
    private final CustomAuthenticationFailureHandler authenticationFailureHandler;
    private final CustomLogoutSuccessHandler logoutSuccessHandler;
    private final CustomPersistentTokenRepositoryImpl tokenRepository;

    @Autowired
    public SpringSecurityConfiguration (
            CustomAccessDeniedHandler accessDeniedHandler,
            CustomAuthenticationEntryPoint authenticationEntryPoint,
            CustomAuthenticationFailureHandler authenticationFailureHandler,
            CustomAuthenticationSuccessHandler authenticationSuccessHandler,
            CustomLogoutSuccessHandler logoutSuccessHandler,
            CustomPersistentTokenRepositoryImpl tokenRepository,
            UserService userService) {
        this.accessDeniedHandler = accessDeniedHandler;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.authenticationFailureHandler = authenticationFailureHandler;
        this.logoutSuccessHandler = logoutSuccessHandler;
        this.tokenRepository = tokenRepository;
        this.userService = userService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                // 跨域
                .cors(cors -> {
                    UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
                    CorsConfiguration corsConfiguration = new CorsConfiguration();
                    corsConfiguration.addAllowedOriginPattern("http://localhost:*");
                    corsConfiguration.addAllowedHeader("*");
                    corsConfiguration.addAllowedMethod("*");
                    corsConfiguration.setAllowCredentials(true);
                    urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
                    cors.configurationSource(urlBasedCorsConfigurationSource);
                })
                // 认证请求
                .authorizeHttpRequests(authorize -> {
                    // 放行登录、注销接口
                    authorize.requestMatchers(HttpMethod.POST, "/login", "/logout").permitAll();
                    // 其他接口都要认证
                    authorize.anyRequest().authenticated();
                })
                // 注销
                .logout(logout -> {
                    // 配置注销请求路径
                    logout.logoutUrl("/logout");
                    // 配置注销成功处理器
                    logout.logoutSuccessHandler(logoutSuccessHandler);
                })
                // 异常处理器
                .exceptionHandling(exception -> {
                    exception.accessDeniedHandler(accessDeniedHandler);
                    exception.authenticationEntryPoint(authenticationEntryPoint);
                })
                // 添加自定义认证过滤器
                .addFilterAfter(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public CustomAuthenticationFilter customAuthenticationFilter() {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter("/login");
        // 配置认证管理器
        customAuthenticationFilter.setAuthenticationManager(authenticationManager());
        // 配置认证成功处理器
        customAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        // 配置认证失败处理器
        customAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
        customAuthenticationFilter.setSecurityContextRepository(new HttpSessionSecurityContextRepository());
        // 配置会话认证策略
        customAuthenticationFilter.setSessionAuthenticationStrategy(compositeSessionAuthenticationStrategy());
        // 配置记住我服务
        customAuthenticationFilter.setRememberMeServices(customPersistentTokenBasedRememberMeServices());
        return customAuthenticationFilter;
    }

    /**
     * 认证管理器
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(argon2PasswordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userService);

        return new ProviderManager(daoAuthenticationProvider);
    }

    /**
     * 会话信息存储
     */
    @Bean
    public SessionRegistry sessionRegistryImpl() {
        return new SessionRegistryImpl();
    }

    /**
     * 处理并发会话控制的策略
     */
    @Bean
    public SessionAuthenticationStrategy concurrentSessionControlAuthenticationStrategy() {
        return new ConcurrentSessionControlAuthenticationStrategy(sessionRegistryImpl());
    }

    /**
     * 身份验证成功后用于向 SessionRegistry 注册用户的策略
     */
    @Bean
    public SessionAuthenticationStrategy registerSessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(sessionRegistryImpl());
    }

    /**
     * 组合会话认证策略，按顺序执行委托的策略
     */
    @Bean
    public SessionAuthenticationStrategy compositeSessionAuthenticationStrategy() {
        List<SessionAuthenticationStrategy> delegateStrategies = new ArrayList<>();
        delegateStrategies.add(concurrentSessionControlAuthenticationStrategy());
        delegateStrategies.add(registerSessionAuthenticationStrategy());
        return new CompositeSessionAuthenticationStrategy(delegateStrategies);
    }

    @Bean
    public RememberMeServices customPersistentTokenBasedRememberMeServices() {
        return new CustomPersistentTokenBasedRememberMeServices("SimpleAuth", userService, tokenRepository);
    }

    /**
     * 密码编码器
     */
    @Bean
    public PasswordEncoder argon2PasswordEncoder() {
        return Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
    }
}
