package com.steadyheart.steadyheartcommon.model.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 描述账号状态
 * @author lts
 * @create 2023-11-04 17:58
 */
public enum UserAccountStatusEnum {
    NORMAL(0,"正常"),
    BAN(1,"封号");
    private Integer value;
    private String text;
    UserAccountStatusEnum(Integer value, String text) {
        this.value = value;
        this.text = text;
    }

    public Integer getValue() {
        return value;
    }

    public String getText() {
        return text;
    }

    /**
     * 获取枚举类value集合
     * @return
     */
    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(item -> item.getValue()).collect(Collectors.toList());
    }

}
