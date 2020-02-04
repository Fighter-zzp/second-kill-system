package com.zzp.second.kill.admin.param;

import lombok.Data;

import java.util.Set;
/**
 * @author 佐斯特勒
 * <p>
 *  成功登录信息类
 * </p>
 * @version v1.0.0
 * @date 2020/1/22 0:03
 * @see  LoginSuccessParam
 **/
@Data
public class LoginSuccessParam {
    private Integer code;
    private String msg;
    /**
     * 用户令牌
     */
    private String token;
    /**
     * 返回前端的key
     */
    private String key;

    /**
     * 菜单信息
     */
    private Set<Menus> menus;

    /**
     * 动态路由信息
     */
    private Set<Router> routers;
}
