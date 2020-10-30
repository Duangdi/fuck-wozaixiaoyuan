package com.fuckwzxy.service;

import java.util.List;

/**
 * @author wzh
 * 2020/9/23 19:03
 */
public interface CheckService {
    /**
     * 晨检
     */
    void morningCheck();

    /**
     * 午检
     */
    void noonCheck();

    /**
     * 晚检
     */
    void eveningCheck();

    /**
     * 签到
     */
    void singIn();


}
