package com.cloud.controller;

import com.cloud.utils.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContainerController {

    @GetMapping("/hello")
    public R<Object> hello() {
        return R.success("Hello from Nacos-Config-Client");
    }
}
