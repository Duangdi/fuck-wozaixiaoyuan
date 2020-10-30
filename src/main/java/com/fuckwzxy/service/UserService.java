package com.fuckwzxy.service;

import com.fuckwzxy.bean.UserInfo;

public interface UserService  {
    void addUser(UserInfo userInfo);


    /**
     * 修改用户
     * @param userInfo 值
     * @return 结果字符串
     */
    String updateUser(UserInfo userInfo);
}
