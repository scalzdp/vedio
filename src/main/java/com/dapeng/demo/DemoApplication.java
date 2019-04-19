package com.dapeng.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@EnableEurekaClient
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }


    @GetMapping(value = "/getUser")
    @ResponseBody
    public Map<String,Object> getUser(@RequestParam Integer id){
        Map<String,Object> data = new HashMap<>();
        data.put("id",id);
        data.put("userName","admin");
        data.put("from","provider-A");
        return data;
    }

}
