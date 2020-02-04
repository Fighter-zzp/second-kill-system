package com.zzp.second.kill.admin.service.impl;

import com.zzp.second.kill.admin.domain.Company;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.zzp.second.kill.admin.mapper.CompanyMapper;
import com.zzp.second.kill.admin.service.CompanyService;

import java.util.List;

/**
 * @author 佐斯特勒
 * <p>
 *  公司服务接口实现类
 * </p>
 * @version v1.0.0
 * @date 2020/1/23 19:36
 * @see  CompanyServiceImpl
 **/
@Service
public class CompanyServiceImpl implements CompanyService{

    @Resource
    private CompanyMapper companyMapper;

    @Override
    public List<Company> getAll(){
        return companyMapper.selectAll();
    }
}
