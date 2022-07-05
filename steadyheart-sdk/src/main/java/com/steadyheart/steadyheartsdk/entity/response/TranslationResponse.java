package com.steadyheart.steadyheartsdk.entity.response;

import lombok.Data;

/**
 * 接收翻译
 * @author lts
 * @create 2024-01-08 18:38
 */
@Data
public class TranslationResponse {

    /**
     * 翻译后的结果
     */
    private String translated;
}
