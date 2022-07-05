package com.steadyheart.springbootinit.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.steadyheart.steadyheartcommon.service.common.ErrorCode;
import com.steadyheart.steadyheartcommon.exception.BusinessException;
import com.steadyheart.springbootinit.service.InterfaceInfoService;
import com.steadyheart.steadyheartcommon.model.entity.InterfaceInfo;
import com.steadyheart.steadyheartcommon.service.InnerInterfaceInfoService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @author lts
 * @create 2023-10-25 16:07
 */
@DubboService
public class InnerInterfaceInfoServiceImpl implements InnerInterfaceInfoService {


    @Resource
    private InterfaceInfoService interfaceInfoService;

    /**
     * 查询请求地址和请求方式是否存在，后续可能会校验一下请求参数
     * @param path
     * @param method
     * @return
     */
    @Override
    public InterfaceInfo getInterfaceInfo(String path, String method) {
        if (StringUtils.isBlank(path) || StringUtils.isBlank(method)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //查询路径时不需要携带参数
        if (path.contains("?")) {
            path = path.substring(0,path.indexOf("?"));
        }
        //  不区分http和https
        if (path.startsWith("http://")) {
            path = path.substring(7);
        }
        if (path.startsWith("https://")) {
            path = path.substring(8);
        }
        LambdaQueryWrapper<InterfaceInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(InterfaceInfo::getUrl,path);
        queryWrapper.eq(InterfaceInfo::getMethod,method);
        InterfaceInfo interfaceInfo = interfaceInfoService.getOne(queryWrapper);
        return interfaceInfo;
    }
}
