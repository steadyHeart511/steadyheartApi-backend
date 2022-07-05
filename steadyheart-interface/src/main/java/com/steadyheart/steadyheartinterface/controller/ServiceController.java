package com.steadyheart.steadyheartinterface.controller;

import cn.hutool.json.JSONUtil;
import com.aliyun.sdk.service.alimt20181012.AsyncClient;
import com.aliyun.sdk.service.alimt20181012.models.TranslateGeneralRequest;
import com.aliyun.sdk.service.alimt20181012.models.TranslateGeneralResponse;
import com.aliyun.sdk.service.alimt20181012.models.TranslateGeneralResponseBody;
import com.steadyheart.steadyheartinterface.enums.Api;
import com.steadyheart.steadyheartsdk.entity.enums.ErrorCode;
import com.steadyheart.steadyheartsdk.entity.params.TranslationParams;
import com.steadyheart.steadyheartsdk.entity.response.JokeResponse;
import com.steadyheart.steadyheartsdk.entity.response.TranslationResponse;
import com.steadyheart.steadyheartsdk.exception.ApiException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.steadyheart.steadyheartinterface.utils.RequestUtil.*;

/**
 * @author lts
 * @create 2024-01-07 16:33
 */
@RestController
public class ServiceController {

    @Resource
    private AsyncClient client;

    @GetMapping("/joke")
    public JokeResponse getJoke() {
        return JSONUtil.toBean(get(Api.JOKE.getAddress()), JokeResponse.class);
    }

    @GetMapping("/translate")
    public TranslationResponse getTranslation(TranslationParams translationParams) throws ExecutionException, InterruptedException {
        //  文字本身语言
        String sourceLanguage = translationParams.getSourceLanguage();
        //  目标语言
        String targetLanguage = translationParams.getTargetLanguage();
        //  文本
        String sourceText = translationParams.getSourceText();
        //  限制文本的长度
        if (sourceText.length() > 100) {
            throw new ApiException(ErrorCode.PARAMS_ERROR,"文本长度不能超过100");
        }
        //  构造请求参数
        TranslateGeneralRequest translateGeneralRequest = TranslateGeneralRequest.builder()
                .formatType("text")
                .sourceLanguage(sourceLanguage)
                .targetLanguage(targetLanguage)
                .sourceText(sourceText)
                .scene("general")
                .build();
        //  发送请求
        CompletableFuture<TranslateGeneralResponse> response = client.translateGeneral(translateGeneralRequest);
        TranslateGeneralResponse resp = response.get();
        TranslateGeneralResponseBody body = resp.getBody();
        Integer code = body.getCode();
        //  翻译失败
        if (code != 200) {
            String message = body.getMessage();
            if (code == 10001) {
                throw new ApiException(ErrorCode.OPERATION_ERROR,"请求超时");
            }
            if (code == 10002) {
                throw new ApiException(ErrorCode.SYSTEM_ERROR,"系统内部错误");
            }
            if (code == 10004 || code == 500) {
                throw new ApiException(ErrorCode.PARAMS_ERROR,message);
            }
            throw new ApiException(ErrorCode.PARAMS_ERROR, message);
        }
        //  翻译成功，返回翻译结果
        TranslateGeneralResponseBody.Data data = body.getData();
        TranslationResponse translationResponse = new TranslationResponse();
        translationResponse.setTranslated(data.getTranslated());
        return translationResponse;
    }


}
