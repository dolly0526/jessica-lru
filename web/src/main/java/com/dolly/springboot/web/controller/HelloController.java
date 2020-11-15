package com.dolly.springboot.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author yusenyang
 * @create 2020/11/15 18:10
 */
@Controller
public class HelloController {

    @RequestMapping("/success")
    public String success() {
        return "success";
    }
}
