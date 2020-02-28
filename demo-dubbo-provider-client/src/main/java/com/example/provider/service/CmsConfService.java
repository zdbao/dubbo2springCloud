package com.example.provider.service;

import com.example.provider.dto.CmsConfList;

import java.util.List;
import java.util.Map;

public interface CmsConfService {

    Map<String,String> getByConfKey(String confKey);

    List<CmsConfList> getCmsConfList(String title, String confKey, String productName, Integer showStatus);

    String addCmsConfList(CmsConfList dto);

    Map<String, String> getCmsConfNodeList(String confKey,Integer level);

    Map<String, String> getCmsConfNodeList(String confKey,Integer level, Integer parentId);
}
