package com.eve.everyone.evetool.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;

import com.maxfun.entityparams.MyConstants;

import java.util.Calendar;
import java.util.Random;
import java.util.TimeZone;

/**
 * Created by Tao on 2015/11/30 0030.
 * 随机时间执行任务
 */
public class TimingUtils {

    private static final long ONE = 3600000L;
    private static final long TEN = 36000000L;
    private static final long ELEVEN = 39600000L;
    private static final long EIGHT = 28800000L;

    /**
     * 使用AlarmManager获取更新信息
     * <p/>
     * setRepeating方法有四个参数，含义如下：
     * <p/>
     * type:闹钟类型，有四个可选值
     * ELAPSED_REALTIME：以手机开机的时间为基准
     * ELAPSED_REALTIME_WAKEUP：以手机开机的时间为基准，并且可以在休眠时发出广播
     * RTC：以UTC标准时间为基准
     * RTC_WAKEUP：以UTC标准时间为基准，并且可以在休眠时发出广播
     * triggerAtTime：第一次启动的时间，单位是毫秒。如果当前时间超过了triggerTime，就会立即启动这个定时提醒。此时应该设置为triggerTime + INTERVAL， 即：首次启动时间 + 循环间隔时间，也就是从下个循环开始启动。
     * 如果type是前两种，就需要提前算好开机后多久启动，如果超过了设定的时间会立即启动；
     * 如果type是后两种，需要设置一个标准时间的毫秒数。
     * interval：循环启动的间隔时间，单位是毫秒。也就是第一次启动之后，每隔多长时间启动一次。
     * <p/>
     * operation：一个PendingIntent对象。这里用来封装BroadcastReceiver。
     *
     * @param context context
     */
    public static long startAlarmPush(Context context) {
        //随机设置更新时间（最后一小时）
        Random random = new Random();
        int randomNum = random.nextInt(3600 + 1) * 1000;
        long nowTime = System.currentTimeMillis();
        //设置当天时间的阈值
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTimeInMillis(System.currentTimeMillis());
        //设置时区
        startCalendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        startCalendar.set(Calendar.HOUR_OF_DAY, 10);
        startCalendar.set(Calendar.MINUTE, 0);
        startCalendar.set(Calendar.SECOND, 0);
        startCalendar.set(Calendar.MILLISECOND, 0);
        long startTime = startCalendar.getTimeInMillis();
        long endTime = startTime + ONE;

        LogUtils.d("startTime:" + DateUtil.getDateTimeCheckStr(startTime) + ":" + "endTime:" + DateUtil.getDateTimeCheckStr(endTime) + "nowTime:" + DateUtil.getDateTimeCheckStr(nowTime));
        if (nowTime < startTime) {
            //时间之前(测试2000，正式数据应为1小时内的随机值)
            startTime += randomNum;
        } else if (nowTime > (endTime - 25000)) {
            //时间之后，设置闹钟为第二天的指定时间,减去25秒是为了下面要加上20秒，此处时间要求并不严格。
            startCalendar.add(Calendar.DAY_OF_MONTH, 1);
            startTime = startCalendar.getTimeInMillis();
            startTime += randomNum;
        } else {
            //在指定时间段内（在此为10-11点之间）,那么将当前时间加上20秒，设置为闹钟
            startTime = nowTime + 20000;
        }

        LogUtils.d("最终的startTime:" + DateUtil.getDateTimeCheckStr(startTime));

        AlarmManager am = (AlarmManager) context
                .getSystemService(Service.ALARM_SERVICE);
        Intent intent = new Intent(MyConstants.AUTO_CHECK_APP_VERSION);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 1, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        am.setRepeating(AlarmManager.RTC_WAKEUP, startTime, AlarmManager.INTERVAL_DAY, pIntent);
        return startTime;
    }

    /**
     * 停止定时器AlarmManager
     *
     * @param context
     */
    public static void stopAlarmPush(Context context) {
        AlarmManager am = (AlarmManager) context
                .getSystemService(Service.ALARM_SERVICE);
        Intent intent = new Intent(MyConstants.AUTO_CHECK_APP_VERSION);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 1, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        am.cancel(pIntent);
    }

}
