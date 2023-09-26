package com.zouapi.project.model.dto.InterfaceInfo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zou
 */
@Data
public class InterfaceInfoInvokeRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    private String userRequestParams;
    private Long id;


}
