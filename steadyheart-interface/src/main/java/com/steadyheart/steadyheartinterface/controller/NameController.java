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
        return user.getName();
    }
}
