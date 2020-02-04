package com.zzp.second.kill.admin.service;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Sets;
import com.zzp.second.kill.admin.domain.SysUser;
import com.zzp.second.kill.admin.grpc.domain.SysUserTokenInfo;
import com.zzp.second.kill.admin.mapper.SysUserMapper;
import com.zzp.second.kill.admin.param.LoginSuccessParam;
import com.zzp.second.kill.admin.param.Menus;
import com.zzp.second.kill.admin.param.Router;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author 佐斯特勒
 * <p>
 * 用户认证服务
 * </p>
 * @version v1.0.0
 * @date 2020/1/23 0:10
 * @see UserAuthenticationServiceImpl
 **/
@Service
public class UserAuthenticationServiceImpl implements UserDetailsService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private RedisTemplate<String, byte[]> redisTemplate;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var userInfo = sysUserMapper.getSysUserInfo(username);
        if (Objects.isNull(userInfo) || userInfo.isEmpty()) {
            throw new UsernameNotFoundException("用户名和密码错误");
        }

        // 用来封装用户名和密a码
        var sysUser = new SysUser();

        // 用户权限集合
        var authorities = new ArrayList<String>();

        // 生成要返回的数据
        var loginSuccessInfo = generateData(userInfo, sysUser, authorities);

        // 设置数据，使用protobuf序列化，存储到Redis中
        var sysUserTokenInfo = SysUserTokenInfo.newBuilder().setUsername(username)
                .setPasswrod(sysUser.getPassword()).addAllAuthorities(authorities).build();
        //用于标识用户状态的token
        var statusToken = UUID.randomUUID().toString();
        // 用户信息存在redis缓存里
        redisTemplate.opsForValue().set(statusToken,sysUserTokenInfo.toByteArray(),30, TimeUnit.MINUTES);

        // 数据转成json，然后由前端传的key再对称加密返回过去
        Objects.requireNonNull(RequestContextHolder.getRequestAttributes()).setAttribute("loginSuccessInfo", loginSuccessInfo, 0);
        // 同样的返回token
        RequestContextHolder.getRequestAttributes().setAttribute("statusToken", statusToken, 0);

        // 设置权限
        var grantedAuthorities = new ArrayList<SimpleGrantedAuthority>();
        authorities.forEach(authority-> grantedAuthorities.add(new SimpleGrantedAuthority(authority)));
        return new User(username, sysUser.getPassword(), grantedAuthorities);
    }

    /**
     * 生成成功登录数据
     *
     * @param userInfo    用户信息
     * @param sysUser     系统用户 ---封装用户名和密码
     * @param authorities 用户权限
     * @return 用户成功登录参数
     */
    private LoginSuccessParam generateData(List<Map<String, Object>> userInfo, SysUser sysUser, ArrayList<String> authorities) {
        var loginSuccessParam = new LoginSuccessParam();

        // 获取用户的用户名和密码
        var map = userInfo.get(0);
        // 设置到user里
        sysUser.setPassword((String)map.get("password"));
        sysUser.setUsername((String)map.get("username"));

        // LinkedHashMultimap 允许key值重复，通过key把value组合成一个数组
        LinkedHashMultimap<String, String> multimap = LinkedHashMultimap.create();

        // 存储所以菜单
        var menuSet = new HashSet<Menus>();

        // 处理用户信息的到菜单集合和路由信息
        userInfo.forEach(user -> {
            var menusName = (String) user.get("name"); //获取菜单名
            var icon = (String) user.get("icon"); // 获取菜单的icon
            var url = null == user.get("url") ? "" : (String) user.get("url"); //获取路由和菜单的url
            var id = (Integer) user.get("id"); //菜单id
            var pid = (Integer) user.get("pid"); // 获取菜单的pid
            var component = null == user.get("component") ? "" : (String) user.get("component"); // 获取路由组件
            var operator = null == user.get("operater") ? "" : (String) user.get("operater"); //获取操作权限

            // 用户的操作权限
            if (!StringUtils.isEmpty(operator)) {
                authorities.add(operator);
            }

            // 构建菜单
            var menus = new Menus();
            menus.setIcon(icon);
            menus.setId(id);
            menus.setUrl(url);
            menus.setTitle(menusName);
            menus.setPid(pid);

            // 把用户对应的菜单存进去
            menuSet.add(menus);

            // 动态路由
            if (!StringUtils.isEmpty(component) && !StringUtils.isEmpty(operator)) {
                multimap.put(component + "##" + url, operator);
            }
        });

        // 构建菜单树, menuSet是所有的菜单
        var treeMenus = generateMenuTree(menuSet);
        loginSuccessParam.setMenus(treeMenus);

        // 构建路由树信息
        var rootRouters = generateRoutesInfo(multimap);
        loginSuccessParam.setRouters(rootRouters);

        return loginSuccessParam;
    }

    /**
     * 构架路由信息
     * @param multimap 信息
     * @return 路由集合
     */
    private Set<Router> generateRoutesInfo(LinkedHashMultimap<String, String> multimap) {
        var routerSet = new HashSet<Router>();

        // 根路由
        var rootRouter = new Router();
        rootRouter.setPath("/");
        rootRouter.setComponent("Index");

        // 子路由设置
        var routerChildren = rootRouter.getChildren();
        var routerKeys = multimap.keySet();
        routerKeys.forEach(componentAndPath->{
            var router = new Router();
            // 提取xxx##xxx 内的值，来得到component和path
            var componentAndPathArray = componentAndPath.split("##");

            // 封装它们
            router.setComponent(componentAndPathArray[0]);
            router.setPath(componentAndPathArray[1]);
            router.setMeta(multimap.get(componentAndPath));
            routerChildren.add(router);
        });
        routerSet.add(rootRouter);
        return routerSet;
    }

    /**
     * 构架菜单树
     * @param menuSet 菜单集合
     * @return 树状结构的菜单集合
     */
    private Set<Menus> generateMenuTree(Set<Menus> menuSet) {
        // 封装菜单树
        Set<Menus> treeMenu = new HashSet<>();

        // 该循环遍历拿到所有的根节点
        menuSet.forEach(menu -> {
            // pid如果为0, 表示根组件
            if(0 == menu.getPid()) {
                treeMenu.add(menu);
            }
        });
        // 当循环走完之后，treeMenu中是根组件
        generateMenuTree(menuSet, treeMenu);
        return treeMenu;
    }

    /**
     * 树的根节点构建
     * @param menuSet menu集合
     * @param treeMenu  封装了所有的根节点
     */
    private void generateMenuTree(Set<Menus> menuSet, Set<Menus> treeMenu) {
        treeMenu.forEach(rootMenu -> {
            //拿到id值
            var id = rootMenu.getId();
            menuSet.forEach(menu -> {
                if(id.equals(menu.getPid())) {
                    //设置子节点
                    rootMenu.getChildMenus().add(menu);
                }
            });
            generateMenuTree(menuSet, rootMenu.getChildMenus());
        });
    }
}
