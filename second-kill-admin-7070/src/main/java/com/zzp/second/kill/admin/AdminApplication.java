package com.zzp.second.kill.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author 佐斯特勒
 * <p>
 *  人皆知有用之用，而莫知无用之用也
 * </p>
 * @version v1.0.0
 * @date 2020/1/15 9:14
 * @see  AdminApplication
 **/
@SpringBootApplication
@MapperScan(basePackages = "com.zzp.second.kill.admin.mapper")
public class AdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }
}
