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

    public String GetJSON(UserInfo userInfo, ApiInfo getSignMessageApiInfo) {
        HttpRequest request = createHttpRequest(getSignMessageApiInfo,userInfo);

        //参数
        request.form("page", 1);
        request.form("size", 5);

        //得到返回的JSON并解析
        String body = request.execute().body();

        return body;
        /**
         * JSONObject data = (JSONObject) ((JSONArray) JSONUtil.parseObj(body).get("data")).get(0);
         * return new SignMessage((String) data.getObj("id"), (String) data.get("logId"));
         */
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

    public boolean needSign(UserInfo userInfo, ApiInfo getSignMessageApiInfo) {
        HttpRequest request = createHttpRequest(getSignMessageApiInfo,userInfo);

        //参数
        request.form("page", 1);
        request.form("size", 5);

        //得到返回的JSON并解析
        String body = request.execute().body();

        JSONObject data = (JSONObject) ((JSONArray) JSONUtil.parseObj(body).get("data")).get(0);

        return Integer.parseInt(data.get("state").toString()) == 0;
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
        //{"name":"李广", "values":[1,2,45,"你好"] }

        request.execute();

    }

    public boolean needCheck(ApiInfo apiInfo,UserInfo userInfo, int seq){
        HttpRequest request = createHttpRequest(apiInfo,userInfo);
        HttpResponse response = request.execute();
        String body = response.body();
        JSONObject data = (JSONObject) ((JSONArray) JSONUtil.parseObj(body).get("data")).get(seq - 1);
        return Integer.parseInt(data.get("state").toString()) == 0;
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
        request.header("token", userInfo.getToken());

        return request;
    }


}
