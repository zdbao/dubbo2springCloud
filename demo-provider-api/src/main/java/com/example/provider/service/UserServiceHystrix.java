package com.example.provider.service;

import com.example.provider.dto.User;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserServiceHystrix implements UserService{
    @Override
    public Map<String, Object> getUserByConfKey(String confKey) {
        Map<String,Object> map = new HashMap<>();
        map.put("success","false");
        map.put("msg","熔断");
        map.put("confKey","confKey");
        return map;
    }

    @Override
    public User getUserById(int id) {
        User user = new User();
        return user;
    }
}
