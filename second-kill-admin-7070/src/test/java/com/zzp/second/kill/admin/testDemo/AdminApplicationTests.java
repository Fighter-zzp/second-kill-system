package com.zzp.second.kill.admin.testDemo;

import com.zzp.second.kill.admin.mapper.SysUserMapper;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;

@SpringBootTest
public class AdminApplicationTests {
    @Test
    void test1(){
        System.out.println("今天的日期" + LocalDate.now());
    }

    @Resource
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    void test2(){
        System.out.println(passwordEncoder.encode("123456"));
    }

    @Resource
    private SysUserMapper sysUserMapper;
    @Test
    void test3(){
        sysUserMapper.getSysUserInfo("zzp").forEach(System.out::println);
    }
}
