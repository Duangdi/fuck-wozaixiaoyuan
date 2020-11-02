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
import com.fuckwzxy.service.CheckService;
import com.fuckwzxy.util.SendUtil;
import com.fuckwzxy.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import javax.annotation.Resource;
import java.text.ParseException;
import java.util.List;

/**
 * @author wzh
 * 2020/9/23 19:05
 */
@Service
public class CheckServiceImpl implements CheckService {

    @Resource
    UserMapper userMapper;

    @Resource
    ApiMapper apiInfoMapper;

    @Autowired
    SendUtil sendUtil;

    @Autowired
    TimeUtil timeUtil;

    @Override
    public void ThreeCheck(int seq){
        List<UserInfo> userInfoList = userMapper.selectAll();
        ApiInfo apiInfo = apiInfoMapper.selectByPrimaryKey(ApiConstant.DO_CHECK);

        for (UserInfo userInfo : userInfoList) {
            sendUtil.sendCheckRequest(userInfo, apiInfo, seq);
        }
    }

    @Override
    public void singIn() {
        List<UserInfo> userInfoList = userMapper.selectAll();

        ApiInfo getSignMessageApiInfo = apiInfoMapper.selectByPrimaryKey(ApiConstant.GET_SIGN_MESSAGE);
        ApiInfo signApiInfo = apiInfoMapper.selectByPrimaryKey(ApiConstant.DO_SIGN);


        for (UserInfo userInfo : userInfoList) {
            String body = sendUtil.GetJSON(userInfo,getSignMessageApiInfo);
            JSONObject data =  null;
            if(!JSONUtil.parseObj(body).containsKey("data") ) {
                continue;
            }else{
                data = (JSONObject) ((JSONArray) JSONUtil.parseObj(body).get("data")).get(0);
                if(Integer.parseInt(data.get("state").toString()) != 1) {
                    continue;
                }
            }

            SignMessage signMessage = new SignMessage((String) data.getObj("id"), (String) data.get("logId"));
            sendUtil.sendSignRequest(userInfo, signApiInfo, signMessage);
        }
    }

    @Override
    public void replaceAllSign() throws ParseException {
        //查班长
        Example example = new Example(UserInfo.class);
        example.createCriteria().andEqualTo("status", 1);
        List<UserInfo> userInfoList = userMapper.selectByExample(example);

        //查两个api
        ApiInfo noSignApi = apiInfoMapper.selectByPrimaryKey(ApiConstant.GET_All_NO_SIGN);
        ApiInfo replaySignApi = apiInfoMapper.selectByPrimaryKey(ApiConstant.REPLACE_SIGN);

        //时间段
        int seq = timeUtil.getSeq();
        if(seq == 0 || seq == 4) return ;//不在时间段内 跳出

        for (UserInfo userInfo : userInfoList) {
            List<String> noSignlist = sendUtil.getAllNoSign(noSignApi,userInfo, seq);
            if(noSignlist == null)  continue; //班长token失效

            for(String noSignStr : noSignlist){
                sendUtil.replaceSign(replaySignApi,userInfo,seq,noSignStr);
            }
        }
    }
}
