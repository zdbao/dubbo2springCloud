package com.example.provider.service;

import com.alibaba.fastjson.JSON;
import com.example.provider.dto.CmsConfList;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.util.Assert;

import java.util.*;

@Service
public class CmsConfServiceImpl implements CmsConfService{

    @Override
    public Map<String, String> getByConfKey(String confKey) {
        Assert.notNull(confKey,"confKey not null");
        Map<String,String> map = new HashMap<>();
        map.put("success","true");
        map.put("confKey",confKey);
        return map;
    }

    @Override
    public List<CmsConfList> getCmsConfList(String title, String confKey, String productName, Integer showStatus) {
        CmsConfList cmsConfList = new CmsConfList();
        cmsConfList.setConfKey(confKey);
        cmsConfList.setTitle(title);
        cmsConfList.setProductName(productName);
        cmsConfList.setId(showStatus);
        cmsConfList.setUpdateTime(new Date());
        List<CmsConfList> list = new ArrayList<>();
        list.add(cmsConfList);
        return list;
    }

    @Override
    public String addCmsConfList(CmsConfList dto) {
        return "添加成功: " + JSON.toJSONString(dto);
    }

    @Override
    public Map<String, String> getCmsConfNodeList(String confKey, Integer level) {
        Map<String,String> map = new HashMap<>();
        map.put("success","true");
        map.put("ms","我是重载方法1");
        map.put("confKey",confKey);
        map.put("level",String.valueOf(level));
        return map;
    }

    @Override
    public Map<String, String> getCmsConfNodeList(String confKey, Integer level, Integer parentId) {
        Map<String,String> map = new HashMap<>();
        map.put("success","true");
        map.put("ms","我是重载方法2");
        map.put("confKey",confKey);
        map.put("level",String.valueOf(level));
        map.put("parentId",String.valueOf(parentId));
        return map;
    }
}
