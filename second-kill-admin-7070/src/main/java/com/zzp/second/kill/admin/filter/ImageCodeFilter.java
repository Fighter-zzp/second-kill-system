package com.zzp.second.kill.admin.filter;

import com.zzp.second.kill.admin.handler.CustomizeAuthenticationFailureHandler;
import com.zzp.second.kill.commons.dto.constant.Constants;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 佐斯特勒
 * <p>
 *  图片验证码过滤器
 * </p>
 * @version v1.0.0
 * @date 2020/1/22 0:14
 * @see  ImageCodeFilter
 **/
@Component
public class ImageCodeFilter extends OncePerRequestFilter {

    @Resource
    private RedisTemplate<String,String> redisTemplate;

    /**
     * 认证失败操作
     */
    @Resource
    private CustomizeAuthenticationFailureHandler customizeAuthenticationFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse resp, FilterChain filterChain)
            throws ServletException, IOException {
        // 获取请求地址uri‘/login’
        var uri = req.getRequestURI();
        if ("POST".equals(req.getMethod()) && Constants.LOGIN_URI.equals(uri)){
            try {
                // 信息验证
                validateProcess(req,resp);
            } catch (AuthenticationException e) {
                // 错误操作
                customizeAuthenticationFailureHandler.onAuthenticationFailure(req,resp,e);
                return;
            }
        }
        filterChain.doFilter(req, resp);
    }

    /**
     * 请求信息验证
     * @param request .
     * @param response .
     * @throws IOException .
     * @throws ServletException .
     */
    private void validateProcess(HttpServletRequest request,
                                 HttpServletResponse response) throws IOException, ServletException {
        // 图片验证码的key值
        var imageCodeInRedisKey = request.getParameter("imageCodeKey");
        // redis中的图片验证码
        var imageCodeInRedis = redisTemplate.opsForValue().get(imageCodeInRedisKey);
        //页面传过来的图片验证码的值
        var code = request.getParameter("imageCode");

        // 异常处理
        if(Strings.isBlank(code)) {
            throw new InternalAuthenticationServiceException("验证码不能为空.");
        }
        if(null == imageCodeInRedis) {
            throw new InternalAuthenticationServiceException("验证码不存在或者已过期.");
        }
        if(!code.equals(imageCodeInRedis)) {
            throw new InternalAuthenticationServiceException("验证码错误.");
        }
        //如果验证都通过了, 删除redis中的图片验证码信息
        redisTemplate.delete(imageCodeInRedisKey);
    }
}
