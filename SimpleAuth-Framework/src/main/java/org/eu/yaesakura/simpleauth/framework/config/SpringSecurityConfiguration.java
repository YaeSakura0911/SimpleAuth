package org.eu.yaesakura.simpleauth.framework.config;

import org.eu.yaesakura.simpleauth.framework.CustomCodeAuthenticationProvider;
import org.eu.yaesakura.simpleauth.framework.CustomPersistentTokenBasedRememberMeServices;
import org.eu.yaesakura.simpleauth.framework.CustomPersistentTokenRepositoryImpl;
import org.eu.yaesakura.simpleauth.framework.filter.CustomPasswordAuthenticationFilter;
import org.eu.yaesakura.simpleauth.framework.filter.CustomCodeAuthenticationFilter;
import org.eu.yaesakura.simpleauth.framework.handler.*;
import org.eu.yaesakura.simpleauth.framework.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
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
import org.springframework.security.web.authentication.session.*;
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

    @Value("${simple-auth.config.remember-me-expire}")
    private Integer rememberMeExpire;

    private final UserService userService;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAuthenticationSuccessHandler authenticationSuccessHandler;
    private final CustomAuthenticationFailureHandler authenticationFailureHandler;
    private final CustomLogoutSuccessHandler logoutSuccessHandler;
    private final CustomPersistentTokenRepositoryImpl tokenRepository;
    private final StringRedisTemplate stringRedisTemplate;

    @Autowired
    public SpringSecurityConfiguration (
            CustomAccessDeniedHandler accessDeniedHandler,
            CustomAuthenticationEntryPoint authenticationEntryPoint,
            CustomAuthenticationFailureHandler authenticationFailureHandler,
            CustomAuthenticationSuccessHandler authenticationSuccessHandler,
            CustomLogoutSuccessHandler logoutSuccessHandler,
            CustomPersistentTokenRepositoryImpl tokenRepository,
            UserService userService,
            StringRedisTemplate stringRedisTemplate) {
        this.accessDeniedHandler = accessDeniedHandler;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.authenticationFailureHandler = authenticationFailureHandler;
        this.logoutSuccessHandler = logoutSuccessHandler;
        this.tokenRepository = tokenRepository;
        this.userService = userService;
        this.stringRedisTemplate = stringRedisTemplate;
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
                .formLogin(AbstractHttpConfigurer::disable)
                // 记住我
                .rememberMe(rememberMe -> {
                    rememberMe.rememberMeServices(customPersistentTokenBasedRememberMeServices());
                })
                // OAuth2
//                .oauth2Login(oauth2 -> {
////                    oauth2.loginProcessingUrl("/login");
////                    oauth2.loginPage("/login");
//                    oauth2.authorizationEndpoint(authorization -> {
//                        authorization.baseUri("/auth/login/oauth2");
//                    });
//                    oauth2.redirectionEndpoint(redirection -> {
//                        redirection.baseUri("/auth/login/oauth2/callback/*");
//                    });
//                })
                // 认证请求
                .authorizeHttpRequests(authorize -> {
                    // 放行所有认证接口
                    authorize.requestMatchers("/auth/**").permitAll();
                    // 其他接口都要认证
                    authorize.anyRequest().authenticated();
                })
                // 注销
                .logout(logout -> {
                    // 配置注销请求路径
                    logout.logoutUrl("/auth/logout");
                    // 配置注销成功处理器
                    logout.logoutSuccessHandler(logoutSuccessHandler);
                })
                // 异常处理器
                .exceptionHandling(exception -> {
                    exception.accessDeniedHandler(accessDeniedHandler);
                    exception.authenticationEntryPoint(authenticationEntryPoint);
                })
                // 添加自定义认证过滤器
                .addFilterAfter(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(customAuthenticationFilter(), CustomPasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public CustomPasswordAuthenticationFilter customAuthenticationFilter() {
        CustomPasswordAuthenticationFilter customPasswordAuthenticationFilter = new CustomPasswordAuthenticationFilter("/auth/login");
        // 配置认证管理器
        customPasswordAuthenticationFilter.setAuthenticationManager(authenticationManager());
        // 配置认证成功处理器
        customPasswordAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        // 配置认证失败处理器
        customPasswordAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
        customPasswordAuthenticationFilter.setSecurityContextRepository(new HttpSessionSecurityContextRepository());
        // 配置会话认证策略
        customPasswordAuthenticationFilter.setSessionAuthenticationStrategy(compositeSessionAuthenticationStrategy());
        // 配置记住我服务
        customPasswordAuthenticationFilter.setRememberMeServices(customPersistentTokenBasedRememberMeServices());
        return customPasswordAuthenticationFilter;
    }

    @Bean
    public CustomCodeAuthenticationFilter customCodeAuthenticationFilter() {
        CustomCodeAuthenticationFilter customCodeAuthenticationFilter = new CustomCodeAuthenticationFilter("/auth/login/code");
        customCodeAuthenticationFilter.setAuthenticationManager(authenticationManager());
        customCodeAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        customCodeAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
        customCodeAuthenticationFilter.setSecurityContextRepository(new HttpSessionSecurityContextRepository());
        customCodeAuthenticationFilter.setSessionAuthenticationStrategy(compositeSessionAuthenticationStrategy());
        customCodeAuthenticationFilter.setRememberMeServices(customPersistentTokenBasedRememberMeServices());
        return customCodeAuthenticationFilter;
    }

    /**
     * 认证管理器
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(argon2PasswordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userService);

        CustomCodeAuthenticationProvider customCodeAuthenticationProvider = new CustomCodeAuthenticationProvider();
        customCodeAuthenticationProvider.setUserDetailsService(userService);
        customCodeAuthenticationProvider.setStringRedisTemplate(stringRedisTemplate);

        return new ProviderManager(daoAuthenticationProvider, customCodeAuthenticationProvider);
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
     *
     */
    @Bean
    public SessionAuthenticationStrategy sessionFixationProtectionStrategy() {
        return new SessionFixationProtectionStrategy();
    }

    /**
     * 组合会话认证策略，按顺序执行委托的策略
     */
    @Bean
    public SessionAuthenticationStrategy compositeSessionAuthenticationStrategy() {
        List<SessionAuthenticationStrategy> delegateStrategies = new ArrayList<>();
        delegateStrategies.add(concurrentSessionControlAuthenticationStrategy());
        delegateStrategies.add(sessionFixationProtectionStrategy());
        delegateStrategies.add(registerSessionAuthenticationStrategy());
        return new CompositeSessionAuthenticationStrategy(delegateStrategies);
    }

    @Bean
    public RememberMeServices customPersistentTokenBasedRememberMeServices() {
        CustomPersistentTokenBasedRememberMeServices rememberMeServices = new CustomPersistentTokenBasedRememberMeServices("SimpleAuth", userService, tokenRepository);
        rememberMeServices.setTokenValiditySeconds(rememberMeExpire);
        return rememberMeServices;
    }

    /**
     * 密码编码器
     */
    @Bean
    public PasswordEncoder argon2PasswordEncoder() {
        return Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
    }
}
