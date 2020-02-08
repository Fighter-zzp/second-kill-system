package com.zzp.second.kill.admin.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
/**
 * @author 佐斯特勒
 * <p>
 *  用户类
 * </p>
 * @version v1.0.0
 * @date 2020/2/8 1:42
 * @see  User
 **/
@Data
@Table(name = "`user`")
public class User implements Serializable {
    private static final long serialVersionUID = -6986730194260557983L;
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    @Column(name = "`name`")
    private String name;

    @Column(name = "`password`")
    private String password;

    @Column(name = "gender")
    private String gender;

    @Column(name = "email")
    private String email;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "birthday")
    private Date birthday;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "createTime")
    private Date createtime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "updateTime")
    private Date updatetime;

    @Column(name = "company_id")
    private Integer companyId;

    private Company company;
}
