package com.steadyheart.steadyheartcommon.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.steadyheart.steadyheartcommon.model.entity.InterfaceInfo;
import com.steadyheart.steadyheartcommon.model.entity.UserInterfaceInfo;

/**
* @author 李天帅
* @description 针对表【user_interface_info(用户调用接口关系表)】的数据库操作Service
* @createDate 2023-10-19 22:47:24
*/
public interface InnerInterfaceInfoService {

    /**
     * 从数据库中查询模拟接口是否存在（请求路径、请求方法、请求参数）
     */
    InterfaceInfo getInterfaceInfo(String path, String method);

}
