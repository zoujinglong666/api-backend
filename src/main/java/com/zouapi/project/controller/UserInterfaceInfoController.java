package com.zouapi.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zouapi.project.common.BaseResponse;
import com.zouapi.project.common.ErrorCode;
import com.zouapi.project.common.ResultUtils;
import com.zouapi.project.exception.BusinessException;
import com.zouapi.project.mapper.UserInterfaceInfoMapper;
import com.zouapi.project.model.entity.InterfaceInfo;
import com.zouapi.project.model.entity.User;
import com.zouapi.project.model.entity.UserInterfaceInfo;
import com.zouapi.project.service.InterfaceInfoService;
import com.zouapi.project.service.UserInterfaceInfoService;
import com.zouapi.project.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 帖子接口
 *
 * @author yupi
 */
@RestController
@RequestMapping("/userInterfaceInfo")
@Slf4j
public class UserInterfaceInfoController {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Resource
    private UserService userService;

    @Resource
    private InterfaceInfoService interfaceInfoService;
    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    @PostMapping("/get/invoke/count")
    public BaseResponse<Boolean> getInvokeCount(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo currentInterfaceInfo = interfaceInfoService.getById(id);
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("InterfaceInfoId", id);
        UserInterfaceInfo oldUserInterfaceInfo = userInterfaceInfoMapper.selectOne(queryWrapper);
        if (oldUserInterfaceInfo != null) {
            UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
            BeanUtils.copyProperties(oldUserInterfaceInfo, userInterfaceInfo);
            Integer leftNum = userInterfaceInfo.getLeftNum();
            userInterfaceInfo.setLeftNum(leftNum + 10);
            boolean res = userInterfaceInfoService.updateById(userInterfaceInfo);
            return ResultUtils.success(res);
        } else {
            if (currentInterfaceInfo == null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }

            User loginUser = getLoginUser(request);
            Long userId = loginUser.getId();
            UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
            userInterfaceInfo.setUserId(userId);
            userInterfaceInfo.setInterfaceInfoId(currentInterfaceInfo.getId());
            userInterfaceInfo.setStatus(currentInterfaceInfo.getStatus());
            userInterfaceInfo.setLeftNum(10);
            boolean res = userInterfaceInfoService.save(userInterfaceInfo);
            return ResultUtils.success(res);
        }


    }

    private User getLoginUser(HttpServletRequest request) {
        return userService.getLoginUser(request);
    }


    // endregion

}
