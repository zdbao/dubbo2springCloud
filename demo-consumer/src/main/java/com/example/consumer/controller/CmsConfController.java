package com.example.consumer.controller;

import com.example.provider.dto.User;
import com.example.provider.service.CmsConfService;
import com.example.provider.service.UserService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
public class CmsConfController {

    @Reference
    private CmsConfService cmsConfService;
    @Autowired
    private UserService userService;

    /**
     * 底层访问的是dubbo服务
     * @date: 2020年01月17日
     * @author: leslie.zhang
     */
    @RequestMapping(value = "/cmsConf/getByConfKey", method = RequestMethod.GET)
    public Map<String,String> getByConfKey(@RequestParam(required = false) String key){
        return cmsConfService.getByConfKey(key);
    }

    /**
     * 访问的是springCloud的http接口，但是userService又访问了dubbo服务
     * @date: 2020年01月17日
     * @author: leslie.zhang
     */
    @RequestMapping(value = "/user/getUserByConfKey", method = RequestMethod.GET)
    public Map<String,Object> getUserByConfKey(@RequestParam(required = false) String key){
        return userService.getUserByConfKey(key);
    }

    /**
     * 访问的是springCloud的http接口
     * @date: 2020年01月17日
     * @author: leslie.zhang
     */
    @RequestMapping(value = "/user/getUserById", method = RequestMethod.GET)
    public User getUserById(@RequestParam(required = false) int key){
        return userService.getUserById(key);
    }
}
