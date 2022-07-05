package com.steadyheart.steadyheartsdk.entity.response;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lts
 * @create 2023-11-02 11:01
 */
@Data
@NoArgsConstructor
public class CurrencyResponse implements Serializable {

    private Map<String,Object> data = new HashMap<>();

    //@JsonAnyGetter注解允许将Map中key/value直接映射到JSON上
    @JsonAnyGetter
    public Map<String, Object> getData() {
        return data;
    }

    //@JsonAnySetter注解允许将没有匹配到字段放在map里
    // @JsonAnySetter
    public void setData(String key, Object value) {
        this.data.put(key, value);
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
