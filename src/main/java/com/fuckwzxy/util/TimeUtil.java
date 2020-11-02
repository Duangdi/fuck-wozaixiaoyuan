package com.fuckwzxy.util;

import com.fuckwzxy.bean.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Component
@Slf4j
public class TimeUtil {

    public int getSeq() throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");// 设置日期格式
        Date now = df.parse(df.format(new Date()));

        if(belongCalendar(now, df.parse("00:01"), df.parse("08:59"))){
            return 1;
        }else if(belongCalendar(now, df.parse("11:01"), df.parse("14:59"))){
            return 2;
        }else if(belongCalendar(now, df.parse("17:01"), df.parse("20:59"))){
            return 3;
        }
        if(belongCalendar(now, df.parse("20:06"), df.parse("21:59"))){
            return 4;
        }
        return 0;
    }
    //https://blog.csdn.net/finaly_yu/article/details/87632726
    public boolean belongCalendar(Date nowTime, Date beginTime,Date endTime) {
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(beginTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }
}
