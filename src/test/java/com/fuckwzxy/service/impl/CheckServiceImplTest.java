package com.fuckwzxy.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fuckwzxy.bean.ApiInfo;
import com.fuckwzxy.bean.SignMessage;
import com.fuckwzxy.bean.UserInfo;
import com.fuckwzxy.common.ApiConstant;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class CheckServiceImplTest {

    @Resource
    UserMapper userMapper;

    @Resource
    ApiMapper apiInfoMapper;

    @Autowired
    SendUtil sendUtil;

    @Test
    void singIn() {
        List<UserInfo> userInfoList = userMapper.selectAll();

        ApiInfo getSignMessageApiInfo = apiInfoMapper.selectByPrimaryKey(ApiConstant.GET_SIGN_MESSAGE);
        ApiInfo signApiInfo = apiInfoMapper.selectByPrimaryKey(ApiConstant.DO_SIGN);


        for (UserInfo userInfo : userInfoList) {
            String body = sendUtil.GetJSON(userInfo,getSignMessageApiInfo);
            JSONObject data =  null;
            if(!JSONUtil.parseObj(body).containsKey("data") ) {
                System.out.println("这个b的token失效");
                continue;
            }else{
                data = (JSONObject) ((JSONArray) JSONUtil.parseObj(body).get("data")).get(0);
                if(Integer.parseInt(data.get("state").toString()) != 1) {
                    System.out.println("这个b已打过卡");
                    continue;
                }
            }

            SignMessage signMessage = new SignMessage((String) data.getObj("id"), (String) data.get("logId"));
            sendUtil.sendSignRequest(userInfo, signApiInfo, signMessage);
        }
    }

    @Test
    void replaceAllSign() throws ParseException {
        //查班长
        Example example = new Example(UserInfo.class);
        example.createCriteria().andEqualTo("status",1);
        List<UserInfo> userInfoList = userMapper.selectByExample(example);
        System.out.println(userInfoList.get(0).getToken());
        //查两个api
        ApiInfo noSignApi = apiInfoMapper.selectByPrimaryKey(5);
        ApiInfo replaySignApi = apiInfoMapper.selectByPrimaryKey(6);

        //时间段
        int seq = helper();
        if(seq == 0) return ;
        System.out.println("时间段序号"+seq);

        for (UserInfo userInfo : userInfoList) {
            List<String> noSignlist = sendUtil.getAllNoSign(noSignApi,userInfo, seq);
            if(noSignlist == null) {
                System.out.println("这个btoken失效");
                continue;
            }
            for(String noSign : noSignlist){
                System.out.println(noSign);
                sendUtil.replaceSign(replaySignApi,userInfo,seq,noSign);
            }
        }
    }

    public int helper() throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");// 设置日期格式
        Date now = df.parse(df.format(new Date()));

        if(belongCalendar(now, df.parse("00:01"), df.parse("08:59"))){
            return 1;
        }else if(belongCalendar(now, df.parse("11:01"), df.parse("14:59"))){
            return 2;
        }else if(belongCalendar(now, df.parse("17:01"), df.parse("20:59"))){
            return 3;
        }

        return 0;
    }

    //https://blog.csdn.net/finaly_yu/article/details/87632726
    public boolean belongCalendar(Date nowTime, Date beginTime, Date endTime) {
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(beginTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }
}