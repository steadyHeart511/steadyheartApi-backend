package com.steadyheart.springbootinit.model.dto.interfaceInfo;

import lombok.Data;

/**
 * 响应参数字段
 * @author lts
 * @create 2024-01-15 9:47
 */
@Data
public class ResponseParamsField {
    private String id;
    private String fieldName;
    private String type;
    private String desc;
}
