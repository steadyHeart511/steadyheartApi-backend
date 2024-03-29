package com.steadyheart.springbootinit.model.dto.interfaceInfo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author lts
 * @create 2023-10-15 10:07
 */
@Data
public class InterfaceInfoInvokeRequest implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 用户请求参数
     */
    private String userRequestParams;

    private List<Field> requestParams;

    private static final long serialVersionUID = 1L;

    @Data
    public static class Field {
        private String fieldName;
        private String value;
    }
}
