package com.zzp.second.kill.admin.controller;

import com.zzp.second.kill.admin.service.CompanyService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

/**
 * @author ZZP
 */
@RequestMapping("/company")
@RestController
public class CompanyController {

    /**
     * 公司服务接口
     */
    @Resource
    private CompanyService companyService;

    /**
     * 展示所有公司接口
     * @return .
     */
    @PreAuthorize("hasAuthority('company:list')")
    @GetMapping
    public Object getAll() {
        return companyService.getAll();
    }
}
