package com.steadyheart.springbootinit.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.steadyheart.springbootinit.annotation.AuthCheck;
import com.steadyheart.springbootinit.common.*;
import com.steadyheart.springbootinit.model.dto.interfaceInfo.*;
import com.steadyheart.steadyheartcommon.service.common.BaseResponse;
import com.steadyheart.steadyheartcommon.service.common.ErrorCode;
import com.steadyheart.steadyheartcommon.service.common.ResultUtils;
import com.steadyheart.steadyheartcommon.exception.BusinessException;
import com.steadyheart.steadyheartcommon.exception.ThrowUtils;
import com.steadyheart.springbootinit.model.enums.InterfaceInfoEnum;
import com.steadyheart.springbootinit.service.InterfaceInfoService;
import com.steadyheart.springbootinit.service.UserService;
import com.steadyheart.steadyheartcommon.model.entity.InterfaceInfo;
import com.steadyheart.steadyheartcommon.model.entity.User;
import com.steadyheart.steadyheartsdk.client.SteadyheartClient;
import com.steadyheart.steadyheartsdk.entity.request.CurrencyRequest;
import com.steadyheart.steadyheartsdk.service.ApiService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static com.steadyheart.springbootinit.constant.UserConstant.ADMIN_ROLE;

/**
 * 帖子接口
 *
 * @author lts
 *
 */
@RestController
@RequestMapping("/interfaceInfo")
@Slf4j
public class InterfaceInfoController {

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Resource
    private UserService userService;

    @Resource
    private SteadyheartClient steadyheartClient;

    @Resource
    private ApiService apiService;

    private final static Gson GSON = new Gson();

    // region 增删改查

