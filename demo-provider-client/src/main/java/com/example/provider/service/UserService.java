package com.example.provider.service;


import com.example.provider.dto.User;

import java.util.Map;

public interface UserService {

    Map<String,Object> getUserByConfKey(String confKey);

    User getUserById(int id);

}
