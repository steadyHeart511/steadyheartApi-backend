package com.steadyheart.springbootinit.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.steadyheart.steadyheartcommon.service.common.ErrorCode;
import com.steadyheart.steadyheartcommon.exception.BusinessException;
import com.steadyheart.springbootinit.service.UserService;
import com.steadyheart.steadyheartcommon.model.entity.User;
import com.steadyheart.steadyheartcommon.service.InnerUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @author lts
 * @create 2023-10-25 16:07
 */
@DubboService
public class InnerUserServiceImpl implements InnerUserService {

    @Resource
    private UserService userService;

    @Override
    public User getInvokeUser(String accessKey) {
        if (StringUtils.isBlank(accessKey)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getAccessKey,accessKey);
        User user = userService.getOne(queryWrapper);
        return user;
    }
}
