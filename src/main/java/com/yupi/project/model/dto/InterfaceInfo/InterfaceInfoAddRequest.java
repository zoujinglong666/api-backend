package com.yupi.project.model.dto.InterfaceInfo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zou
 */
@Data
public class InterfaceInfoAddRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 描述
     */
    private String description;
    /**
     * 接口地址
     */
    private String url;
    private String requestHeader;
    /**
     * 响应头
     */
    private String reponseHeader;
    /**
     * 请求类型
     */
    private String method;
    private String type;
    /**
     * 名称
     */
    private String name;

}
