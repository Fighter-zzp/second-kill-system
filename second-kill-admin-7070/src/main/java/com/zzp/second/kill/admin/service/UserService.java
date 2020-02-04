package com.zzp.second.kill.admin.service;

import com.zzp.second.kill.admin.domain.User;

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

}
