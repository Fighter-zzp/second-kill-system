package com.zzp.second.kill.admin.handler;

import com.alibaba.fastjson.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 佐斯特勒
 * <p>
 *  认证失败操作类
 * </p>
 * @version v1.0.0
 * @date 2020/1/21 23:53
 * @see  CustomizeAuthenticationFailureHandler
 **/
@Component
public class CustomizeAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        Map<String, Object> map = new HashMap<>();
        map.put("code", -1);
        map.put("msg", exception.getMessage());

        PrintWriter writer = response.getWriter();

        writer.write(JSONObject.toJSONString(map));
        writer.flush();
        writer.close();
    }
}
