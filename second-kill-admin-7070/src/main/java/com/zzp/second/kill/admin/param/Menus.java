package com.zzp.second.kill.admin.param;

import lombok.Data;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
/**
 * @author 佐斯特勒
 * <p>
 *  菜单参数类
 * </p>
 * @version v1.0.0
 * @date 2020/1/22 0:00
 * @see  Menus
 **/
@Data
public class Menus {
    private Integer id;
    private String title;
    private String url;
    private Integer pid;
    private String icon;
    /**
     * 子菜单
     */
    private Set<Menus> childMenus = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Menus menus = (Menus) o;
        return id.equals(menus.id) &&
                title.equals(menus.title) &&
                url.equals(menus.url) &&
                pid.equals(menus.pid) &&
                icon.equals(menus.icon) &&
                childMenus.equals(menus.childMenus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, url, pid, icon, childMenus);
    }
}
