package com.fuckwzxy.service;

import com.fuckwzxy.bean.UserInfo;
import com.fuckwzxy.common.Result;

import java.text.ParseException;

public interface UserService  {
    <T> Result addUser(UserInfo userInfo);

    <T> Result updateUser(UserInfo userInfo) throws ParseException;
}
