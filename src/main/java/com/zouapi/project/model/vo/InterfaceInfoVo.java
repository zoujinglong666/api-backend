package com.zouapi.project.model.vo;

import com.zouapi.project.model.entity.InterfaceInfo;
import lombok.Data;

import java.util.Date;

@Data
public class InterfaceInfoVo extends InterfaceInfo {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    private Long id;
    /**
     * 接口名称
     */
    private String name;
    /**
     * 账号
     */
    private String type;
    /**
     * 用户头像
     */
    private String method;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
}
