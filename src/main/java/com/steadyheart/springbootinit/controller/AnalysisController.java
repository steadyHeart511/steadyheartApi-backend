package com.steadyheart.springbootinit.controller;

import cn.hutool.core.collection.CollUtil;
import com.steadyheart.springbootinit.annotation.AuthCheck;
import com.steadyheart.steadyheartcommon.service.common.BaseResponse;
import com.steadyheart.steadyheartcommon.service.common.ErrorCode;
import com.steadyheart.springbootinit.constant.UserConstant;
import com.steadyheart.steadyheartcommon.service.common.ResultUtils;
import com.steadyheart.steadyheartcommon.exception.BusinessException;
import com.steadyheart.springbootinit.model.vo.InterfaceInfoVo;
import com.steadyheart.springbootinit.service.AnalysisService;
import com.steadyheart.springbootinit.service.InterfaceInfoService;
import com.steadyheart.steadyheartcommon.model.entity.InterfaceInfo;
import com.steadyheart.steadyheartcommon.model.entity.UserInterfaceInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lts
 * @create 2023-10-27 10:21
 */
@RestController
@RequestMapping("/analysis")
public class AnalysisController {

    @Resource
    private AnalysisService analysisService;

    @Resource
    private InterfaceInfoService interfaceInfoService;


    @GetMapping("/top/interfaceInfo/invoke")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<List<InterfaceInfoVo>> getTopInvokeInterface() {
        List<UserInterfaceInfo> topInvokeInterface = analysisService.getTopInvokeInterface(3);
        Map<Long, List<UserInterfaceInfo>> interfaceInfoIdObjMap = topInvokeInterface.stream().collect(Collectors.groupingBy(UserInterfaceInfo::getInterfaceId));
        List<InterfaceInfo> interfaceInfoList = interfaceInfoService.getInterfaceInfo(interfaceInfoIdObjMap.keySet());
        if (CollUtil.isEmpty(interfaceInfoList)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        List<InterfaceInfoVo> topInvokeInterfaceVo = interfaceInfoList.stream().map(interfaceInfo -> {
            InterfaceInfoVo interfaceInfoVo = new InterfaceInfoVo();
            BeanUtils.copyProperties(interfaceInfo, interfaceInfoVo);
            interfaceInfoVo.setTotalNum(interfaceInfoIdObjMap.get(interfaceInfo.getId()).get(0).getTotalNum());
            return interfaceInfoVo;
        }).collect(Collectors.toList());
        return ResultUtils.success(topInvokeInterfaceVo);
    }
}
