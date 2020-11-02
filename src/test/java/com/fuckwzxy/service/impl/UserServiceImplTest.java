package com.fuckwzxy.service.impl;

import cn.hutool.core.map.MapBuilder;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fuckwzxy.bean.ApiInfo;
import com.fuckwzxy.bean.SignMessage;
import com.fuckwzxy.bean.UserInfo;
import com.fuckwzxy.common.ApiConstant;
import com.fuckwzxy.common.ResultCode;
import com.fuckwzxy.common.ResultFactory;
import com.fuckwzxy.mapper.ApiMapper;
import com.fuckwzxy.mapper.UserMapper;
import com.fuckwzxy.util.SendUtil;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserServiceImplTest {

    @Resource
    UserMapper userMapper;

    @Resource
    ApiMapper apiInfoMapper;

    @Autowired
    SendUtil sendUtil;

    @Test
    void updateUser() {
        UserInfo userInfoUpdateVo = new UserInfo("17251104285","","123456","7ed683bb-30f3-4cf1-ab5f-d5608c664d98",0);
        //学号和密码校验
        Example example = new Example(UserInfo.class);
        example.createCriteria().andEqualTo("id", userInfoUpdateVo.getId()).andEqualTo("password", userInfoUpdateVo.getPassword());
        UserInfo userInfo = userMapper.selectOneByExample(example);

        if(userInfo == null ){//学号或密码错误
            //return ResultFactory.fail(ResultCode.IncorrectCredentialsException);
        }else{//更新token值

            ApiInfo apiInfo = apiInfoMapper.selectByPrimaryKey(7);//获取校验token API
            //if(!sendUtil.JudgeTokenIsValid(apiInfo,userInfoUpdateVo)) return ResultFactory.fail(ResultCode.TOKEN_INVALID);

            int status = userMapper.updateByPrimaryKeySelective(userInfoUpdateVo);
            //if(status == 0) return  ResultFactory.fail(ResultCode.UPDATE_FAIL);


            //明明都校验了token是否有效 判断是否打卡还是有空指针异常
            //helper(userInfo);//避免你刚注册 无法触发打卡或晚签的尴尬

        }
    }

    @Test
    void helper() {
    }

    @Test
    void judgeAndDo() {
    }

    @Test
    void belongCalendar() {
    }

    @Test
    void judgeAndSign() {
    }

    @Test
    void judgeAllToken() {
        List<UserInfo> userInfoList = userMapper.selectAll();

        ApiInfo apiInfo = apiInfoMapper.selectByPrimaryKey(7);
        ApiInfo signApiInfo = apiInfoMapper.selectByPrimaryKey(ApiConstant.DO_SIGN);


        for (UserInfo userInfo : userInfoList) {
            if(!sendUtil.JudgeTokenIsValid(apiInfo,userInfo)) {
                System.out.println(userInfo.getName()+"的token失效");
            }else{
                System.out.println(userInfo.getName()+"的token有效");
            }

        }
    }
}