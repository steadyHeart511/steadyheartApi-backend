package com.steadyheart.steadyheartinterface.controller;

import com.steadyheart.steadyheartsdk.client.SteadyheartClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

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
        // System.out.println(steadyheartClient.getNameByPost(new User("yy")));

    }
}
