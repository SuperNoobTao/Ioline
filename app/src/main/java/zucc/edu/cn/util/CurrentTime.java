package zucc.edu.cn.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by QI on 2015/12/29.
 */
public class CurrentTime {

    public static final String 刚刚 = "刚刚";
    public static final String 分钟前 = "分钟前";
    public static final String 小时前 = "小时前";
    public static final String 天前 = "天前";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    public static String getTime(){
        java.util.Date utilDate = new java.util.Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        String datetime=formatter.format(utilDate);
        return  datetime;
    }

    public static long subreturnday(String starttime, String endtime){
        long diff=0;
        long days=0;

        try {
            DateFormat df = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
            Date d1 = df.parse(endtime);
            Date d2 = df.parse(starttime);
            diff = d1.getTime() - d2.getTime();
            days = diff / (1000 * 60 * 60 * 24);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }

    public static String subDay(Date realse_date){
//        2016-03-23 11:38:49

        java.util.Date utilDate = new java.util.Date();
        long diff = 0;
        long days = 0;
        long houer = 0;
        long min = 0;
        try {

            Date d1 = utilDate;       //现在日期
            Date d2 = realse_date;  //单发布日期

            diff = d1.getTime() - d2.getTime();
            days = diff / (1000 * 60 * 60 * 24);
            houer = diff/(1000 * 60 * 60 );
            min = diff/(1000 * 60 );

            if(min < 5){
                return 刚刚;
            }else if(min >= 5 && min < 60){
                return min+ 分钟前;
            }else if(min >= 60 && houer < 24){
                return  houer+ 小时前;
            }else if(houer >= 24 && days < 30){
                return days+ 天前;
            }else {
                SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日");
               return formatter.format(d2);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



}
