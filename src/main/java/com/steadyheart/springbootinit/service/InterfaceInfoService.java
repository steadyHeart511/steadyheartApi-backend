package com.steadyheart.springbootinit.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.steadyheart.springbootinit.model.dto.interfaceInfo.InterfaceInfoQueryRequest;
import com.baomidou.mybatisplus.extension.service.IService;
import com.steadyheart.steadyheartcommon.model.entity.InterfaceInfo;

import java.util.Collection;
import java.util.List;

/**
* @author lts
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2023-10-08 11:45:10
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);

    QueryWrapper<InterfaceInfo> getQueryWrapper(InterfaceInfoQueryRequest interfaceInfoQueryRequest);

    List<InterfaceInfo> getInterfaceInfo(Collection collection);

}
