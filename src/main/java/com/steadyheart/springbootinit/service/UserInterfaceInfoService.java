package com.steadyheart.springbootinit.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.steadyheart.springbootinit.model.dto.userInterfaceInfo.UserInterfaceInfoQueryRequest;
import com.baomidou.mybatisplus.extension.service.IService;
import com.steadyheart.steadyheartcommon.model.entity.UserInterfaceInfo;

/**
* @author 李天帅
* @description 针对表【user_interface_info(用户调用接口关系表)】的数据库操作Service
* @createDate 2023-10-19 22:47:24
*/
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    void validInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add);

    Wrapper<UserInterfaceInfo> getQueryWrapper(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest);

    boolean invokeCount(long userId,long interfaceInfoId);

}
