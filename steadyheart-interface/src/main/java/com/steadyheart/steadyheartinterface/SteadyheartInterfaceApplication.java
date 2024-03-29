package com.steadyheart.steadyheartinterface;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.steadyheart"})
public class SteadyheartInterfaceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SteadyheartInterfaceApplication.class, args);
    }

}
