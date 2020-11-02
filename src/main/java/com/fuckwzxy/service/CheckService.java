package com.fuckwzxy.service;

import java.text.ParseException;
import java.util.List;

/**
 * @author wzh
 * 2020/9/23 19:03
 */
public interface CheckService {

    /**
     * 三检
     */
    void ThreeCheck(int seq);

    /**
     * 签到
     */
    void singIn();


    /**
     * 代签
     */
    void replaceAllSign() throws ParseException;
}
