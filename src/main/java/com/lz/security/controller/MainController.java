package com.lz.security.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class MainController {

    @GetMapping("vip/{id}")
    public Object vip(@PathVariable("id") String id) {
        return "vip" + id;
    }

//    @PostMapping("/mobile")
//    public Object mobile(String mobile, String code){
//
//        System.out.println(mobile + code);
//        return mobile + code;
//    }
}
