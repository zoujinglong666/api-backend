package com.yupi.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yupi.project.common.BaseResponse;
import com.yupi.project.common.ErrorCode;
import com.yupi.project.common.ResultUtils;
import com.yupi.project.exception.BusinessException;
import com.yupi.project.model.dto.InterfaceInfo.InterfaceInfoAddRequest;
import com.yupi.project.model.dto.InterfaceInfo.InterfaceInfoQueryRequest;
import com.yupi.project.model.entity.InterfaceInfo;
import com.yupi.project.model.entity.User;
import com.yupi.project.model.vo.InterfaceInfoVo;
import com.yupi.project.service.InterfaceInfoService;
import com.yupi.project.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zou
 */
@RestController
@RequestMapping("/interfaceInfo")
public class InterfaceInfoController {
    @Resource
    InterfaceInfoService interfaceInfoService;
    @Resource
    UserService userService;

    @GetMapping("/list")
    public BaseResponse<List<InterfaceInfoVo>> listInterfaceInfo(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        InterfaceInfo interfaceInfoQuery = new InterfaceInfo();
        if (interfaceInfoQueryRequest != null) {
            BeanUtils.copyProperties(interfaceInfoQueryRequest, interfaceInfoQuery);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(interfaceInfoQuery);
        List<InterfaceInfo> interfaceInfoList = interfaceInfoService.list(queryWrapper);
        System.out.println(interfaceInfoList);
        List<InterfaceInfoVo> interfaceInfoVoList = interfaceInfoList.stream().map(interfaceInfo -> {
            InterfaceInfoVo interfaceInfoVo = new InterfaceInfoVo();
            BeanUtils.copyProperties(interfaceInfo, interfaceInfoVo);
            return interfaceInfoVo;
        }).collect(Collectors.toList());
        return ResultUtils.success(interfaceInfoVoList);
    }

    @PostMapping("/add")
    public BaseResponse<Long> addInterfaceInfo(@RequestBody InterfaceInfoAddRequest interfaceInfoAddRequest, HttpServletRequest request) {
        if (interfaceInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoAddRequest, interfaceInfo);
        // 校验
        interfaceInfoService.valid(interfaceInfo, true);
        User loginUser = userService.getLoginUser(request);
        interfaceInfo.setUserid(loginUser.getId());
        boolean result = interfaceInfoService.save(interfaceInfo);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        long newInterfaceInfoId = interfaceInfo.getId();
        return ResultUtils.success(newInterfaceInfoId);
    }


}
