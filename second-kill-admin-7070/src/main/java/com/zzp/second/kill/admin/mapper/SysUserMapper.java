package com.zzp.second.kill.admin.mapper;

import com.zzp.second.kill.admin.domain.SysUser;
import tk.mybatis.mapper.MyMapper;

import java.util.List;
import java.util.Map;

/**
 * @author 佐斯特勒
 * <p>
 *  系统用户Dao接口
 * </p>
 * @version v1.0.0
 * @date 2020/1/21 23:19
 * @see  SysUserMapper
 **/
public interface SysUserMapper extends MyMapper<SysUser> {
    /**
     * 获取系统用户信息
     * @param username 用户名
     * @return 用户下的信息 字段名：对象
     */
    List<Map<String, Object>> getSysUserInfo(String username);
}
