<template>
    <el-container>
        <el-header>Scarborough企业</el-header>
        <el-container>
            <el-aside>
                <el-menu router>
                    <tree-menu :menus="menus"></tree-menu>
                </el-menu>
            </el-aside>
            <el-main>
                <router-view></router-view>
            </el-main>
        </el-container>
    </el-container>
</template>

<script>
    import {mapState} from 'vuex'
    import TreeMenu from "../components/TreeMenu";

    export default {
        name: "Index",
        components: {
            TreeMenu
        },
        computed: {
            ...mapState(['menus'])
        },
    }
</script>

<style scoped>
    .el-header {
        background-color: #409EFF;
        color: #ffffff;
        line-height: 60px;
        font-size: 28px;
    }

    .el-aside {
        background-color: #e3e3e3;
        width: 200px !important;
    }

    body .el-table th.gutter {
        display: table-cell !important;
    }

    body .el-table colgroup.gutter {
        display: table-cell !important;
    }

</style>
