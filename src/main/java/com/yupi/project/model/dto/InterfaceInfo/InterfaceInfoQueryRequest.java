package com.yupi.project.model.dto.InterfaceInfo;

import com.yupi.project.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zou
 */
@EqualsAndHashCode(callSuper = true)
@Data

public class InterfaceInfoQueryRequest extends PageRequest implements Serializable {

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
     * 接口类型
     */
    private String type;
    /**
     * 接口请求方法
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
