package com.zbycorp.user.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xuyonghong
 * @date 2025-04-08 17:00
 **/
@RestController
@RequestMapping("user")
public class UserController {

    @GetMapping("hello")
    public String hello() {
        return "hello";
    }
}
