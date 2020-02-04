import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

const state = {
    // 用户登录解密用户信息的公钥
    loginPublicKey: 'MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCAhliZx/+oLAGAGeBkXW0K8Fpi+dgH8F4fAdfSsDooflMevI7U27yZ3O7Q2BJ3Em7jKTMugrJa8kq1z9T24z96up4+AqcF6aVBbQHGODJXwojvtUHM2jf6ySATyz0bUxjyqiXeim2uaBQ7PknGEhRTcYMg/r1SdywIVo4uGViopwIDAQAB',
    menus: '',   //登录成功之后设置的数据
}

const mutations = {
    setMenus(state, value) {
        state.menus = value;
    }
}

const actions = {
    setMenus(context, value) {
        context.commit('setMenus', value);
    }
}

const getters = {

}

export default new Vuex.Store({
    state,
    mutations,
    actions,
    getters
})
