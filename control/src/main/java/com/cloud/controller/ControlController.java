package com.cloud.controller;

import com.cloud.utils.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/control")
public class ControlController {

    @GetMapping("/hello")
    public R<Object> hello() {
        return  R.success("Hello, World!");
    }




}