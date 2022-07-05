package com.steadyheart.springbootinit.model.dto.interfaceInfo;

import lombok.Data;

/**
 * 请求参数字段
 * @author lts
 * @create 2023-11-04 21:42
 */
@Data
public class RequestParamsField {
    private String id;
    private String fieldName;
    private String type;
    private String desc;
    private String required;
}
