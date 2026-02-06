package com.china.soft.srcurity.config;

import cn.hutool.core.collection.CollUtil;
import com.china.soft.srcurity.filter.AuthenticationFilter;
import com.china.soft.srcurity.handler.*;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.context.ApplicationContext;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.china.soft.commons.utils.CollectionUtils.convertList;

/**
 * description SpringSecurity核心配置类
 * @author  MingYue
 * @version 1.0
 * @since   2025/3/21
 * @updateTime  2026-02-05
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Slf4j
public class SecurityConfig {

    @Resource
    private UserLoginSuccessHandler userLoginSuccessHandler;
    @Resource
    private UserLoginFailureHandler userLoginFailureHandler;
    @Resource
    private UserLogoutSuccessHandler userLogoutSuccessHandler;
    @Resource
    private UserAuthAccessDeniedHandler userAuthAccessDeniedHandler;
    @Resource
    private UserAuthenticationEntryPointHandler userAuthenticationEntryPointHandler;
    @Resource
    private UserAuthenticationProvider userAuthenticationProvider;
    @Resource
    private ApplicationContext applicationContext;
    
     // TODO @MingYue：

    /**
     * 自定义 PermissionEvaluator
     *
     */
    @Bean
    public DefaultWebSecurityExpressionHandler userSecurityExpressionHandler() {
        DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
        handler.setPermissionEvaluator(new UserPermissionEvaluator());
        return handler;
    }

    /**
     * AuthenticationManager（替代 configure(AuthenticationManagerBuilder)）
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(userAuthenticationProvider)
                .build();
    }

    /**
     * 核心安全配置（替代 configure(HttpSecurity)）
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // 1. PermitAll 注解解析
        Multimap<HttpMethod, String> permits = getPermitAllUrlsFromAnnotations();
        for (Map.Entry<HttpMethod, String> entry : permits.entries()) {
            http.authorizeRequests()
                    .antMatchers(entry.getKey(), entry.getValue())
                    .permitAll();
        }
        http.authorizeRequests(authorize -> authorize
                        .antMatchers(HttpMethod.GET,
                                "/", "/*.html", "favicon.ico",
                                "/**/*.html", "/**/*.css", "/**/*.js")
                        .permitAll()
                        .antMatchers("/login").permitAll()
                        .antMatchers(
                                "/swagger-ui/**",
                                "/swagger-resources/**",
                                "/v2/api-docs",
                                "/v3/api-docs",
                                "/webjars/**",
                                "/doc.html",
                                "/ws/**")
                        .permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic()
                .authenticationEntryPoint(userAuthenticationEntryPointHandler)
                .and()
                .formLogin()
                .loginProcessingUrl("/login")
                .successHandler(userLoginSuccessHandler)
                .failureHandler(userLoginFailureHandler)
                .and()
                .logout()
                .logoutUrl("/userLogout")
                .logoutSuccessHandler(userLogoutSuccessHandler)
                .and()
                .exceptionHandling()
                .accessDeniedHandler(userAuthAccessDeniedHandler)
                .and()
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .headers().cacheControl();

        http.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);

        log.info("SecurityConfig initialized successfully");
        return http.build();
    }

    @Bean
    public AuthenticationFilter authenticationTokenFilterBean() {
        return new AuthenticationFilter();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        return new JdbcTokenRepositoryProvider();
    }

    /**
     * 配置 URL 的安全配置
     * anyRequest          |   匹配所有请求路径
     * access              |   SpringEl表达式结果为true时可以访问
     * anonymous           |   匿名可以访问
     * denyAll             |   用户不能访问
     * fullyAuthenticated  |   用户完全认证可以访问（非remember-me下自动登录）
     * hasAnyAuthority     |   如果有参数，参数表示权限，则其中任何一个权限可以访问
     * hasAnyRole          |   如果有参数，参数表示角色，则其中任何一个角色可以访问
     * hasAuthority        |   如果有参数，参数表示权限，则其权限可以访问
     * hasIpAddress        |   如果有参数，参数表示IP地址，如果用户IP和参数匹配，则可以访问
     * hasRole             |   如果有参数，参数表示角色，则其角色可以访问
     * permitAll           |   用户可以任意访问
     * rememberMe          |   允许通过remember-me登录的用户访问
     * authenticated       |   用户登录后可访问
     */
    private Multimap<HttpMethod, String> getPermitAllUrlsFromAnnotations() {
        Multimap<HttpMethod, String> result = HashMultimap.create();
        // 获得接口对应的 HandlerMethod 集合
        RequestMappingHandlerMapping requestMappingHandlerMapping = (RequestMappingHandlerMapping)
                applicationContext.getBean("requestMappingHandlerMapping");
        Map<RequestMappingInfo, HandlerMethod> handlerMethodMap = requestMappingHandlerMapping.getHandlerMethods();
        // 获得有 @PermitAll 注解的接口
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethodMap.entrySet()) {
            HandlerMethod handlerMethod = entry.getValue();
            if (!handlerMethod.hasMethodAnnotation(PermitAll.class) &&
                    !handlerMethod.getBeanType().isAnnotationPresent(PermitAll.class)
            ) {
                continue;
            }
            Set<String> urls = new HashSet<>();
            if (entry.getKey().getPatternsCondition() != null) {
                urls.addAll(entry.getKey().getPatternsCondition().getPatterns());
            }
            if (entry.getKey().getPathPatternsCondition() != null) {
                urls.addAll(convertList(entry.getKey().getPathPatternsCondition().getPatterns(), PathPattern::getPatternString));
            }
            if (urls.isEmpty()) {
                continue;
            }

            // 特殊：使用 @RequestMapping 注解，并且未写 method 属性，此时认为都需要免登录
            Set<RequestMethod> methods = entry.getKey().getMethodsCondition().getMethods();
            if (CollUtil.isEmpty(methods)) {
                result.putAll(HttpMethod.GET, urls);
                result.putAll(HttpMethod.POST, urls);
                result.putAll(HttpMethod.PUT, urls);
                result.putAll(HttpMethod.DELETE, urls);
                result.putAll(HttpMethod.HEAD, urls);
                result.putAll(HttpMethod.PATCH, urls);
                continue;
            }
            // 根据请求方法，添加到 result 结果
            entry.getKey().getMethodsCondition().getMethods().forEach(requestMethod -> {
                switch (requestMethod) {
                    case GET:
                        result.putAll(HttpMethod.GET, urls);
                        break;
                    case POST:
                        result.putAll(HttpMethod.POST, urls);
                        break;
                    case PUT:
                        result.putAll(HttpMethod.PUT, urls);
                        break;
                    case DELETE:
                        result.putAll(HttpMethod.DELETE, urls);
                        break;
                    case HEAD:
                        result.putAll(HttpMethod.HEAD, urls);
                        break;
                    case PATCH:
                        result.putAll(HttpMethod.PATCH, urls);
                        break;
                }
            });
        }
        return result;
    }
}
