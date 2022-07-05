package com.steadyheart.springbootinit.service.impl;

import com.steadyheart.springbootinit.mapper.UserInterfaceInfoMapper;
import com.steadyheart.springbootinit.service.AnalysisService;
import com.steadyheart.steadyheartcommon.model.entity.UserInterfaceInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lts
 * @create 2023-10-27 10:51
 */
@Service
public class AnalysisServiceImpl implements AnalysisService {

    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;
    /**
     * 获取调用次数最多的前n个接口
     * @param num
     * @return
     */
    @Override
    public List<UserInterfaceInfo> getTopInvokeInterface(Integer num) {
        return userInterfaceInfoMapper.getTopInvokeInterface(num);
    }
}
