package com.zzp.second.kill.commons.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 佐斯特勒
 * <p>
 *  用户dto
 * </p>
 * @version v1.0.0
 * @date 2020/2/8 12:58
 * @see  UserExcelDto
 **/
@Getter@Setter
@NoArgsConstructor
@ToString
public class UserExcelDto implements Serializable {
    private static final long serialVersionUID = -1545360655792731932L;
    /**
     * 用户编号
     */
    @ExcelProperty({"用户列表","编号"})
    private Integer num;

    /**
     * 姓名
     */
    @ExcelProperty({"用户列表","姓名"})
    private String name;

    /**
     * 性别
     */
    @ExcelProperty({"用户列表","性别"})
    private String gender;

    /**
     * 邮件
     */
    @ExcelProperty({"用户列表","邮件"})
    private String email;

    /**
     * 生日
     */
    @DateTimeFormat("yyyy-MM-dd")
    @ExcelProperty({"用户列表","生日"})
    private Date birthday;

    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ExcelProperty({"用户列表","创建时间"})
    private Date createtime;

    /**
     * 公司名
     */
    @ExcelProperty({"用户列表","公司名"})
    private String companyName;
}
