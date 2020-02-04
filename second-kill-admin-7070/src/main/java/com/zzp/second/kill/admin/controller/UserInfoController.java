package com.zzp.second.kill.admin.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zzp.second.kill.admin.domain.User;
import com.zzp.second.kill.admin.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 佐斯特勒
 * <p>
 *  用户信息接口
 * </p>
 * @version v1.0.0
 * @date 2020/1/23 19:33
 * @see  UserInfoController
 **/
@RestController
@RequestMapping("/userInfo")
public class UserInfoController {
    /**
     * 日志操作
     */
    private final static Logger log = LoggerFactory.getLogger(UserInfoController.class);

    /**
     * 用户服务接口
     */
    @Resource
    private UserService userService;

    /**
     * 分页信息类
     * @param page 页数
     * @param size 数据条
     * @return 分页信息
     */
    @PreAuthorize("hasAuthority('userInfo:list')")
    @GetMapping
    public Object list(@RequestParam(required = false, defaultValue = "1") Integer page,
                       @RequestParam(required = false, defaultValue = "10") Integer size) {
        PageHelper.startPage(page, size);
        List<User> list = userService.selectPageData();
        return new PageInfo<>(list);
    }

    /**
     * 编辑
     * @param user .
     * @return .
     */
    @PreAuthorize("hasAuthority('userInfo:edit')")
    @PutMapping
    public Object edit(User user){
        return "edit " + user;
    }


    /**
     * 添加
     * @param user .
     * @return .
     */
    @PreAuthorize("hasAuthority('userInfo:add')")
    @PostMapping
    public Object add(User user) {
        return "add " + user;
    }

    /**
     * 删除
     * @return .
     */
    @PreAuthorize("hasAuthority('userInfo:delete')")
    @DeleteMapping(value="/{id}")
    public Object delete(@PathVariable String id) {
        return "delete " + id;
    }

    /**
     * 导出
     * @return .
     */
    @PreAuthorize("hasAuthority('userInfo:export')")
    @GetMapping(value="/export")
    public Object export() {
        return "export";
    }

    /**
     * 导入要上传文件
     * @return .
     */
    @PreAuthorize("hasAuthority('userInfo:import')")
    @PostMapping(value="/import")
    public Object importData() {
        return "import";
    }
}
