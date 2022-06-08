package com.steadyheart.steadyheartinterface.controller;

import com.steadyheart.steadyheartsdk.client.SteadyheartClient;
import com.steadyheart.steadyheartsdk.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author lts
 * @create 2023-10-11 14:55
 */
@SpringBootTest
class NameControllerTest {

    @Resource
    private SteadyheartClient steadyheartClient;

    @Test
    void getUserByPost() {
        System.out.println(steadyheartClient.getNameByPost(new User("yy")));

    }
}
