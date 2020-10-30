package com.fuckwzxy.common;


public class ResultFactory {
    /**
     原来错误 static Result result = new Result();
     Result result = new Result();
     并发啊
     会导致获取的是上一个接口的数据的啊
     */
    public static <T>Result success(T data, ResultCode resultCode){
        Result result = new Result();
        result.getMeta().put("msg",resultCode.getMsg());
        result.getMeta().put("status",resultCode.getCode());
        result.setData(data);
        return result;
    }

    public static Result fail(ResultCode resultCode){
        Result result = new Result();
        result.getMeta().put("msg",resultCode.getMsg());
        result.getMeta().put("status",resultCode.getCode());
        return result;
    }

    public static <T> Result fail(T meta){
        Result result = new Result();
        result.getMeta().put("msg",meta);
        result.getMeta().put("status", ResultCode.ERROR.getCode());
        return result;
    }
}
