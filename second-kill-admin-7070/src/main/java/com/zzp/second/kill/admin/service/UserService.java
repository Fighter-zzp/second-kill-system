package com.zzp.second.kill.admin.service;

import com.zzp.second.kill.admin.domain.User;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author 佐斯特勒
 * <p>
 *  用户接口
 * </p>
 * @version v1.0.0
 * @date 2020/1/23 19:47
 * @see  UserService
 **/
public interface UserService{

    /**
     * 查询分页后的数据
     * @return 用户集合
     */
    List<User> selectPageData();

    /**
     * 更加id删除用户
     * @param id 用户id
     * @return 删除后的返回值
     */
    Integer delete(Integer id);

    /**
     * 保存添加用户
     * @param user 用户
     * @return 添加后的返回条数
     */
    Integer save(User user);

    /**
     * 根据id查询用户
     * @param id 用户id
     * @return 用户
     */
    User findById(Integer id);

    /**
     * 编辑用户
     * @param user 更新的数据
     * @return 编辑后的返回条数
     */
    Integer edit(User user);

    /**
     * 导出Excel
     * @param resp .
     */
    void export(HttpServletResponse resp) throws IOException;
}
