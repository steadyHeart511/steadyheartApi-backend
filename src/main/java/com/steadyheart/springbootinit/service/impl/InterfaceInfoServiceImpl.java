package com.steadyheart.springbootinit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.steadyheart.steadyheartcommon.service.common.ErrorCode;
import com.steadyheart.springbootinit.constant.CommonConstant;
import com.steadyheart.steadyheartcommon.exception.BusinessException;
import com.steadyheart.springbootinit.mapper.InterfaceInfoMapper;
import com.steadyheart.springbootinit.model.dto.interfaceInfo.InterfaceInfoQueryRequest;
import com.steadyheart.springbootinit.service.InterfaceInfoService;
import com.steadyheart.steadyheartcommon.model.entity.InterfaceInfo;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * @author lts
 * @description 针对表【interface_info(接口信息)】的数据库操作Service实现
 * @createDate 2023-10-08 11:45:10
 */
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
        implements InterfaceInfoService {

    @Resource
    private InterfaceInfoMapper interfaceInfoMapper;

    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String name = interfaceInfo.getName();
        String url = interfaceInfo.getUrl();
        String method = interfaceInfo.getMethod();
        String requestExample = interfaceInfo.getRequestExample();
        String responseParams = interfaceInfo.getResponseParams();
        Integer reduceScore = interfaceInfo.getReduceScore();
        String description = interfaceInfo.getDescription();
        // 创建时，所有参数必须非空
        if (add) {
            if (StringUtils.isAnyBlank(name,url,method,requestExample,responseParams)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }
        if (StringUtils.isNotBlank(method)) {
            interfaceInfo.setMethod(method.trim().toUpperCase());
        }
        if (StringUtils.isNotBlank(url)) {
            interfaceInfo.setUrl(url.trim());
        }
        if (ObjectUtils.isNotEmpty(reduceScore) && reduceScore < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "扣除积分个数不能为负数");
        }
        if (StringUtils.isNotBlank(name) && name.length() > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口名称过长");
        }

        if (StringUtils.isNotBlank(description) && description.length() > 100) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口描述过长");
        }

    }

    @Override
    public QueryWrapper<InterfaceInfo> getQueryWrapper(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        InterfaceInfo interfaceInfoQuery = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoQueryRequest, interfaceInfoQuery);
        String sortField = interfaceInfoQueryRequest.getSortField();
        String sortOrder = interfaceInfoQueryRequest.getSortOrder();
        String name = interfaceInfoQuery.getName();
        String description = interfaceInfoQuery.getDescription();
        // name、description支持模糊搜索
        interfaceInfoQuery.setName(null);
        interfaceInfoQuery.setDescription(null);
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(interfaceInfoQuery);
        queryWrapper
                .like(StringUtils.isNotBlank(name),"name",name)
                .like(StringUtils.isNotBlank(description),"description",description);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        //  todo 游客不该查看未发布接口
        return queryWrapper;
    }

    @Override
    public List<InterfaceInfo> getInterfaceInfo(Collection collection) {
        return interfaceInfoMapper.getInterfaceInfo(collection);
    }
}




