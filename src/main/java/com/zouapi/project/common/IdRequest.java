package com.zouapi.project.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 删除请求
 *
 * @author yupi
 */
@Data
public class IdRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    private Long id;
}