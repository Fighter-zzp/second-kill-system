import Vue from 'vue'

Vue.directive('hasPermission', {
    /**
     * @param el
     * @param binding
     * @param vnode
     */
    inserted: (el, binding, vnode) => {
        /**
         * 在指令绑定的时候： v-has-permission:userinfo:add,userinfo:list
         */
        let bindPermission = binding.arg.split(/,\s+/);

        // 取出后台配置的权限 ["user:list", "user:add", "user:edit"]
        let routeMeta = vnode.context.$route.meta;

        let flag = false;  //假设其为隐藏

        for(let i = 0; i < bindPermission.length; i++) {
            if(routeMeta.indexOf(bindPermission[i]) > -1) {
                flag = true;
                break;
            }
        }

        if(!flag) {
            el.parentNode.removeChild(el);  // 如果没有权限，直接移除
        }
    }
})
