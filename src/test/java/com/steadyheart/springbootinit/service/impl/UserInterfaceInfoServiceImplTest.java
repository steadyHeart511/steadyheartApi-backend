package com.steadyheart.springbootinit.service.impl;

import com.steadyheart.springbootinit.service.UserInterfaceInfoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author lts
 * @create 2023-10-20 14:59
 */
@SpringBootTest
@Slf4j
class UserInterfaceInfoServiceImplTest {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Test
    void invokeCount() {
        boolean result = userInterfaceInfoService.invokeCount(1, 1);
        if (result) {
            log.info("UserInterfaceInfoServiceImplTest[invokeCount]:调用成功");
        }
    }
}
