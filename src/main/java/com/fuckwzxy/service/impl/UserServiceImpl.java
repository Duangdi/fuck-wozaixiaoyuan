package com.fuckwzxy.service.impl;

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
import com.fuckwzxy.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private ApiMapper apiInfoMapper;

    @Autowired
    SendUtil sendUtil;

    @Autowired
    TimeUtil timeUtil;

    @Autowired @Lazy
    UserService userService;


    @Override
    public <T> Result addUser(UserInfo userInfo) {
        
        UserInfo user = userMapper.selectByPrimaryKey(userInfo.getId());
        if(user != null){
            return ResultFactory.fail(ResultCode.USER_PRESENCE);
        }

        try {
            userMapper.insertSelective(userInfo);
        }catch (Exception e){
            return ResultFactory.fail(ResultCode.REGISTER_FAIL);
        }


        Map<Object, Object> map = MapBuilder.create().put("id", userInfo.getId()).put("name", userInfo.getName())
                               .put("password", userInfo.getPassword()).put("status",userInfo.getStatus()).map();

        return ResultFactory.success(map,ResultCode.OK);
    }

    @Override
    public <T>Result updateUser(UserInfo userInfoUpdateVo) throws ParseException {

        //学号和密码校验
        Example example = new Example(UserInfo.class);
        example.createCriteria().andEqualTo("id", userInfoUpdateVo.getId()).andEqualTo("password", userInfoUpdateVo.getPassword());
        UserInfo userInfo = userMapper.selectOneByExample(example);

        if(userInfo == null ){//学号或密码错误
            return ResultFactory.fail(ResultCode.IncorrectCredentialsException);
        }else{//更新token值
            //防止有人粘多了空格进来
            userInfoUpdateVo.setToken(userInfoUpdateVo.getToken().trim());

            //获取校验token API
            ApiInfo apiInfo = apiInfoMapper.selectByPrimaryKey(ApiConstant.JUDGE_IVAILD);
            if(!sendUtil.JudgeTokenIsValid(apiInfo,userInfoUpdateVo)) return ResultFactory.fail(ResultCode.TOKEN_INVALID);

            //更新token
            int status = userMapper.updateByPrimaryKeySelective(userInfoUpdateVo);
            if(status == 0) return  ResultFactory.fail(ResultCode.UPDATE_FAIL);


            log.info(Thread.currentThread().getName());

            //异步执行  避免你刚注册 无法触发打卡或晚签的尴尬
            userService.helper(userInfo);

            Map<Object, Object> map = MapBuilder.create().put("id", userInfo.getId()).put("name",userInfo.getName())
                                   .put("password", userInfo.getPassword()).put("token", userInfo.getToken()).map();

            return ResultFactory.success(map, ResultCode.OK);
        }
    }

    public void helper(UserInfo userInfo) throws ParseException {

        int seq = timeUtil.getSeq();

        if(seq == 0) return;
        if(seq == 1) JudgeAndDo(userInfo,seq);
        else if(seq == 2) JudgeAndDo(userInfo,seq);
        else if(seq == 3) JudgeAndDo(userInfo,seq);

        if(seq == 4) JudgeAndSign(userInfo);

    }


    public void JudgeAndDo(UserInfo userInfo,int seq){
        //获取三检状况API
        ApiInfo JudgeApi = apiInfoMapper.selectByPrimaryKey(ApiConstant.NEED_CHECK);
        //获取三检API
        ApiInfo DoCheckApi = apiInfoMapper.selectByPrimaryKey(ApiConstant.DO_CHECK);

        if (sendUtil.needCheck(JudgeApi,userInfo, seq)) {
            sendUtil.sendCheckRequest(userInfo,DoCheckApi , seq);
            log.info(userInfo.getId()+"已经打卡");
        }
    }

    public void JudgeAndSign(UserInfo userInfo){
        //获取三检状况API
        ApiInfo apiInfo = apiInfoMapper.selectByPrimaryKey(ApiConstant.GET_SIGN_MESSAGE);
        String body = sendUtil.GetJSON(userInfo,apiInfo);

        if(!JSONUtil.parseObj(body).containsKey("data")) return;
        JSONObject data = (JSONObject) ((JSONArray) JSONUtil.parseObj(body).get("data")).get(0);

        if(Integer.parseInt(data.get("state").toString()) == 1){
            SignMessage signMessage = new SignMessage((String) data.getObj("id"), (String) data.get("logId"));
            ApiInfo signApiInfo = apiInfoMapper.selectByPrimaryKey(ApiConstant.DO_SIGN);//获取晚签到API
            sendUtil.sendSignRequest(userInfo,signApiInfo,signMessage);
        }
    }
}
