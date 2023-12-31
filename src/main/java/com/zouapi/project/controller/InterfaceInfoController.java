package com.zouapi.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.zou.zouapiclientsdk.client.ZouApiClient;
import com.zouapi.project.common.*;
import com.zouapi.project.exception.BusinessException;
import com.zouapi.project.model.dto.InterfaceInfo.InterfaceInfoAddRequest;
import com.zouapi.project.model.dto.InterfaceInfo.InterfaceInfoInvokeRequest;
import com.zouapi.project.model.dto.InterfaceInfo.InterfaceInfoQueryRequest;
import com.zouapi.project.model.dto.InterfaceInfo.InterfaceInfoUpdateRequest;
import com.zouapi.project.model.entity.InterfaceInfo;
import com.zouapi.project.model.entity.User;
import com.zouapi.project.model.enums.InterfaceInfoStatusEnum;
import com.zouapi.project.model.vo.InterfaceInfoVo;
import com.zouapi.project.service.InterfaceInfoService;
import com.zouapi.project.service.UserService;
import org.apache.commons.lang3.StringUtils;
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
    @Resource
    ZouApiClient zouApiClient;

    private static long getIdResult(IdRequest idRequest) {
        long id = idRequest.getId();
        if (idRequest == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return id;
    }

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
        interfaceInfo.setUserId(loginUser.getId());
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
        if (!oldInterfaceInfo.getUserId().equals(loginUser.getId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = interfaceInfoService.removeById(id);
        return ResultUtils.success(result);
    }

    @GetMapping("/get")
    public BaseResponse<InterfaceInfo> getInterfaceInfoById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        return ResultUtils.success(interfaceInfo);
    }


    @PostMapping("/online")
    public BaseResponse<Boolean> onlineInterfaceInfo(@RequestBody IdRequest idRequest, HttpServletRequest request) {
        long id = getIdResult(idRequest);
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        if (oldInterfaceInfo.getStatus() == InterfaceInfoStatusEnum.ON_LINE.getValue()) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "该接口已上线");
        }
        // 仅本人或管理员可修改
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(id);
        interfaceInfo.setStatus(InterfaceInfoStatusEnum.ON_LINE.getValue());
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);

    }

    @PostMapping("/offline")
    public BaseResponse<Boolean> offlineInterfaceInfo(@RequestBody IdRequest idRequest, HttpServletRequest request) {
        long id = getIdResult(idRequest);

        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        if (oldInterfaceInfo.getStatus() == InterfaceInfoStatusEnum.OFF_LINE.getValue()) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "该接口已下线");
        }

        // 仅本人或管理员可修改
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(id);
        interfaceInfo.setStatus(InterfaceInfoStatusEnum.OFF_LINE.getValue());
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }

    @PostMapping("/removeIds")
    public BaseResponse<Boolean> removeInterfaceInfoByIds(@RequestBody List<Integer> list, HttpServletRequest request) {
        List<Integer> integerList = list.stream().distinct().collect(Collectors.toList());
        if (integerList.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = interfaceInfoService.removeByIds(integerList);
        if (!result) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(result);
    }

    @PostMapping("/invoke")
    public BaseResponse<Object> invokeInterfaceInfoByIds(@RequestBody InterfaceInfoInvokeRequest interfaceInfoInvokeRequest, HttpServletRequest request) {
        if (interfaceInfoInvokeRequest == null || interfaceInfoInvokeRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }


        Long id = interfaceInfoInvokeRequest.getId();
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);

        if (interfaceInfo.getStatus() == InterfaceInfoStatusEnum.OFF_LINE.getValue()) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "该接口已下线");
        }

        User loginUser = getLoginUser(request);
        String accessKey = loginUser.getAccessKey();
        String secretKey = loginUser.getSecretKey();
        ZouApiClient client = new ZouApiClient(accessKey, secretKey);
        String userRequestParams = interfaceInfoInvokeRequest.getUserRequestParams();
        if (StringUtils.isBlank(userRequestParams)) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "请求参数为空");
        }
        System.out.println(userRequestParams);
        Gson gson = new Gson();
        com.zou.zouapiclientsdk.model.User user = gson.fromJson(userRequestParams, com.zou.zouapiclientsdk.model.User.class);
        String userNameByPost = client.getUserNameByPost(user);
        return ResultUtils.success(userNameByPost);


    }


}
