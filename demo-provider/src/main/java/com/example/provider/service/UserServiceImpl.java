package com.example.provider.service;

import com.alibaba.fastjson.JSON;
import com.example.provider.dto.User;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@Service
@RestController
public class UserServiceImpl implements UserService {

    @Reference
    private CmsConfService cmsConfService;

    @Override
    @RequestMapping("/user/getUserByConfKey")
    public Map<String, Object> getUserByConfKey(@RequestParam String confKey) {
        //测试熔断
//        try{
//            Thread.sleep(10000);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        Map<String,Object> map = new HashMap<>();
        map.putAll(cmsConfService.getByConfKey(confKey));
        map.put("user",getUserById(1));
        return map;
    }

    @Override
    @RequestMapping("/user/getUserById")
    public User getUserById(@RequestParam int id) {
        //测试熔断
//        try{
//            Thread.sleep(10000);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        User user = new User();
        user.setId(id);
        user.setName("张三丰");
        user.setAge(100);
        user.setUpdateTime(new Date());
        return user;
    }
}
