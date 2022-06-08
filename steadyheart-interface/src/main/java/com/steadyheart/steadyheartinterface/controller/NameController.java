package com.steadyheart.steadyheartinterface.controller;

import com.steadyheart.steadyheartsdk.entity.User;
import com.steadyheart.steadyheartsdk.utils.SignUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lts
 * @create 2023-10-11 14:24
 */
@RestController
@RequestMapping("/name")
public class NameController {

    @PostMapping("/user")
    public String getUserByPost(@RequestBody User user, HttpServletRequest request) {
        String accessKey = request.getHeader("accessKey");
        String body = request.getHeader("body");
        String sign = request.getHeader("sign");
        String nonce = request.getHeader("nonce");
        String timestamp = request.getHeader("timestamp");

        //  todo 这里是手动判断，需要从数据库中去查
        if (!"lts".equals(accessKey)) {
            throw new RuntimeException("accessKey not found");
        }

        //  todo 需要自己写逻辑
        if (Integer.valueOf(nonce) > 10000) {
            throw new RuntimeException("用过了");
        }

//        todo 需要自己写逻辑
//        if (timestamp) {
//
//        }
        if (!SignUtils.sign("xx",body).equals(sign)) {
            throw new RuntimeException("信息错误");
        }
        return user.getName();
    }
}
