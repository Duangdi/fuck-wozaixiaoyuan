package com.fuckwzxy.service;

import com.fuckwzxy.bean.UserInfo;
import com.fuckwzxy.common.Result;
import org.springframework.scheduling.annotation.Async;

import java.text.ParseException;

public interface UserService  {
    <T> Result addUser(UserInfo userInfo);

    <T> Result updateUser(UserInfo userInfo) throws ParseException;

    @Async
    void helper(UserInfo userInfo) throws ParseException;
}
