package com.steadyheart.springbootinit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.steadyheart.steadyheartcommon.model.entity.InterfaceInfo;

import java.util.Collection;
import java.util.List;

/**
* @author lts
* @description 针对表【interface_info(接口信息)】的数据库操作Mapper
* @createDate 2023-10-08 11:45:10
* @Entity com.steadyheart.springbootinit.model.entity.InterfaceInfo
*/
public interface InterfaceInfoMapper extends BaseMapper<InterfaceInfo> {

    List<InterfaceInfo> getInterfaceInfo(Collection collection);
}




