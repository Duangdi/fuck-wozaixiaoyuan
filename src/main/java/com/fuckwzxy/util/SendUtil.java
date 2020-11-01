package com.fuckwzxy.util;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fuckwzxy.bean.ApiInfo;
import com.fuckwzxy.bean.SignMessage;
import com.fuckwzxy.bean.UserInfo;
import com.fuckwzxy.common.ApiConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.sound.midi.Soundbank;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author wzh
 * 2020/9/23 19:15
 */
@Component
@Slf4j
public class SendUtil {

    @Async
    public void sendCheckRequest(UserInfo userInfo, ApiInfo apiInfo, int seq) {

        HttpRequest request = createHttpRequest(apiInfo,userInfo);
        //body
        request.body(apiInfo.getBody().replace("seq=3", "seq=" + seq));

        //return body
        request.execute();
    }

    /**
     * 获取签到的id和logId
     *
     * @param userInfo              user
     * @param getSignMessageApiInfo 接口
     */

    public SignMessage getSignMessage(UserInfo userInfo, ApiInfo getSignMessageApiInfo) {
        HttpRequest request = createHttpRequest(getSignMessageApiInfo,userInfo);
        //参数
        request.form("page", 1);
        request.form("size", 5);
        //得到返回的JSON并解析
        String body = request.execute().body();

        JSONObject data = (JSONObject) ((JSONArray) JSONUtil.parseObj(body).get("data")).get(0);
        return new SignMessage((String) data.getObj("id"), (String) data.get("logId"));
    }

    @Async
    public void sendSignRequest(UserInfo userInfo, ApiInfo signApiInfo, SignMessage signMessage) {
        HttpRequest request = createHttpRequest(signApiInfo,userInfo);
        //JSON data
        JSONObject data = new JSONObject();
        data.set("id", signMessage.getLogId());
        data.set("signId", signMessage.getId());
        data.set("latitude", "23.090164");
        data.set("longitude", "113.354053");
        data.set("country", "中国");
        data.set("province", "广东省");
        data.set("district", "海珠区");
        data.set("township", "官洲街道");
        data.set("city", "广州市");
        request.body(data.toString());
        request.execute();
    }

    public String GetJSON(UserInfo userInfo, ApiInfo getSignMessageApiInfo) {
        HttpRequest request = createHttpRequest(getSignMessageApiInfo,userInfo);
        //参数
        request.form("page", 1);
        request.form("size", 5);
        //得到返回的JSON并解析
        String body = request.execute().body();

        return body;
    }

    public boolean needCheck(ApiInfo apiInfo,UserInfo userInfo, int seq){
        HttpRequest request = createHttpRequest(apiInfo,userInfo);
        HttpResponse response = request.execute();
        String body = response.body();
        JSONObject data = (JSONObject) ((JSONArray) JSONUtil.parseObj(body).get("data")).get(seq - 1);
        return Integer.parseInt(data.get("state").toString()) == 0;
    }

    public List<String> getAllNoSign(ApiInfo apiInfo,UserInfo userInfo, int seq){
        HttpRequest request = createHttpRequest(apiInfo,userInfo);
        //body
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");// 设置日期格式
        String date = df.format(new Date());

        String httpbody = apiInfo.getBody().replace("seq=3", "seq=" + seq);
        httpbody = httpbody.replace("date=20201030","date="+date);
        request.body(httpbody);

        String body = request.execute().body();
        if(!JSONUtil.parseObj(body).containsKey("data") ){
            return null;
        }else{
            List<String> list = new ArrayList<>();
            JSONArray arr = (JSONArray) JSONUtil.parseObj(body).get("data");
            for (int i = 0; i < arr.size() ; i++) {
                JSONObject stu =  (JSONObject) arr.get(i);
                list.add((String) stu.get("userId"));
            }
            return  list;
        }
    }

    public void replaceSign(ApiInfo apiInfo,UserInfo userInfo, int seq,String userId){

        HttpRequest request = createHttpRequest(apiInfo,userInfo);
        //body
        String httpbody = apiInfo.getBody().replace("seq=", "seq=" + seq);
        httpbody = httpbody.replace("userId=","userId="+userId);

        request.body(httpbody);

        //return body
        request.execute();
    }

    /**
     * 创建HttpRequest对象
     */
    private HttpRequest createHttpRequest(ApiInfo apiInfo,UserInfo userInfo) {
        //请求方法和请求url
        HttpRequest request = request = apiInfo.getMethod().equals(ApiConstant.METHOD_GET)?HttpRequest.get(apiInfo.getUrl()):HttpRequest.post(apiInfo.getUrl());
        //报文头
        request.contentType(apiInfo.getContenttype());
        //token
        request.header("token", userInfo.getToken().trim());//总有混蛋 带空格存进来

        return request;
    }


}
