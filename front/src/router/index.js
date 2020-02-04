import Vue from 'vue'
import Router from 'vue-router'
import VueCookies from 'vue-cookies'
import store from '../store'
import utils from '../utils/index'

const Login = () => import("../views/Login")

Vue.use(Router)

const router = new Router({
    mode: 'history',

    routes: [
        {
            path: '/login',
            component: Login
        }
    ]
})

// 全局路由守卫
router.beforeEach((to,from,next)=>{
    // 1. 如果用户是到登录页面直接放其过
    // 2.用户登录过，也让其过，因为用户如果登录过 VueCookies.set('token', token);
    if(to.path.indexOf('/login') > -1) {
        next();  //直接放其过
    }else if(VueCookies.get('token')) {
        // 如果vuex中state.menus中有数据，表示用户没有刷新页面，同时动态路由信息也存在
        if(store.state.menus) {
            next();
        }else {// 没有数据表示用户是通过刷新页面的方式来实现的，所以需要从本地

            store.state.menus = JSON.parse(window.localStorage.getItem("dynamic_menus"));//重新设置菜单信息

            let routers = JSON.parse(window.localStorage.getItem("dynamic_routers"))

            utils.parseRouterComponent(routers);  //将组件进行解析

            router.addRoutes(routers)

            // 必须使用 next(to) 让其重新跳转一次，否则无效
            next(to);
        }
    }else {
        next({path: '/login'});
    }
})

export default router;
