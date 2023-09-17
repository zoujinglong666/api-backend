package com.yupi.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yupi.project.common.BaseResponse;
import com.yupi.project.common.DeleteRequest;
import com.yupi.project.common.ErrorCode;
import com.yupi.project.common.ResultUtils;
import com.yupi.project.exception.BusinessException;
import com.yupi.project.model.dto.InterfaceInfo.InterfaceInfoAddRequest;
import com.yupi.project.model.dto.InterfaceInfo.InterfaceInfoQueryRequest;
import com.yupi.project.model.dto.InterfaceInfo.InterfaceInfoUpdateRequest;
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
        User loginUser = getLoginUser(request);
        interfaceInfo.setUserid(loginUser.getId());
        boolean result = interfaceInfoService.save(interfaceInfo);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        long newInterfaceInfoId = interfaceInfo.getId();
        return ResultUtils.success(newInterfaceInfoId);
    }

    private User getLoginUser(HttpServletRequest request) {
        return userService.getLoginUser(request);
    }

    @PostMapping("/update")
    public BaseResponse<Long> addInterfaceInfo(@RequestBody InterfaceInfoUpdateRequest interfaceInfoUpdateRequest, HttpServletRequest request) {
        if (interfaceInfoUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoUpdateRequest, interfaceInfo);
        // 校验

        Long oldId = interfaceInfoUpdateRequest.getId();
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(oldId);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = oldInterfaceInfo.getId();
        if (!(oldId > 0 && oldId.equals(id))) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        interfaceInfoService.valid(interfaceInfo, false);
        BeanUtils.copyProperties(interfaceInfoUpdateRequest, oldInterfaceInfo);
        interfaceInfoService.updateById(oldInterfaceInfo);
        return ResultUtils.success(oldId);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteInterfaceInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        long id = deleteRequest.getId();
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        User loginUser = userService.getLoginUser(request);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (!oldInterfaceInfo.getUserid().equals(loginUser.getId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = interfaceInfoService.removeById(id);
        return ResultUtils.success(result);
    }


}
