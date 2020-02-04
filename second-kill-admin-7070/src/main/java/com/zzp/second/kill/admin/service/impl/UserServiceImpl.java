package com.zzp.second.kill.admin.service.impl;

import com.zzp.second.kill.admin.domain.User;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.zzp.second.kill.admin.mapper.UserMapper;
import com.zzp.second.kill.admin.service.UserService;

import java.util.List;
/**
 * @author 佐斯特勒
 * <p>
 *  用户接口实现类
 * </p>
 * @version v1.0.0
 * @date 2020/1/23 19:51
 * @see  UserServiceImpl
 **/
@Service
public class UserServiceImpl implements UserService{

    @Resource
    private UserMapper userMapper;

    @Override
    public List<User> selectPageData() {
        return userMapper.selectAll();
    }
}
