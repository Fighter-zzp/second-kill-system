package com.zzp.second.kill.admin.param;

import lombok.Data;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
/**
 * @author 佐斯特勒
 * <p>
 *  动态路由参数类
 * </p>
 * @version v1.0.0
 * @date 2020/1/22 0:00
 * @see  Router
 **/
@Data
public class Router {
    private String path;
    private String component;
    private Set<String> meta;
    private Set<Router> children = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Router router = (Router) o;
        return path.equals(router.path) &&
                component.equals(router.component) &&
                meta.equals(router.meta) &&
                children.equals(router.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, component, meta, children);
    }
}
