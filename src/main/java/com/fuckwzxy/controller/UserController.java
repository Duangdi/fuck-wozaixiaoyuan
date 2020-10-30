package com.fuckwzxy.controller;

import cn.hutool.core.map.MapBuilder;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fuckwzxy.bean.ApiInfo;
import com.fuckwzxy.bean.SignMessage;
import com.fuckwzxy.bean.UserInfo;
import com.fuckwzxy.common.ApiConstant;
import com.fuckwzxy.common.Result;
import com.fuckwzxy.common.ResultCode;
import com.fuckwzxy.common.ResultFactory;
import com.fuckwzxy.mapper.ApiMapper;
import com.fuckwzxy.mapper.UserMapper;
import com.fuckwzxy.service.UserService;

import com.fuckwzxy.util.SendUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
