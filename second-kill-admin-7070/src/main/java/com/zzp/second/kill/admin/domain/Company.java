package com.zzp.second.kill.admin.domain;

import java.io.Serializable;
import javax.persistence.*;
import lombok.Data;

@Data
@Table(name = "company")
public class Company implements Serializable {
    private static final long serialVersionUID = 7225342001715852575L;
    @Column(name = "id")
    private Integer id;

    @Column(name = "`name`")
    private String name;

    @Column(name = "parentId")
    private Integer parentid;
}
