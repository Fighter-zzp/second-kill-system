package com.zzp.second.kill.admin.configure;

import com.zzp.second.kill.admin.filter.FrontTokenAuthenticationFilter;
import com.zzp.second.kill.admin.filter.ImageCodeFilter;
import com.zzp.second.kill.admin.handler.CustomizeAuthenticationFailureHandler;
import com.zzp.second.kill.admin.handler.CustomizeAuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;

/**
 * @author 佐斯特勒
 * <p>
 *  web安全配置类
 * </p>
 * @version v1.0.0
 * @date 2020/1/21 23:35
 * @see  WebSecurityConfigurer
 **/
@Configuration
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    /**
     * 图片验证码过滤器
     */
    @Resource
    private ImageCodeFilter imageCodeFilter;

    /**
     * 自定义认证失败操作类
     */
    @Resource
    private CustomizeAuthenticationFailureHandler customizeAuthenticationFailureHandler;

    /**
     * 自定义认证成功操作类
     */
    @Resource
    private CustomizeAuthenticationSuccessHandler customizeAuthenticationSuccessHandler;

    /**
     * 前端token过滤器
     */
    @Resource
    private FrontTokenAuthenticationFilter frontTokenAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     *  spring security 跨域
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        //指定允许跨域的请求(*所有)：http://wap.ivt.guansichou.com
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
        // setAllowCredentials(true) is important, otherwise:
        // 当请求的凭据模式为“ include”时，响应中“ Access-Control-Allow-Origin”标头的值不得为通配符“ *”。
        configuration.setAllowCredentials(true);
        // setAllowedHeaders很重要！没有它，OPTIONS飞行前请求将失败，并显示403 Invalid CORS request
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "X-User-Agent", "Content-Type"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .servletApi().disable()
                .requestCache().disable()
                .cors()
                .and()
                .addFilterBefore(frontTokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(imageCodeFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin()
                .loginProcessingUrl("/login")
                .failureHandler(customizeAuthenticationFailureHandler)
                .successHandler(customizeAuthenticationSuccessHandler)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/imageCode").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .csrf().disable();
    }
}
