package com.group3.mBaaS;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @GetMapping("/")
    public String status() {
        return "Mobile Backend is up and running.";
    }
}
