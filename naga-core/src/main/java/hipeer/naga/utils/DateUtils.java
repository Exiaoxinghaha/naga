package hipeer.naga.utils;

import cn.hutool.core.date.DateUtil;

import java.util.Date;

public class DateUtils {

    public static String getNowDate(){
        return DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");
    }

    public static String format(int timestamp){
        return DateUtil.format(new Date(timestamp), "yyyy-MM-dd HH:mm:ss");
    }
}
