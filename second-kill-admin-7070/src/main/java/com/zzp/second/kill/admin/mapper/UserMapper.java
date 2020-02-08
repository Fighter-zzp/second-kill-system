package com.zzp.second.kill.admin.mapper;

import com.zzp.second.kill.admin.domain.User;
import com.zzp.second.kill.commons.dto.UserExcelDto;
import tk.mybatis.mapper.MyMapper;

import java.util.List;

public interface UserMapper extends MyMapper<User> {
    /**
     * userDto数据
     * @return .
     */
    List<User> selectUsers();
}
