package com.steadyheart.springbootinit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.steadyheart.springbootinit.model.vo.InterfaceInfoVo;
import com.steadyheart.steadyheartcommon.model.entity.UserInterfaceInfo;

import java.util.List;

/**
* @author 李天帅
* @description 针对表【user_interface_info(用户调用接口关系表)】的数据库操作Mapper
* @createDate 2023-10-19 22:47:24
* @Entity com.steadyheart.springbootinit.model.entity.UserInterfaceInfo
*/
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {

    List<UserInterfaceInfo> getTopInvokeInterface(Integer num);
}




