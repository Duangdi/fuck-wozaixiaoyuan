package com.fuckwzxy.controller;

import java.text.ParseException;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import com.fuckwzxy.bean.UserInfo;
import com.fuckwzxy.common.Result;
import com.fuckwzxy.service.UserService;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@CrossOrigin
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public  Result register(@RequestBody UserInfo userInfo){
        return userService.addUser(userInfo);
    }

    @PostMapping("/login")
    public Result LoginAndUpdate(@RequestBody UserInfo uservo) throws ParseException {
        return userService.updateUser(uservo);
    }

}
