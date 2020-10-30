package com.fuckwzxy.bean;

import lombok.*;

import javax.persistence.Id;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserInfo {
    @Id
    private String id;

    private String name;
    private String password;
    private String token;

    private int status;

}
