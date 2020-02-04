package org.springframework.security.web.authentication;

import com.alibaba.fastjson.JSONObject;
import com.zzp.second.kill.admin.domain.SysUser;
import com.zzp.second.kill.commons.utils.RsaUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.attribute.UserPrincipalNotFoundException;

/**
 * @author 佐斯特勒
 * 用户名密码认证过滤器
 * <p>
 * 重写用户名与密码过滤器，来使认证按照自定义规则进行
 * </p>
 * @version v1.0.0
 * @date 2020/1/21 22:43
 * @see UsernamePasswordAuthenticationFilter
 **/
public class UsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    /**
     * 私钥
     */
    private String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAICGWJnH/6gsAYAZ4GRdbQrwWmL5" +
            "2AfwXh8B19KwOih+Ux68jtTbvJnc7tDYEncSbuMpMy6CslrySrXP1PbjP3q6nj4CpwXppUFtAcY4" +
            "MlfCiO+1QczaN/rJIBPLPRtTGPKqJd6Kba5oFDs+ScYSFFNxgyD+vVJ3LAhWji4ZWKinAgMBAAEC" +
            "gYAROnEMBCw4DdYRpuZWbdOdIMTs+ZLE1aq4O1YD7w2MugpIV3xJ+j6Y3W3B3wTyoyzsFGzA8lIY" +
            "MNxk7LwecC3lA7c+wImW/7IQxNi8epuIzKHO2JsreQ4NT6vfRmsUlr6QcvLzKIxQgcvjHztj6Z8l" +
            "N4Ci/03GPOYGXmvVkZrIUQJBAM1slqba4ZXieNsz2EnYCNzQa3cYCu94YC69wNa4+pB3IhQH440b" +
            "OQvwE8SBkfafDDXmwVJk1JX6XO8KZE4SfP8CQQCgKveH2RgFhDv4A80FdfrZ3zsAH1Km1eo5IvlW" +
            "4mXFMSbT2riI1Vs3tDpElecWnnzcnedTCVSCc0lN3Xu2sMxZAkEAsuQNjeCu8sf2V24evK/Vh/Y4" +
            "n83gOAqsOAgnFtaf3Y7hrm/wScGbPDol/MqZQhvfllENqaMFPlZ49/Ikx5hyIQJAHhZUL2G5oHep" +
            "qDpWjhsOh1TmyWwY45w1howqWIbo5TUJfSyOE644l2s0suOR6G7XN7410SRkiqjLcWQUtEnb6QJA" +
            "BkBVrN234KShy1GG6ZUqoq7Kyi+6uKTzUS3mCO+fMMH8wduGt3vQ6SISt2Hs8ZzNtAsvqxD5EIOD" +
            "CZL8QkxHQQ==";

    public static final String SPRING_SECURITY_FORM_USERNAME_KEY = "username";
    public static final String SPRING_SECURITY_FORM_PASSWORD_KEY = "password";

    private String usernameParameter = SPRING_SECURITY_FORM_USERNAME_KEY;
    private String passwordParameter = SPRING_SECURITY_FORM_PASSWORD_KEY;
    private boolean postOnly = true;

    public UsernamePasswordAuthenticationFilter() {
        super(new AntPathRequestMatcher("/login", "POST"));
    }

    /**
     * 尝试认证方式
     *
     * @param request  “”
     * @param response ‘’
     * @return ‘’ {@link Authentication}
     * @throws AuthenticationException ''
     * @throws IOException             ''
     * @throws ServletException        ''
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (postOnly && !"POST".equals(request.getMethod())) {
            throw new AuthenticationServiceException(
                    "不支持此认证请求方法: " + request.getMethod());
        }
        // 获取签名
        var sign = request.getParameter("sign");
        // 原生数据
        String raw = null;
        // 获取解密数据
        try {
            raw = RsaUtils.decrypt(sign, privateKey);
        } catch (Exception e) {
            throw new UserPrincipalNotFoundException("用户名或密码错误！");
        }
        // 解析成SysUser
        var user = JSONObject.parseObject(raw, SysUser.class);
        // 获取用户名和密码
        var username = user.getUsername();
        var password = user.getPassword();
        // 设置请求key与username
        request.setAttribute("key", user.getKey());
        request.setAttribute("username", username);

        // 处理null值
        if (username == null){
            username = "";
        }

        if (password == null){
            password = "";
        }

        username = username.trim();

        // 认证令牌
        var token = new UsernamePasswordAuthenticationToken(username, password);

        // 允许子类设置详情
        setDetails(request,token);

        return this.getAuthenticationManager().authenticate(token);
    }

    /**
     * 提供方便子类设置详情内容
     * @param request 正在为其创建身份验证请求
     * @param token 应当具有其详细信息的身份验证请求对象集
     */
    private void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken token) {
        token.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    /**
     * 设置将被用于获得从登录请求的用户名参数名称
     * @param usernameParameter 参数名称。默认为“用户名”.
     */
    public void setUsernameParameter(String usernameParameter) {
        Assert.hasText(usernameParameter, "用户名不能为空或者null");
        this.usernameParameter = usernameParameter;
    }

    /**
     * 设置将被用于获得从登录请求中的密码参数名..
     * @param passwordParameter 默认为“密码.
     */
    public void setPasswordParameter(String passwordParameter) {
        Assert.hasText(passwordParameter, "密码不能为空或者null");
        this.passwordParameter = passwordParameter;
    }

    /**
     * 定义是否只是HTTP POST请求将被该过滤器允许的。
     * 如果设置为true，并在收到认证请求这是不是一个POST请求，异常会立即提出和验证将不会尝试。
     * 该unsuccessfulAuthentication（）方法将被称为如果处理失败的认证。
     * 默认为真 ，但可以由子类覆盖
     */
    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    public final String getUsernameParameter() {
        return usernameParameter;
    }

    public final String getPasswordParameter() {
        return passwordParameter;
    }
}
