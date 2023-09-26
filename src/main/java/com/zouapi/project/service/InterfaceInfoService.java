package com.zouapi.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zouapi.project.model.entity.InterfaceInfo;

/**
 * @author zou
 * @description 针对表【interface_info(接口信息)】的数据库操作Service
 * @createDate 2023-09-15 22:23:02
 */
public interface InterfaceInfoService extends IService<InterfaceInfo> {
    void valid(InterfaceInfo interfaceInfo, boolean add);

}
