<template>
    <el-container>
        <el-main>
            <el-row>
                <el-col :span="12" :offset="6">
                    <el-card class="box-card">
                        <div slot="header" class="clearfix">
                            <span>用户登录</span>
                        </div>
                        <div>
                            <el-form ref="loginInfo" label-width="80px">
                                <el-form-item label="用户名">
                                    <el-input v-model="loginInfo.username"></el-input>
                                </el-form-item>
                                <el-form-item label="密码">
                                    <el-input v-model="loginInfo.password"></el-input>
                                </el-form-item>
                                <el-form-item label="验证码">
                                    <el-input v-model="loginInfo.imageCode" style="width: 80%; vertical-align: top;"></el-input>
                                    <el-image :src="imageData" style="width: 18%; vertical-align: top; margin-left: 3px"></el-image>
                                </el-form-item>
                                <el-form-item>
                                    <el-button @click.prevent="login" type="primary">登录</el-button>
                                </el-form-item>
                            </el-form>
                        </div>
                    </el-card>
                </el-col>
            </el-row>
        </el-main>
    </el-container>
</template>

<script>
    import VueCookies from 'vue-cookies'
    import JSEncrypt from 'jsencrypt'  //非对称加密
    import stringRandom from 'string-random'  //生成随机码
    import {mapState, mapActions} from 'vuex'

    export default {
        name: "Login",
        data() {
            return {
                imageCodeKey: '',
                imageCodeBase64: '',
                imageData: '',
                loginInfo: {
                    username: '',
                    password: '',
                    imageCode: ''
                }
            }
        },
        computed: {
            ...mapState(['loginPublicKey'])  // 引入公钥
        },
        mounted() {

            this.axios.get("/imageCode")
                .then(resp => {
                    this.imageCodeBase64 = resp.data.data;
                    this.imageData = 'data:image/JPEG;base64,' + this.imageCodeBase64;
                    //获取redis中存储的图片验证的key
                    this.imageCodeKey = resp.data.imageCodeKey;
                });
        },
        methods: {
            ...mapActions(['setMenus']),
            login() {
                // 生成长度为16位的随机码，包含数据
                let key = stringRandom(16, {numbers: true});

                let rsa = new JSEncrypt();  //公钥加密的对象

                rsa.setPublicKey(this.loginPublicKey);  //设置公钥

                // 使用公钥加密
                let encryptData = rsa.encrypt(JSON.stringify({
                    username: this.loginInfo.username,
                    password: this.loginInfo.password,
                    key: key
                }));

                this.axios({
                    url: "/login",
                    method: "POST",
                    params: {sign: encryptData, imageCodeKey: this.imageCodeKey, imageCode: this.loginInfo.imageCode},
                }).then(resp => {
                    let resultData = resp.data;
                    if(resultData.code > 0) {
                        let routers = resultData.routers;

                        /**
                         * 不能将解析后的路由信息存入到本地，因为通过JSON.stringify解析后的会将 component组件排除掉
                         */
                        window.localStorage.setItem("dynamic_routers", JSON.stringify(routers));

                        this.$parseRouterComponent(routers);  //解析路由，路由为嵌套路由

                        this.$router.addRoutes(routers);

                        this.setMenus(resultData.menus);

                        // 将菜单信息存储到sessionStorage中，如果存储到vuex中，当用户刷新会重置vuex中的数据
                        window.localStorage.setItem("dynamic_menus", JSON.stringify(resultData.menus))

                        VueCookies.set('token', resultData.token);

                        this.$router.push({path: '/'})
                    }
                })
            }
        }
    }
</script>

<style scoped>
    .el-image {
        width: 18%;
        vertical-align: top;
        margin-left: 3px;
        height: 40px;
    }
</style>
