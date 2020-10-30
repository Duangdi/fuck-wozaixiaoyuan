package com.fuckwzxy.scheduled;

import com.fuckwzxy.service.CheckService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


/**
 * @author wzh
 * 2020/9/23 19:01
 */
@Component
@Slf4j
public class DailyScheduled {

    @Autowired
    CheckService checkService;

    //0点15秒
    @Scheduled(cron = "15 0 0 * * *")
    public void morningCheck() {
        log.info("开始进行晨检......");
        checkService.morningCheck();
    }

    //11点15秒
    @Scheduled(cron = "15 0 11 * * *")
    public void noonCheck() {
        log.info("开始进行午检......");
        checkService.noonCheck();
    }

    //17点15秒
    @Scheduled(cron = "15 0 17 * * *")
    public void eveningCheck() {
        log.info("开始进行晚检......");
        checkService.eveningCheck();
    }

    //20点5分10秒
    @Scheduled(cron = "10 5 20 * * *")
    public void signIn() {
        log.info("开始进行签到......");
        checkService.singIn();
    }

}
