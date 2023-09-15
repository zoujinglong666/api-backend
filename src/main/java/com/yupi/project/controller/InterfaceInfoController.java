package com.yupi.project.controller;

import com.yupi.project.common.BaseResponse;
import com.yupi.project.common.ResultUtils;
import com.yupi.project.model.vo.InterfaceInfoVo;
import com.yupi.project.service.InterfaceInfoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/interfaceInfo")
public class InterfaceInfoController {
    @Resource
    InterfaceInfoService interfaceInfoService;

    @PostMapping("/list")
    public BaseResponse<List<InterfaceInfoVo>> listInterfaceInfo() {


        return ResultUtils.success(result);
    }

}
