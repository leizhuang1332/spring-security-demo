package com.lz.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("vip/{id}")
    public Object vip(@PathVariable("id") String id) {
        return "vip" + id;
    }
}
