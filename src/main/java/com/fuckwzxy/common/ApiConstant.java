package com.fuckwzxy.common;

/**
 * @author wzh
 * 2020/9/23 20:04
 */
public class ApiConstant {

    //请求方法--GET
    public static final Integer METHOD_GET = 0;

    //请求方法--POST
    public static final Integer METHOD_POST = 1;

    //三检接口
    public static final int DO_CHECK = 1;

    //监测三检
    public static final int NEED_CHECK = 2;

    //获取签到信息
    public static final int GET_SIGN_MESSAGE = 3;

    //晚签到
    public static final int DO_SIGN = 4;

    //获取所有未打卡
    public static final int GET_All_NO_SIGN = 5;

    //代替打卡
    public static final int REPLACE_SIGN = 6;

    //监测token是否有效
    public static final int JUDGE_IVAILD = 7;
}
