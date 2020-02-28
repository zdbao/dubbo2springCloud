package com.example.provider.service;


import com.example.provider.dto.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
@FeignClient(value = "demo-provider",fallback = UserServiceHystrix.class)
public interface UserService {

    @RequestMapping(value = "/user/getUserByConfKey",method = RequestMethod.GET)
    Map<String,Object> getUserByConfKey(@RequestParam("confKey") String confKey);

    @RequestMapping(value = "/user/getUserById",method = RequestMethod.GET)
    User getUserById(@RequestParam("id") int id);

}
