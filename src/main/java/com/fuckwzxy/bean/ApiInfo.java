package com.fuckwzxy.bean;

import lombok.*;

import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ApiInfo {
    /**
     *
     */
    @Id
    private Integer id;

    /**
     * 请求方法  0get  1post
     */
    private Integer method;

    /**
     * 地址
     */
    private String url;

    /**
     * 名字
     */
    private String name;

    /**
     * 服务端发送的类型及采用的编码方式
     */
    private String contenttype;


    /**
     * 方法体
     */
    private String body;

}
