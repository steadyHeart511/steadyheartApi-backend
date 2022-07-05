package com.steadyheart.springbootinit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.steadyheart.steadyheartcommon.service.common.ErrorCode;
import com.steadyheart.steadyheartcommon.exception.ThrowUtils;
import com.steadyheart.springbootinit.mapper.UserInterfaceInfoMapper;
import com.steadyheart.steadyheartcommon.model.entity.UserInterfaceInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author 李天帅
 * @description 针对表【user_interface_info(用户调用接口关系表)】的数据库操作Service实现
 * @createDate 2023-10-19 22:47:24
 */
@Service
public class UserInterfaceInfoServiceImpl2 extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo> {


    @Transactional(rollbackFor = RuntimeException.class)
    public boolean updateUserInterface(long userId, long interfaceInfoId) {
        //  第一次调用该接口，赠送10次调用机会
        LambdaQueryWrapper<UserInterfaceInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInterfaceInfo::getUserId, userId);
        queryWrapper.eq(UserInterfaceInfo::getInterfaceId, interfaceInfoId);
        UserInterfaceInfo userInterfaceInfo = getOne(queryWrapper);
        if (userInterfaceInfo == null) {
            UserInterfaceInfo insertUserInterfaceInfo = new UserInterfaceInfo();
            insertUserInterfaceInfo.setInterfaceId(interfaceInfoId);
            insertUserInterfaceInfo.setUserId(userId);
            insertUserInterfaceInfo.setLeftNum(10);
            boolean result = save(insertUserInterfaceInfo);
            ThrowUtils.throwIf(!result, ErrorCode.SYSTEM_ERROR);
        }

        //  调用次数加一，剩余次数减一
        LambdaUpdateWrapper<UserInterfaceInfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserInterfaceInfo::getUserId, userId);
        updateWrapper.eq(UserInterfaceInfo::getInterfaceId, interfaceInfoId);
        //  判断接口的剩余次数是否大于0
        updateWrapper.gt(UserInterfaceInfo::getLeftNum, 0);
        updateWrapper.setSql("totalNum = totalNum + 1,leftNum = leftNum - 1");
        return update(updateWrapper);
    }
}




