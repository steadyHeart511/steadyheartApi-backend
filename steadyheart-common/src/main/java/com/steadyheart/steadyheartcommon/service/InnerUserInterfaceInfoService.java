package com.steadyheart.steadyheartcommon.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.steadyheart.steadyheartcommon.model.entity.InterfaceInfo;

/**
* @author lts
* @description 针对表【InnerUserInterfaceInfoService(接口信息)】的数据库操作Service
* @createDate 2023-10-08 11:45:10
*/
public interface InnerUserInterfaceInfoService {

    /**
     * 调用接口统计
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean invokeCount(long interfaceInfoId, long userId);


}
