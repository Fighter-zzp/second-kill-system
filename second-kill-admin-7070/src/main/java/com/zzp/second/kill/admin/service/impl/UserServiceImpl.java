package com.zzp.second.kill.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.zzp.second.kill.admin.domain.User;
import com.zzp.second.kill.commons.dto.UserExcelDto;
import com.zzp.second.kill.commons.utils.ExcelUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.zzp.second.kill.admin.mapper.UserMapper;
import com.zzp.second.kill.admin.service.UserService;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
        return userMapper.selectUsers();
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

    @Override
    public void export(HttpServletResponse response) throws IOException {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            var users = userMapper.selectUsers();
            var userDtos = new ArrayList<UserExcelDto>();
            for (int i = 0; i <users.size(); i++) {
                var user = users.get(i);
                var userDto = new UserExcelDto();
                BeanUtils.copyProperties(user,userDto);
                userDto.setCompanyName(user.getCompany().getName());
                var gender = "M".equals(user.getGender()) ? "男" : "女";
                userDto.setGender(gender);
                userDto.setNum(++i);
                userDtos.add(userDto);
            }
            // 这里需要设置不关闭流
            ExcelUtils.writeFile(response.getOutputStream(), UserExcelDto.class,userDtos);
        } catch (Exception e) {
            e.printStackTrace();
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            var map = new HashMap<String, String>(2);
            map.put("code", "-1");
            map.put("msg", "下载文件失败");
            response.getWriter().println(JSON.toJSONString(map));
        }
    }
}
