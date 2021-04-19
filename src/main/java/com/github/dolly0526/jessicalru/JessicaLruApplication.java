package com.github.dolly0526.jessicalru;

import cn.hutool.extra.spring.EnableSpringUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableSpringUtil
@SpringBootApplication
public class JessicaLruApplication {

    public static void main(String[] args) {
        SpringApplication.run(JessicaLruApplication.class, args);
    }
}
