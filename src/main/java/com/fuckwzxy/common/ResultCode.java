package com.fuckwzxy.common;


import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public enum  ResultCode {

    OK(200, "OK"),
    TOKEN_INVALID(401,"TOKEN无效"),
    IncorrectCredentialsException(400,"密码错误"),
    REGISTER_FAIL(400,"注册失败"),
    CREATED(201, "Created"),
    ERROR(400,"error"),
    NO_LOGIN(400,"获取token失败"),
    UPDATE_FAIL(400,"更新失败"),
    USER_PRESENCE (400,"用户已存在"),
    UnknownAccountException(400,"用户不存在"),
    UnAuthentication(400,"用户未认证"),
    ExpiredCredentialsException(401,"token过期");

    private int code;
    private String msg;

    ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }


}
