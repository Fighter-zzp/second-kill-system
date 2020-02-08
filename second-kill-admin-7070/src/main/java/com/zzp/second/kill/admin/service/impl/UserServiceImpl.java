package com.zzp.second.kill.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.zzp.second.kill.admin.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.zzp.second.kill.admin.mapper.UserMapper;
import com.zzp.second.kill.admin.service.UserService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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
@Slf4j
public class UserServiceImpl implements UserService{

    @Resource
    private UserMapper userMapper;

    @Override
    public List<User> selectPageData() {
        return userMapper.selectAll();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer delete(Integer id) {
        log.info("Id:{}用户被删除",id);
        return userMapper.deleteByPrimaryKey(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer save(User user) {
        user.setCreatetime(new Date());
        user.setUpdatetime(new Date());
        log.info("添加用户:{}成功", user.getName());
        return userMapper.insert(user);
    }

    @Override
    public User findById(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer edit(User user) {
        user.setUpdatetime(new Date());
        log.info("编辑id:{},用户:{}成功", user.getId(),user.getName());
        return userMapper.updateByPrimaryKeySelective(user);
    }
}
