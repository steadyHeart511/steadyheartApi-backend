package com.steadyheart.springbootinit.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.steadyheart.springbootinit.common.ErrorCode;
import com.steadyheart.springbootinit.constant.CommonConstant;
import com.steadyheart.springbootinit.exception.BusinessException;
import com.steadyheart.springbootinit.exception.ThrowUtils;
import com.steadyheart.springbootinit.model.dto.userInterfaceInfo.UserInterfaceInfoQueryRequest;
import com.steadyheart.springbootinit.model.entity.UserInterfaceInfo;
import com.steadyheart.springbootinit.service.UserInterfaceInfoService;
import com.steadyheart.springbootinit.mapper.UserInterfaceInfoMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
* @author 李天帅
* @description 针对表【user_interface_info(用户调用接口关系表)】的数据库操作Service实现
* @createDate 2023-10-19 22:47:24
*/
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
    implements UserInterfaceInfoService{


    /**
     * 针对添加或修改时校验参数
     * @param userInterfaceInfo
     * @param add   是否为添加时校验
     */
    @Override
    public void validInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add) {
        if (userInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //  创建时参数校验
        if (add) {
            if (userInterfaceInfo.getInterfaceId() <= 0 || userInterfaceInfo.getUserId() <= 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口或用户不存在");
            }
        } else {
            //  修改时参数校验
            if (userInterfaceInfo.getId() == null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
            if (userInterfaceInfo.getLeftNum() < 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "剩余次数不能小于 0");
            }
        }
    }

    /**
     * 获取查询wrapper（封装了查询条件）
     * @param userInterfaceInfoQueryRequest
     * @return
     */
    @Override
    public Wrapper<UserInterfaceInfo> getQueryWrapper(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest) {
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        BeanUtils.copyProperties(userInterfaceInfoQueryRequest,userInterfaceInfo);
        String sortField = userInterfaceInfoQueryRequest.getSortField();
        userInterfaceInfoQueryRequest.getSortOrder();
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>(userInterfaceInfo);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField), CommonConstant.SORT_ORDER_ASC.equals(sortField),sortField);
        return queryWrapper;
    }

    /**
     * 当调用接口时1，调用次数加一
     * @param userId
     * @param interfaceInfoId
     * @return
     */
    @Override
    public boolean invokeCount(long userId, long interfaceInfoId) {
        //  记录不存在，数据库记录修改也不会修改，因此没必要校验记录是否存在
        if (userId <= 0 || interfaceInfoId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LambdaUpdateWrapper<UserInterfaceInfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserInterfaceInfo::getUserId,userId);
        updateWrapper.eq(UserInterfaceInfo::getInterfaceId,interfaceInfoId);
        //  判断接口的剩余次数是否大于0
        updateWrapper.gt(UserInterfaceInfo::getLeftNum,0);
        updateWrapper.setSql("totalNum = totalNum + 1,leftNum = leftNum - 1");
        //  接口调用次数不足，抛出异常
        ThrowUtils.throwIf(!update(updateWrapper),ErrorCode.NOT_ENOUGH);
        return true;
    }
}




