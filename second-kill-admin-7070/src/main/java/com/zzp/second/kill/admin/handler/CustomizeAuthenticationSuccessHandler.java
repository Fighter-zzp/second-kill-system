package com.zzp.second.kill.admin.handler;

import com.alibaba.fastjson.JSONObject;
import com.zzp.second.kill.admin.param.LoginSuccessParam;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 佐斯特勒
 * <p>
 *  成功登录操作类
 * </p>
 * @version v1.0.0
 * @date 2020/1/22 0:05
 * @see  CustomizeAuthenticationSuccessHandler
 **/
@Component
public class CustomizeAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");

        /*
         * 在 UsernamePasswordAuthenticationFilter 设置key
         * 获取前端传入过来的对称加密的key
         */
        var key = (String)request.getAttribute("key");

        // 在 UserAuthentication 中设置的
        var loginSuccessInfo = (LoginSuccessParam)request.getAttribute("loginSuccessInfo");
        //用户的token
        var statusToken = (String)request.getAttribute("statusToken");

        loginSuccessInfo.setMsg("登录成功");
        loginSuccessInfo.setCode(1);

        // 数据的安全传输
        var username = (String)request.getAttribute("username");

        // 将用户名做两次md5
        loginSuccessInfo.setKey(DigestUtils.md5DigestAsHex(DigestUtils.md5Digest(username.getBytes())));
        //状态token
        loginSuccessInfo.setToken(statusToken);

        var writer = response.getWriter();

        writer.write(JSONObject.toJSONString(loginSuccessInfo));
        writer.flush();
        writer.close();
    }
}
