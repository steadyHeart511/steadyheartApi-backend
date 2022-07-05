package com.steadyheart.steadyheartinterface.enums;

/**
 * 调用第三方Api的地址
 *
 * @author lts
 * @create 2024-01-07 18:55
 */
public enum Api {

    /**
     * 获取笑话的接口地址
     */
    JOKE("https://api.vvhan.com/api/joke?type=json");

    Api(String address) {
        this.address = address;
    }

    /**
     * 请求地址
     */
    private String address;

    public String getAddress() {
        return address;
    }
}
