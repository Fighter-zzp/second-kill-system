const parsePath = function(path) {
    return resolve => {
        require.ensure([], (require) => {
            resolve(require('@/views/' + path + '.vue'))
        })
    }
}

const parseRouterComponent = function (routers) {
    if(routers && routers.length > 0) {
        routers.forEach(r => {
            r['component'] = parsePath(r.component);
            if(r.children && r.children.length > 0) {
                parseRouterComponent(r.children);
            }
        })
    }
}

export default {parsePath, parseRouterComponent}
