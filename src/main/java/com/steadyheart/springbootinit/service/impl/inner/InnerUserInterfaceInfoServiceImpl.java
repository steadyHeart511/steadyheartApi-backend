package com.steadyheart.springbootinit.service.impl.inner;

import com.steadyheart.springbootinit.service.UserInterfaceInfoService;
import com.steadyheart.steadyheartcommon.service.InnerUserInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @author lts
 * @create 2023-10-25 16:08
 */
@DubboService
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        return userInterfaceInfoService.invokeCount(userId,interfaceInfoId);
    }
}
