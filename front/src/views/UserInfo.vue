<template>
    <div>
        <el-breadcrumb separator="/">
            <el-breadcrumb-item>管理系统</el-breadcrumb-item>
            <el-breadcrumb-item>用户列表</el-breadcrumb-item>
        </el-breadcrumb>
        <el-button @click="showAddDialog" type="primary" v-has-permission:userInfo:add size="mini">添加</el-button>
        <el-button type="primary" v-has-permission:userInfo:export size="mini">导出</el-button>
        <el-button type="primary" v-has-permission:userInfo:import size="mini">导入</el-button>
        <el-table
                :data="users"
                border
                style="width: 100%">
            <el-table-column
                    align="center"
                    prop="name"
                    label="姓名"
                    width="180">
            </el-table-column>
            <el-table-column
                    prop="gender"
                    :formatter="(row, colum, cellValue) => cellValue == 'F' ? '女' : '男'"
                    label="性别"
                    width="180">
            </el-table-column>
            <el-table-column
                    prop="email"
                    label="邮件">
            </el-table-column>
            <el-table-column
                    prop="birthday"
                    :formatter="formatBirthday"
                    label="生日">
            </el-table-column>
            <el-table-column
                    prop="createtime"
                    :formatter="formatCreatetime"
                    label="创建日期">
            </el-table-column>
            <el-table-column
                    prop="company.name"
                    label="公司名">
            </el-table-column>
            <el-table-column
                    label="操作">
                <template slot-scope="scope">
                    <el-button type="success" v-has-permission:userInfo:edit size="mini"><i class="el-icon-edit"></i> 编辑
                    </el-button>
                    <el-button type="danger" v-has-permission:userInfo:delete
                               @click="delUser(scope.row.id, scope.row.name)" size="mini"><i class="el-icon-delete"></i>
                        删除
                    </el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-pagination
                background
                layout="prev, pager, next"
                @current-change="toPage"
                :total="total">
            <!-- total是总的数据量 -->
        </el-pagination>

        <!-- visible.sync 控制dialog是否显示 --->
        <el-dialog title="添加用户" :visible.sync="showAddUserDialog">
            <!--  rules是表单的规则 -->
            <el-form ref="userModel" :model="userModel" :rules="userRules" label-width="80px">
                <el-form-item label="用户名" prop="name">
                    <el-input v-model="userModel.name"></el-input>
                </el-form-item>
                <el-form-item label="邮箱" prop="email">
                    <el-input v-model="userModel.email"></el-input>
                </el-form-item>
                <el-form-item label="生日" prop="birthday">
                    <el-date-picker type="date" v-model="userModel.birthday"></el-date-picker>
                </el-form-item>
                <el-form-item label="性别" prop="gender">
                    <el-select v-model="userModel.gender">
                        <el-option label="女" value="F"></el-option>
                        <el-option label="男" value="M"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="公司" prop="companyId">
                    <el-select v-model="userModel.companyId">
                        <el-option v-for="com in companys" :label="com.name" :value="com.id" :key="com.id"></el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="showAddUserDialog = false">取 消</el-button>
                <el-button @click="addUser" type="primary">确 定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    // 添加模块
    import VueCookies from 'vue-cookies'
    import moment from 'moment'

    export default {
        name: "UserInfo",
        data() {
            return {
                users: [],
                total: 0,
                currentPage: 1,
                showAddUserDialog: false,
                companys: [],
                userModel: {
                    name: '',
                    email: '',
                    gender: '',
                    birthday: '',
                    companyId: ''
                },
                userRules: {
                    name: [{required: true, message: '用户名不能为空', trigger: 'blur'}],
                    // 邮箱的规则:  以数字、字母、下划线开头, @符号前最低为3位, 最高为16位(中间可以包含数字、字母、下划线、-), 接着就是@
                    // @符号后，点之前是数字或者字母[2,6], 接着是点, 点之后只能是字母(长度为[2,4])
                    email: [{required: true, message: '邮箱不能为空', trigger: 'blur'},
                        {
                            pattern: /^\w[-\w]{2,15}@[a-zA-Z0-9]{2,6}\.[a-zA-Z]{2,4}$/,
                            message: '请填写正确的邮箱格式',
                            trigger: 'blur'
                        }],
                    gender: [{required: true, message: '性别不能为空', trigger: 'change'}],
                    birthday: [{required: true, message: '生日不能为空', trigger: 'blur'}]
                }
            }
        },
        mounted() {
            this.toPage(1)
        },
        methods: {
            // row指一行的数据, cellValue指的是格子的数据
            formatGender(row, column, cellValue) {
                return cellValue == 'F' ? '女' : '男';
            },
            formatBirthday(row, column, cellValue) {
                return moment(cellValue).format('YYYY-MM-DD');
            },
            formatCreatetime(row, column, cellValue) {
                return moment(cellValue).format('YYYY-MM-DD HH:mm:ss');
            },
            toPage(val) {
                //window.console.log(val);
                this.axios.get('/userInfo?page=' + val + "&size=10", {
                    headers: {
                        "Authorization": "Bearer " + VueCookies.get('token')
                    }
                })
                    .then(resp => {
                        // resp.data才是实际的数据本身
                        this.users = resp.data.list;
                        this.total = resp.data.total;
                        this.currentPage = resp.data.pageNum;
                        // window.console.log(this.users);
                    })
            },
            delUser(id, name) {
                this.$confirm('是否删除【' + name + '】的数据?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {  //当用户点击确认后执行的逻辑
                    this.axios.delete("/user/" + id, {
                        headers: {
                            "Authorization": "Bearer " + VueCookies.get('token')
                        }
                    })
                        .then(resp => {
                            let code = resp.data.code;
                            if (code > 0) {
                                this.toPage(this.currentPage); //重新加载当前页的数据
                            } else {
                                this.$notify({
                                    title: '提示',
                                    message: '删除失败，请联系管理员'
                                });
                            }
                        });
                });
            },
            showAddDialog() {
                this.axios.get("/company", {
                    headers: {
                        "Authorization": "Bearer " + VueCookies.get('token')
                    }
                })
                    .then(resp => {
                        this.companys = resp.data;
                        this.showAddUserDialog = true;
                    })
            },
            // 添加数据
            addUser() {
                // valid的值就是true或者false
                this.$refs.userModel.validate(valid => {
                    if (valid) {
                        this.axios.post('/userInfo', this.userModel)
                            .then(resp => {
                                let code = resp.data.code;
                                if (code > 0) {
                                    this.showAddUserDialog = false;
                                    this.toPage(this.currentPage); //重新加载当前页的数据
                                } else {
                                    this.$notify({
                                        title: '提示',
                                        message: '添加失败，请联系管理员'
                                    });
                                }
                            })
                    }
                })
            }
        }
    }
</script>

<style scoped>
    .el-pagination {
        text-align: center;
        margin-top: 8px;
    }

    .el-breadcrumb {
        margin-bottom: 8px;
    }

    .el-table {
        margin-top: 5px;
    }

    .tableContent {
        padding: 20px !important;
        background: #ffffff;
        margin-top: 20px;
    }
</style>
