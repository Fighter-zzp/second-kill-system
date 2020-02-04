package com.zzp.second.kill.admin.filter;

import com.zzp.second.kill.admin.grpc.domain.SysUserTokenInfo;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @author 佐斯特勒
 * <p>
 *  前后端分离的token认证
 * </p>
 * @version v1.0.0
 * @date 2020/1/23 0:05
 * @see  FrontTokenAuthenticationFilter
 **/
@Component
public class FrontTokenAuthenticationFilter extends OncePerRequestFilter {
    /**
     * redis模板,用来记录token和数据
     */
    @Resource
    private RedisTemplate<String, byte[]> redisTemplate;

    String tokenHead = "Bearer ";
    String tokenHeader = "Authorization";

    @Resource
    private UserDetailsService userDetailsService;

    /**
     * 对前端token的处理
     * @param req .
     * @param resp .
     * @param filterChain .
     * @throws ServletException .
     * @throws IOException .
     */
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse resp, FilterChain filterChain)
            throws ServletException, IOException {
        // 设置请求头，让其携带Authorization: Bearer xxx
        var authHeader = req.getHeader(this.tokenHeader);

        // 获取其中的token信息
        if (authHeader!=null && authHeader.startsWith(tokenHead)){
            //截取xxx，即token
            final var token = authHeader.substring(tokenHead.length());
            if (Objects.requireNonNull(redisTemplate.hasKey(token))){
                // 查看redis缓存的用户信息
                var sysUserTokenInfo = SysUserTokenInfo.parseFrom(redisTemplate.opsForValue().get(token));
                if (Objects.nonNull(sysUserTokenInfo) && SecurityContextHolder.getContext().getAuthentication() == null){
                    // 用户存进用户权限
                    var authorities = new ArrayList<SimpleGrantedAuthority>();
                    sysUserTokenInfo.getAuthoritiesList().forEach(authority-> authorities.add(new SimpleGrantedAuthority(authority)));

                    var userDetails = new User(sysUserTokenInfo.getUsername(), sysUserTokenInfo.getPasswrod(), authorities);

                    // 用户认证token
                    var authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(
                            req));
                    logger.info("已验证用户： " + sysUserTokenInfo.getUsername() + ", 设置上下文安全");
                    //将用户信息重新设置到上下文
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        filterChain.doFilter(req,resp);
    }
}
