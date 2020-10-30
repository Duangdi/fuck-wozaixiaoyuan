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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private ApiMapper apiInfoMapper;

    @Autowired
    SendUtil sendUtil;

    @Override
    public <T> Result addUser(UserInfo userInfo) {
        
        UserInfo user = userMapper.selectByPrimaryKey(userInfo.getId());
        if(user != null){
            return ResultFactory.fail(ResultCode.USER_PRESENCE);
        }

        userMapper.insert(userInfo);
        
        Map<Object, Object> map = MapBuilder.create()
                .put("id", userInfo.getId())
                .put("name", userInfo.getName())
                .put("password", userInfo.getPassword())
                .put("status",userInfo.getStatus())
                .map();
        
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

            int status = userMapper.updateByPrimaryKeySelective(userInfoUpdateVo);
            if(status == 0) return  ResultFactory.fail(ResultCode.UPDATE_FAIL);


            helper(userInfo);//避免你刚注册 无法触发打卡或晚签的尴尬


            Map<Object, Object> map = MapBuilder.create()
                    .put("id", userInfo.getId())
                    .put("name",userInfo.getName())
                    .put("password", userInfo.getPassword())
                    .put("token", userInfo.getToken())
                    .map();
            return ResultFactory.success(map, ResultCode.OK);
        }
    }


    public void helper(UserInfo userInfo) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");// 设置日期格式
        Date now = df.parse(df.format(new Date()));

        if(belongCalendar(now, df.parse("00:01"), df.parse("05:00"))){
            JudgeAndDo(userInfo,1);
        }else if(belongCalendar(now, df.parse("11:01"), df.parse("14:59"))){
            JudgeAndDo(userInfo,2);
        }else if(belongCalendar(now, df.parse("17:01"), df.parse("20:59"))){
            JudgeAndDo(userInfo,3);
        }
        if(belongCalendar(now, df.parse("20:06"), df.parse("21:59"))){
            JudgeAndSign(userInfo);
        }
    }

    public void JudgeAndDo(UserInfo userInfo,int seq){
        ApiInfo apiInfo = apiInfoMapper.selectByPrimaryKey(ApiConstant.NEED_CHECK);//获取三检状况API
        if (sendUtil.needCheck(apiInfo,userInfo, seq)) {
            sendUtil.sendCheckRequest(userInfo, apiInfoMapper.selectByPrimaryKey(ApiConstant.DO_CHECK), seq);
        }
    }

    public void JudgeAndSign(UserInfo userInfo){
        ApiInfo apiInfo = apiInfoMapper.selectByPrimaryKey(ApiConstant.GET_SIGN_MESSAGE);//获取三检状况API
        String body = sendUtil.GetJSON(userInfo,apiInfo);

        JSONObject data = (JSONObject) ((JSONArray) JSONUtil.parseObj(body).get("data")).get(0);

        if(Integer.parseInt(data.get("state").toString()) == 1){
            SignMessage signMessage = new SignMessage((String) data.getObj("id"), (String) data.get("logId"));
            ApiInfo signApiInfo = apiInfoMapper.selectByPrimaryKey(ApiConstant.DO_SIGN);//获取晚签到API
            sendUtil.sendSignRequest(userInfo,signApiInfo,signMessage);
        }
    }

    //https://blog.csdn.net/finaly_yu/article/details/87632726
    public boolean belongCalendar(Date nowTime, Date beginTime,Date endTime) {
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