    /**
     * 创建
     *
     * @param interfaceInfoAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = ADMIN_ROLE)
    public BaseResponse<Long> addInterfaceInfo(@RequestBody InterfaceInfoAddRequest interfaceInfoAddRequest, HttpServletRequest request) {

        //  非空校验
        if (interfaceInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();

        //  将获取到的请求参数转为json
        if (CollUtil.isNotEmpty(interfaceInfoAddRequest.getRequestParams())) {
            List<RequestParamsField> fields = interfaceInfoAddRequest.getRequestParams()
                    .stream()
                    .filter(field -> StringUtils.isNoneBlank(field.getFieldName(), field.getType(), field.getRequired()))
                    .map(field -> {
                        field.setFieldName(field.getFieldName().trim());
                        if (StringUtils.isNotBlank(field.getDesc())) {
                            field.setDesc(field.getDesc().trim());
                        }
                        return field;
                    })
                    .collect(Collectors.toList());
            interfaceInfo.setRequestParams(JSONUtil.toJsonStr(fields));
        }

        //  将获取到的响应参数转为json
        if (CollUtil.isNotEmpty(interfaceInfoAddRequest.getResponseParams())) {
            List<ResponseParamsField> fields = interfaceInfoAddRequest.getResponseParams()
                    .stream()
                    .filter(field -> StringUtils.isNoneBlank(field.getFieldName(), field.getType()))
                    .map(field -> {
                        field.setFieldName(field.getFieldName().trim());
                        if (StringUtils.isNotBlank(field.getDesc())) {
                            field.setDesc(field.getDesc().trim());
                        }
                        return field;
                    })
                    .collect(Collectors.toList());
            interfaceInfo.setResponseParams(JSONUtil.toJsonStr(fields));
        }

        //  复制数据
        BeanUtils.copyProperties(interfaceInfoAddRequest, interfaceInfo);

        //  校验参数
        interfaceInfoService.validInterfaceInfo(interfaceInfo, true);
        User loginUser = userService.getLoginUser(request);
        interfaceInfo.setUserId(loginUser.getId());
        boolean result = interfaceInfoService.save(interfaceInfo);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newInterfaceInfoId = interfaceInfo.getId();
        return ResultUtils.success(newInterfaceInfoId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = ADMIN_ROLE)
    public BaseResponse<Boolean> deleteInterfaceInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        boolean b = interfaceInfoService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @param interfaceInfoUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = ADMIN_ROLE)
    public BaseResponse<Boolean> updateInterfaceInfo(@RequestBody InterfaceInfoUpdateRequest interfaceInfoUpdateRequest) {
        if (interfaceInfoUpdateRequest == null || interfaceInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        //  将获取到的请求参数转为json
        if (CollUtil.isNotEmpty(interfaceInfoUpdateRequest.getRequestParams())) {
            List<RequestParamsField> fields = interfaceInfoUpdateRequest.getRequestParams()
                    .stream()
                    .filter(field -> StringUtils.isNoneBlank(field.getFieldName(), field.getType(), field.getRequired()))
                    .map(field -> {
                        field.setFieldName(field.getFieldName().trim());
                        if (StringUtils.isNotBlank(field.getDesc())) {
                            field.setDesc(field.getDesc().trim());
                        }
                        return field;
                    })
                    .collect(Collectors.toList());
            interfaceInfo.setRequestParams(JSONUtil.toJsonStr(fields));
        }

        //  将获取到的响应参数转为json
        if (CollUtil.isNotEmpty(interfaceInfoUpdateRequest.getResponseParams())) {
            List<ResponseParamsField> fields = interfaceInfoUpdateRequest.getResponseParams()
                    .stream()
                    .filter(field -> StringUtils.isNoneBlank(field.getFieldName(), field.getType()))
                    .map(field -> {
                        field.setFieldName(field.getFieldName().trim());
                        if (StringUtils.isNotBlank(field.getDesc())) {
                            field.setDesc(field.getDesc().trim());
                        }
                        return field;
                    })
                    .collect(Collectors.toList());
            interfaceInfo.setResponseParams(JSONUtil.toJsonStr(fields));
        }
        BeanUtils.copyProperties(interfaceInfoUpdateRequest, interfaceInfo);
        // 参数校验
        interfaceInfoService.validInterfaceInfo(interfaceInfo, false);
        long id = interfaceInfoUpdateRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }

    /**
     * 发布接口（仅管理员）
     *
     * @param idRequest
     * @return
     */
    @PostMapping("/online")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Boolean> onlineInterfaceInfo(@RequestBody IdRequest idRequest) {
        //  判断id是否合法
        if (idRequest == null || idRequest.getId() == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"接口id错误");
        }
        //  校验接口是否存在
        Long id = idRequest.getId();
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceInfo == null,ErrorCode.PARAMS_ERROR,"接口id不存在");
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(id);
        interfaceInfo.setStatus(InterfaceInfoEnum.ONLINE.getValue());
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }

    /**
     * 关闭接口（仅管理员）
     *
     * @param idRequest
     * @return
     */
    @PostMapping("/offline")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Boolean> offlineInterfaceInfo(@RequestBody IdRequest idRequest) {
        //  判断id是否合法
        if (idRequest == null || idRequest.getId() == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"接口id错误");
        }
        //  校验接口是否存在
        Long id = idRequest.getId();
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceInfo == null,ErrorCode.PARAMS_ERROR,"接口id不存在");
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(id);
        interfaceInfo.setStatus(InterfaceInfoEnum.OFFLINE.getValue());
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }

    /**
     * 在线调用接口
     *
     * @param interfaceInfoInvokeRequest
     * @return
     */
    @PostMapping("/invoke")
    public BaseResponse<Object> invokeInterfaceInfo(@RequestBody InterfaceInfoInvokeRequest interfaceInfoInvokeRequest,HttpServletRequest request) {
        if (interfaceInfoInvokeRequest == null || interfaceInfoInvokeRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = interfaceInfoInvokeRequest.getId();
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        if (oldInterfaceInfo.getStatus().equals(InterfaceInfoEnum.OFFLINE.getValue())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口已关闭");
        }
        List<InterfaceInfoInvokeRequest.Field> requestParams = interfaceInfoInvokeRequest.getRequestParams();
        String requestParamsJson = "{}";

        //  有参数取出来构建成json对象
        JsonObject jsonObject = new JsonObject();
        if (!CollUtil.isEmpty(requestParams)) {
            requestParams.stream().forEach(requestParam -> {
                jsonObject.addProperty(requestParam.getFieldName(),requestParam.getValue());
            });
            //  转为json字符串
            requestParamsJson = new Gson().toJson(jsonObject);
        }
        //  转为map
        Map<String, Object> params = new Gson().fromJson(requestParamsJson, new TypeToken<Map<String, Object>>() {
        }.getType());
        //  通用请求参数
        CurrencyRequest currencyRequest = new CurrencyRequest();
        currencyRequest.setPath(oldInterfaceInfo.getUrl());
        currencyRequest.setMethod(oldInterfaceInfo.getMethod());
        currencyRequest.setRequestParams(params);
        // 获取当前登录用户的ak和sk，这样相当于用户自己的这个身份去调用，
        // 也不会担心它刷接口，因为知道是谁刷了这个接口，会比较安全
        User loginUser = userService.getLoginUser(request);
        String accessKey = loginUser.getAccessKey();
        String secretKey = loginUser.getSecretKey();
        SteadyheartClient temp = new SteadyheartClient(accessKey,secretKey);
        com.steadyheart.steadyheartsdk.entity.response.BaseResponse result = apiService.request(temp, currencyRequest);
        // 返回成功响应，并包含调用结果
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<InterfaceInfo> getInterfaceInfoById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(interfaceInfo);
    }

    /**
     * 分页获取列表（封装类）
     *
     * @param interfaceInfoQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<InterfaceInfo>> listInterfaceInfoByPage(@RequestBody InterfaceInfoQueryRequest interfaceInfoQueryRequest,
                                                                     HttpServletRequest request) {
        if (interfaceInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = interfaceInfoQueryRequest.getCurrent();
        long size = interfaceInfoQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 50, ErrorCode.PARAMS_ERROR);
        Page<InterfaceInfo> interfaceInfoPage = interfaceInfoService.page(new Page<>(current, size),
                interfaceInfoService.getQueryWrapper(interfaceInfoQueryRequest));
        return ResultUtils.success(interfaceInfoPage);
    }

    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param interfaceInfoQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<InterfaceInfo>> listMyInterfaceInfoByPage(@RequestBody InterfaceInfoQueryRequest interfaceInfoQueryRequest,
                                                                           HttpServletRequest request) {
        if (interfaceInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        interfaceInfoQueryRequest.setUserId(loginUser.getId());
        long current = interfaceInfoQueryRequest.getCurrent();
        long size = interfaceInfoQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<InterfaceInfo> interfaceInfoPage = interfaceInfoService.page(new Page<>(current, size),
                interfaceInfoService.getQueryWrapper(interfaceInfoQueryRequest));
        return ResultUtils.success(interfaceInfoPage);
    }

}
