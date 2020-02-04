<template>
    <div>
        <div v-for="menu in menus" :key="menu.id">
            <!-- 表示有子菜单 -->
            <el-submenu v-if="menu.childMenus && menu.childMenus.length > 0" :index="menu.title">
                <template slot="title"><i :class="menu.icon"></i>{{menu.title}}</template>
                <tree-menu :menus="menu.childMenus"></tree-menu>
            </el-submenu>

            <!-- 菜单这边，ElementUI将Vue中route的 to 属性，使用index来代替
              没有子菜单，就直接渲染
            -->
            <el-menu-item v-else :index="menu.url"><i :class="menu.icon"></i>{{menu.title}}</el-menu-item>
        </div>
    </div>
</template>

username:
password:

<script>
    export default {
        name: "TreeMenu",
        props: {
            menus: Array
        }
    }
</script>

<style scoped>

</style>
