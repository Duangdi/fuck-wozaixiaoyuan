package com.fuckwzxy.common;


import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public enum  ResultCode {
    /**
     * 暂无
     */
    OK(200, "OK"),
    TOKEN_INVALID(400,"TOKEN无效"),
    REGISTER_FAIL(400,"注册失败"),
    CREATED(201, "Created"),
    ERROR(400,"error"),
    NO_LOGIN(400,"获取token失败"),
    CATEGORY_EXISTS(400,"当前分类已存在"),
    GOOD_EXISTS(400,"当前商品已经存在"),
    GET_AUTHORIZATION_FAIL(400,"获取权限列表失败"),
    USER_NO_ROLE(400,"当前用户没有角色,无法获取菜单"),
    UPDATE_FAIL(400,"更新失败"),
    DELETE_FAIL(400,"删除失败"),
    UPLOAD_FAIL(400,"上传失败"),
    USER_PRESENCE (400,"用户已存在"),
    UnknownAccountException(400,"用户不存在"),
    UnAuthentication(400,"用户未认证"),
    IncorrectCredentialsException(400,"密码错误"),
    DisabledAccountException(400,"账户被冻结"),
    ExpiredCredentialsException(401,"token过期"),
    UnauthorizedException(401,"权限不足"),
    PARSE_FAILED(400,"token解析异常");

    private int code;
    private String msg;

    ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }


}
