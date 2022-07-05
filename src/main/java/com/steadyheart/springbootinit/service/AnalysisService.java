package com.steadyheart.springbootinit.service;

import com.steadyheart.springbootinit.model.vo.InterfaceInfoVo;
import com.steadyheart.steadyheartcommon.model.entity.UserInterfaceInfo;

import java.util.List;

/**
 * @author lts
 * @create 2023-10-27 10:51
 */
public interface AnalysisService {

    /**
     * 获取调用次数最多的前n个接口
     * @param num
     * @return
     */
    List<UserInterfaceInfo> getTopInvokeInterface(Integer num);
}
