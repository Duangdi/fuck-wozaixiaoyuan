package com.fuckwzxy.service.impl;

import com.fuckwzxy.bean.UserInfo;
import com.fuckwzxy.mapper.UserMapper;
import com.fuckwzxy.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public void addUser(UserInfo userInfo) {
        userMapper.insert(userInfo);
    }

    @Override
    public String updateUser(UserInfo userInfoUpdateVo) {
        userMapper.updateByPrimaryKey(userInfoUpdateVo);
        return "success";
    }


}
