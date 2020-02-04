import Vue from 'vue'
import App from './App.vue'
// 如下两个是网络的请求组件
import VueAxios from "vue-axios";
import axios from 'axios'
// ElmentUI的组件
import ElementUI from 'element-ui'
// ElementUI的样式
import 'element-ui/lib/theme-chalk/index.css'
import router from './router'
import CryptoJS from 'crypto-js'  //aes加密
import store from './store'
import './directive/hasPermission'
import utils from './utils/index'

Vue.use(VueAxios, axios)
Vue.use(ElementUI)
Vue.use(CryptoJS)

/**
 * 全局绑定
 * 使用Vue.prototype.$parseComponent 那么就可以在任意一个组件中
 * this.$parseComponent('company')
 */
Vue.prototype.$parseRouterComponent = utils.parseRouterComponent;

// 基础的url地址
axios.defaults.baseURL = 'http://localhost:7070'

Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: h => h(App),
}).$mount('#app')
