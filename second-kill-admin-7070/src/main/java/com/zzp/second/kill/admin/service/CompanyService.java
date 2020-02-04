package com.zzp.second.kill.admin.service;

import com.zzp.second.kill.admin.domain.Company;

import java.util.List;
/**
 * @author 佐斯特勒
 * <p>
 *  公司服务接口
 * </p>
 * @version v1.0.0
 * @date 2020/1/23 19:38
 * @see  CompanyService
 **/
public interface CompanyService{

    /**
     * 获取全部公司信息
     * @return .
     */
    List<Company> getAll();
}
