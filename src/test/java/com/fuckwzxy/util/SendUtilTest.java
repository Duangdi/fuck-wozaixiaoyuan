package com.fuckwzxy.util;

import cn.hutool.http.HttpRequest;
import com.fuckwzxy.bean.ApiInfo;
import com.fuckwzxy.bean.UserInfo;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class SendUtilTest {

    @Autowired
    SendUtil sendUtil;

    @Test
    void getAllNoSign() {
        //UserInfo userInfo = new UserInfo("17251104285","","123456","7ed683bb-30f3-4cf1-ab5f-d5608c664d98",0);
        UserInfo userInfo = new UserInfo("17251104285","","123456","3afd36cd-30d6-409b-9e29-3461e5d67895",0);
        //UserInfo userInfo = new UserInfo("17251104285","","123456","000",0);
        ApiInfo apiInfo = new ApiInfo(5,1,"https://student.wozaixiaoyuan.com/heat/getHeatUsers.json","获取班级未打卡名单","application/x-www-form-urlencoded","seq=3&date=20201030&type=0");
        List<String> list = sendUtil.getAllNoSign(apiInfo,userInfo,2);
        if(list == null){
            System.out.println("没有查询权限");
        }else{
            System.out.println("查了"+list.size()+"人");
        }

    }

    @Test
    void replaceSign() {
        UserInfo userInfo = new UserInfo("17251104285","","123456","7ed683bb-30f3-4cf1-ab5f-d5608c664d98",0);
        //UserInfo userInfo = new UserInfo("17251104285","","123456","3afd36cd-30d6-409b-9e29-3461e5d67895",0);

        //UserInfo userInfo = new UserInfo("17251104285","","123456","000",0);
        ApiInfo apiInfo = new ApiInfo(5,1,"https://student.wozaixiaoyuan.com/heat/getHeatUsers.json","获取班级未打卡名单","application/x-www-form-urlencoded","seq=3&date=20201030&type=0");
        List<String> list = sendUtil.getAllNoSign(apiInfo,userInfo,2);

        ApiInfo ai = new ApiInfo(6,1,"https://student.wozaixiaoyuan.com/heat/save.json","代打卡","application/x-www-form-urlencoded","answers=%5B%220%22%5D&seq=&temperature=36.4&userId=&latitude=&longitude=&country=%E4%B8%AD%E5%9B%BD&city=%E5%B9%BF%E5%B7%9E%E5%B8%82&district=%E6%B5%B7%E7%8F%A0%E5%8C%BA&province=%E5%B9%BF%E4%B8%9C%E7%9C%81&township=&street=&myArea=");
        sendUtil.replaceSign(ai,userInfo,3,list.get(list.size() - 1));

    }
}