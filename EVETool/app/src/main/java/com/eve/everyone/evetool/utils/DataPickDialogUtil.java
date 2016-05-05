package com.eve.everyone.evetool.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.maxfun.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Tao on 2015/11/17 0017.
 * 日期选择框
 */
public class DataPickDialogUtil implements DatePicker.OnDateChangedListener {

    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static String defaultDateTime = "1990-01-01";       //默认日期值
    private Context mContext;
    private String initDateTime;       //初始日期值
    private String dateTime;            //用户选中的日期
    private AlertDialog ad;


    private DatePicker dp;


    /**
     * 日期弹出选择框构造函数
     *
     * @param context      context
     * @param initDateTime 初始日期时间值，作为弹出窗口的标题和日期时间初始值
     */
    public DataPickDialogUtil(Context context, String initDateTime) {
        mContext = context;
        this.initDateTime = initDateTime;
    }

    /**
     * 弹出日期选择框
     *
     * @param inputData 需要设置时间的编辑框
     * @return
     */
    public AlertDialog dataPickDialog(final EditText inputData) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_data_pick, null);
        dp = (DatePicker) view.findViewById(R.id.dialog_datapick);
        initData(dp);
        //ad = new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.dialog)).setTitle(dateTime).setView(view)
        ad = new AlertDialog.Builder(mContext).setTitle(dateTime).setView(view)
                .setPositiveButton(R.string.txt_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //设置时间
                        inputData.setText(dateTime);
                    }
                }).setNegativeButton(R.string.txt_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //取消设置
                        inputData.setText(initDateTime);
                    }
                }).show();
        return ad;
    }

    /**
     * 初始化显示的日期
     *
     * @param dp
     */
    private void initData(final DatePicker dp) {
        Calendar calendar = Calendar.getInstance();
        if (!(initDateTime == null || TextUtils.isEmpty(initDateTime))) {
            //如果时间不为空，则显示已设置好的时间
            calendar = getCalendarByInintData(initDateTime);
            dateTime = initDateTime;
        } else {
            //如果时间为空，则显示默认的时间值
            calendar = getCalendarByInintData(defaultDateTime);
            dateTime = defaultDateTime;
        }
        dp.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), this);
        LogUtils.d("alert", "day of month:" + dp.getDayOfMonth());
    }

    /**
     * 将日期xxxx-xx-xx转化,并赋值给calendar
     *
     * @param initDateTime 日期时间值 字符串型
     * @return Calendar
     */
    private Calendar getCalendarByInintData(String initDateTime) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(dateFormat.parse(initDateTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar;
    }

    @Override
    public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
        // 获得日历实例
        Calendar calendar = Calendar.getInstance();
        calendar.set(datePicker.getYear(), datePicker.getMonth(),
                datePicker.getDayOfMonth());
        dateTime = dateFormat.format(calendar.getTime());
        ad.setTitle(dateTime);
        LogUtils.d("alert", "date pick");
    }


}
