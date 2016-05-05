package com.eve.everyone.evetool.utils;


import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;

import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToolUtils {

    private static int srollHeight = 0;    //输入法弹出高度
    private static ViewTreeObserver.OnGlobalLayoutListener mLayoutChangeListener;

    public static void removeViewListener(View root) {
        //移除布局变化监听
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            root.getViewTreeObserver().removeOnGlobalLayoutListener(mLayoutChangeListener);
        } else {
            root.getViewTreeObserver().removeGlobalOnLayoutListener(mLayoutChangeListener);
        }
    }

    /**
     * @param root 最外层布局，需要调整的布局
     * @param view 被键盘遮挡的view，滚动root,使view在root可视区域的底部
     */
    public static void registerViewListener(final Context mContext, final View root, final View view) {
        // 注册一个回调函数，当在一个视图树中全局布局发生改变或者视图树中的某个视图的可视状态发生改变时调用这个回调函数。
        mLayoutChangeListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                // 获取root在窗体的可视区域
                root.getWindowVisibleDisplayFrame(rect);
//                activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
                //获取屏幕高度
                int screenHeight = DensityUtil.getScreenHeight(mContext);
                //获得屏幕高度和Window可见区域高度差值
                int heightDifference = screenHeight - (rect.bottom - rect.top);
                LogUtils.d("最外层的高度" + root.getRootView().getHeight() + ";\theightDifference:" + heightDifference);
                //如果差值大于整个屏幕高度的1/3，则表示软键盘显示中，否则软键盘为隐藏状态
                if (heightDifference > screenHeight / 3) {
                    LogUtils.d("键盘的状态:true");
                    //软键盘弹出来的时候
                    if (srollHeight == 0) {
                        int[] location = new int[2];
                        // 获取scrollToView在窗体的坐标
                        view.getLocationInWindow(location);
                        // 计算root滚动高度，使scrollToView在可见区域的底部
                        srollHeight = (location[1] + view
                                .getHeight()) - rect.bottom;
                    }
                    //对同一种输入法有效，但是如果手机在输入密码时会弹出自带安全输入法，则会出现位置上的微小偏差，不影响使用
                    root.scrollTo(0, srollHeight);
                    LogUtils.d("onBackPressed__上移srollHeight" + srollHeight);
                } else {
                    LogUtils.d("键盘的状态:false");
                    // 软键盘没有弹出来的时候
                    root.scrollTo(0, 0);
                }

            }
        };
        root.getViewTreeObserver().addOnGlobalLayoutListener(mLayoutChangeListener);
    }

    public static int getSrollHeight() {
        return srollHeight;
    }

    public static void setSrollHeight(int mSrollHeight) {
        srollHeight = mSrollHeight;
    }

    /**
     * 垂直绘图
     *
     * @param height 需要填充的区域高度
     * @param src    填充的图片
     * @return 绘制好的资源
     */
    public static Bitmap createRepeater(int height, Bitmap src) {
        int count = (height + src.getHeight() - 1) / src.getHeight(); //计算出平铺填满所给width（宽度）最少需要的重复次数
        Bitmap bitmap = Bitmap.createBitmap(src.getWidth(), src.getHeight() * count, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        for (int idx = 0; idx < count; ++idx) {
            canvas.drawBitmap(src, 0, idx * src.getHeight(), null);
        }
        return bitmap;
    }

//    /**
//     * Map a value within a given range to another range.
//     *
//     * @param value    the value to map
//     * @param fromLow  the low end of the range the value is within
//     * @param fromHigh the high end of the range the value is within
//     * @param toLow    the low end of the range to map to
//     * @param toHigh   the high end of the range to map to
//     * @return the mapped value
//     */
//    public static double mapValueFromRangeToRange(
//            double value,
//            double fromLow,
//            double fromHigh,
//            double toLow,
//            double toHigh) {
//        double fromRangeSize = fromHigh - fromLow;
//        double toRangeSize = toHigh - toLow;
//        double valueScale = (value - fromLow) / fromRangeSize;
//        return toLow + (valueScale * toRangeSize);
//    }
//
//    /**
//     * set margins of the specific view
//     *
//     * @param target
//     * @param l
//     * @param t
//     * @param r
//     * @param b
//     */
//    public static void setMargin(View target, int l, int t, int r, int b) {
//        if (target.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
//            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) target.getLayoutParams();
//            p.setMargins(l, t, r, b);
//            target.requestLayout();
//        }
//    }
//
//    /**
//     * convert drawable to bitmap
//     *
//     * @param drawable
//     * @return
//     */
//    public static Bitmap drawableToBitmap(Drawable drawable) {
//        int width = drawable.getIntrinsicWidth();
//        int height = drawable.getIntrinsicHeight();
//        Bitmap bitmap = Bitmap.createBitmap(width, height, drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
//        Canvas canvas = new Canvas(bitmap);
//        drawable.setBounds(0, 0, width, height);
//        drawable.draw(canvas);
//        return bitmap;
//
//    }


    /**
     * 获取两个List的不同元素
     *
     * @param list1
     * @param list2
     * @return
     */
    //TODO 在获取优惠券列表界面,需要优化
    public static List<Object> getListDiffrent(List<Object> list1, List<Object> list2) {
        List<Object> diff = new ArrayList<>();
        List<Object> maxList = list1;
        List<Object> minList = list2;
        if (list2.size() > list1.size()) {
            maxList = list2;
            minList = list1;
        }

        // 将List中的数据存到Map中
        Map<Object, Integer> maxMap = new HashMap<Object, Integer>(maxList.size());
        for (Object string : maxList) {
            maxMap.put(string, 1);
        }

        // max:1,2,3,4,5
        // min:2,4,6,8,10

        // 循环minList中的值，标记 maxMap中 相同的 数据2
        for (Object string : minList) {
            // 相同的
            if (maxMap.get(string) != null) {
                maxMap.put(string, 2);
                continue;
            }
            // 不相等的
            diff.add(string);
        }

        // 循环maxMap
        for (Map.Entry<Object, Integer> entry : maxMap.entrySet()) {
            if (entry.getValue() == 1) {
                diff.add(entry.getKey());
            }
        }

        return diff;
    }

    public static boolean isMobileNO(String mobiles) {
        /*
        总结起来就是第一位必定为1，第二位必定为3或5或7或8，其他位置的可以为0-9
		"[1]"代表第1位为数字1，"[3578]"代表第二位可以为3、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
		香港：5/6/9开头后面跟7位数字（手机）和 2/3开头的8位数的号码（座机） 规则：    |[23569]\d{7}
		台湾：09开头后面跟8位数字（暂未添加）
		澳门：66或68开头后面跟5位数字（暂未添加）
		*/
        String telRegex = "[1][34578]\\d{9}|\\d{4}";
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }

    public static boolean isNotNULL(String str) {
        if (null != str && str.trim().length() > 0)
            return true;
        return false;
    }

    public static boolean isOneDecimal(double decimal) {

        String balanceStr = "" + decimal;
        int dotIndex = balanceStr.indexOf(".");
        if (dotIndex != -1) {
            int decimalLength = balanceStr.length() - dotIndex - 1;
            if (decimalLength > 1) {
                return false;
            }
        }
        return true;
    }

    public static String subZeroAndDot(String s) {
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }

    public static boolean isBirethdayNO(String birthday) {
        String telRegex = "([1-2][0-9][0-9][0-9][-](([0][13578])|([1][02]))[-](([0][0-9])|([1-2][0-9])|([3][0-1])))|" +
                "([1-2][0-9][0-9][0-9][-](([0][469])|([1][1]))[-](([0][0-9])|([1-2][0-9])|([3][0])))|" +
                "([1-2][0-9][0-9][0-9][-]([0][2])[-](([0][0-9])|([1-2][0-9])))";
        if (TextUtils.isEmpty(birthday)) return false;
        else return birthday.matches(telRegex);
    }

    public static boolean isBirethday_NO(String birthday) {
        String telRegex = "([1-2][0-9][0-9][0-9](([0][13578])|([1][02]))(([0][0-9])|([1-2][0-9])|([3][0-1])))|" +
                "([1-2][0-9][0-9][0-9](([0][469])|([1][1]))(([0][0-9])|([1-2][0-9])|([3][0])))|" +
                "([1-2][0-9][0-9][0-9]([0][2])(([0][0-9])|([1-2][0-9])))";
        if (TextUtils.isEmpty(birthday)) return false;
        else return birthday.matches(telRegex);
    }

    public static boolean isDayBetween(String beginStr, String endStr) {
        Calendar now = Calendar.getInstance();
        Calendar begin = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date beginDate = sdf.parse(beginStr);
            Date endDate = sdf.parse(endStr);
            begin.setTime(beginDate);
            end.setTime(endDate);
            end.add(Calendar.DAY_OF_YEAR, 1);
            //Log.d("alert",begin.get(Calendar.YEAR)+"-"+begin.get(Calendar.MONTH)+"-"+begin.get(Calendar.DAY_OF_MONTH)+" "+begin.get(Calendar.HOUR_OF_DAY)+":"+begin.get(Calendar.MINUTE));
            //Log.d("alert",end.get(Calendar.YEAR)+"-"+end.get(Calendar.MONTH)+"-"+end.get(Calendar.DAY_OF_MONTH)+" "+end.get(Calendar.HOUR_OF_DAY)+":"+end.get(Calendar.MINUTE));
            if (now.after(begin) && now.before(end)) {
                return true;
            }
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public final static String MD5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                if (byte0 < 0) {
                    byte0 += 256;
                }
                str[k++] = hexDigits[(byte0 >> 4) & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
            /*StringBuffer str=new StringBuffer();
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                if(byte0<0){
                    byte0+=256;
                }
                str.append( hexDigits[(byte0 >> 4) & 0xf]);
                str.append( hexDigits[byte0 & 0xf]);
            }
            return str.toString();*/
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getApplicationMetaValue(Context mContext, String name) {
        ApplicationInfo appInfo = null;
        try {
            appInfo = mContext.getPackageManager().getApplicationInfo(mContext.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (appInfo != null) {
            return appInfo.metaData.getString(name);
        }
        return null;
    }

    // 获得可用的内存
    public static long getmem_UNUSED(Context mContext) {
        long MEM_UNUSED;
        // 得到ActivityManager
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        // 创建ActivityManager.MemoryInfo对象

        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);

        // 取得剩余的内存空间

        MEM_UNUSED = mi.availMem / 1024;
        return MEM_UNUSED;
    }
}
