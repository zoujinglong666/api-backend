package com.yupi.project.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 接口信息
 *
 * @TableName interface_info
 */
@TableName(value = "interface_info")
@Data
public class InterfaceInfo implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 创建时间
     */
    private Date createtime;
    /**
     * 更新时间
     */
    private Date updatetime;
    /**
     * 是否删除(0-未删, 1-已删)
     */
    private Integer idelete;
    /**
     * 请求头
     */
    private String requestheader;
    /**
     * 用户名
     */
    private String name;
    /**
     * 用户名
     */
    private String url;
    /**
     * 用户名
     */
    private String method;
    /**
     * 响应头
     */
    private String reponseheader;
    /**
     * 接口状态
     */
    private Integer status;
    /**
     *
     */
    private Long userid;
}