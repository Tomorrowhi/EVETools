package com.eve.everyone.evetool.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.google.gson.JsonParseException;
import com.maxfun.MyApplication;
import com.maxfun.R;
import com.maxfun.entity.EnterpriseUser;
import com.maxfun.entityparams.HttpParams;
import com.maxfun.entityparams.HttpRequestParams;
import com.maxfun.interfaces.InterfaceCallback;
import com.maxfun.interfaces.MyCallBack;
import com.maxfun.response.ApiErrorResponse;
import com.maxfun.shared.LoginTokenKeeper;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Map;

/**
 * Created by Tao_office on 2016/1/18 0018.
 * 网络请求工具类，在此使用xUtils工具类作为主要的网络请求手段
 */
public class HttpUtils {


    private static final String DemoURL = "demo.";
    private static final String ReleaseURL = "api.maxfun";
    public static String HTTP_POST = "POST";
    public static String HTTP_GET = "GET";
    protected static String HEADER = "application/json;charset=utf-8";
    protected static int TIME_OUT = 15000;         //设置超时为10s
    private static String TOKEN = "bearer ";
    private static String versionCode = null;
    private static ConnectivityManager connectivityManager;

    /**
     * 判断当前是否为demo
     *
     * @return
     */
    public static boolean isDemo() {
        //判断是否是测试版本
        //if (HttpParams.URL.contains(DemoURL) || HttpParams.WXURL.contains(DemoURL)) {
        if (!HttpParams.URL.contains(ReleaseURL) || !HttpParams.WXURL.contains(ReleaseURL)) {
            //测试版应用
            return true;
        } else {
            //正式版
            return false;
        }
    }

    /**
     * post请求(异步)
     *
     * @param mContext        context
     * @param url             短url
     * @param jsonObject      body数据
     * @param isAddDeviceInfo 是否添加设备信息 true:添加
     * @param string          显示弹窗的内容
     * @param entityClass     class
     * @param callback        callback
     * @return 请求对象
     */
    public static Callback.Cancelable httpPost(final Context mContext, final String url,
                                               JSONObject jsonObject, boolean isAddDeviceInfo, String string,
                                               final Class<?> entityClass,
                                               final InterfaceCallback callback) {
        if (string != null && string.length() > 0) {
            ExDialogUtil.show(mContext, string);
        }
        LogUtils.d("URL：" + HttpParams.URL + url + "");
        final RequestParams rp = new RequestParams(HttpParams.URL + url);
        addHeader(mContext, rp);
        rp.setMethod(HttpMethod.POST);
        //判断是否需要添加设备信息
        if (isAddDeviceInfo) {
            addDeviceInfo(mContext, rp);
        }
        rp.setAsJsonContent(true);
        rp.setBodyContent(jsonObject.toString());
        LogUtils.d("request：" + rp.toJSONString() + ",Header:" + rp.getHeaders().toString());
        return x.http().post(rp, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                parseResult(mContext, url, rp.toJSONString(), HTTP_POST, result, callback, entityClass);
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                super.onError(throwable, b);
                httpErrorMsgDispose(throwable, mContext, url, rp, HTTP_POST, callback);

            }

            @Override
            public void onFinished() {
                super.onFinished();
                //请求结束
                callback.finish(null);
            }
        });
    }

    /**
     * 请求error后的数据处理
     *
     * @param throwable throwable
     * @param mContext  context
     * @param url       url
     * @param rp        请求参数
     * @param method    请求方法类型
     * @param callback  回调
     */
    protected static void httpErrorMsgDispose(Throwable throwable, Context mContext, String url, RequestParams rp, String method, InterfaceCallback callback) {
        HttpException httpException;
        try {
            httpException = (HttpException) throwable;
            int code = httpException.getCode();
            if (code != HttpParams.SUCCESSINT) {
                //非200的状态码,需要将信息记录后发送
                sendExceptionMsg(mContext, httpException.getResult(), url, rp.toString(), method, MyApplication.ERRTYPE_HTTP);
                callback.error(httpException.getResult());
            }
        } catch (ClassCastException e) {
            ToastUtil.showMessage(MyApplication.getInstance().getContext(),
                    MyApplication.getInstance().getContext().getString(R.string.err_no_net));
        }
    }


    /**
     * get请求(异步)
     *
     * @param mContext        context
     * @param url             短url
     * @param map             map集合数据
     * @param isAddDeviceInfo 是否添加设备信息 true:添加
     * @param string          显示加载框内容
     * @param entityClass     class
     * @param callback        callback
     * @return 请求对象
     */
    public static Callback.Cancelable httpGet(final Context mContext, final String url,
                                              Map<String, Object> map, boolean isAddDeviceInfo,
                                              String string, final Class<?> entityClass,
                                              final InterfaceCallback callback) {
        if (string != null && string.length() > 0) {
            ExDialogUtil.show(mContext, string);
        }
        LogUtils.d("URL：" + HttpParams.URL + url + "");
        final RequestParams rp = new RequestParams(HttpParams.URL + url);
        addHeader(mContext, rp);
        rp.setMethod(HttpMethod.GET);
        if (null != map) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                rp.addParameter(entry.getKey(), entry.getValue());
            }
        }
        //判断是否需要添加设备信息
        if (isAddDeviceInfo) {
            addDeviceInfo(mContext, rp);
        }
        LogUtils.d("request：" + rp.toJSONString() + ",Header:" + rp.getHeaders().toString());
        return getCancelable(mContext, url, entityClass, callback, rp);

    }

    /**
     * get请求(异步)
     *
     * @param mContext        context
     * @param url             短url
     * @param map             map集合数据
     * @param isAddDeviceInfo 是否添加设备信息 true:添加
     * @param string          显示加载框内容
     * @param entityClass     class
     * @param callback        callback
     * @return 请求对象
     */
    public static Callback.Cancelable httpWxGet(final Context mContext, final String url,
                                                Map<String, Object> map, boolean isAddDeviceInfo,
                                                String string, final Class<?> entityClass,
                                                final InterfaceCallback callback) {
        if (string != null && string.length() > 0) {
            ExDialogUtil.show(mContext, string);
        }
        LogUtils.d("URL：" + HttpParams.WXURL + url + "");
        final RequestParams rp = new RequestParams(HttpParams.WXURL + url);
        addHeader(mContext, rp);
        rp.setMethod(HttpMethod.GET);
        if (null != map) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                rp.addParameter(entry.getKey(), entry.getValue());
            }
        }
        //判断是否需要添加设备信息
        if (isAddDeviceInfo) {
            addDeviceInfo(mContext, rp);
        }
        LogUtils.d("request：" + rp.toJSONString() + ",Header:" + rp.getHeaders().toString());
        return getCancelable(mContext, url, entityClass, callback, rp);

    }

    /**
     * get请求
     *
     * @param mContext    context
     * @param url         url
     * @param entityClass 实体类
     * @param callback    回调
     * @param rp          参数
     * @return 请求对象
     */
    protected static Callback.Cancelable getCancelable(final Context mContext, final String url, final Class<?> entityClass, final InterfaceCallback callback, final RequestParams rp) {
        return x.http().get(rp, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                parseResult(mContext, url, rp.toJSONString(), HTTP_GET, result, callback, entityClass);
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                super.onError(throwable, b);
                httpErrorMsgDispose(throwable, mContext, url, rp, HTTP_GET, callback);
            }

            @Override
            public void onFinished() {
                super.onFinished();
                //请求结束
                callback.finish(null);
            }
        });
    }


    /**
     * 解析从服务器返回的数据
     *
     * @param context     context
     * @param url         url
     * @param body        请求内容
     * @param method      请求方法类型
     * @param result      请求结果
     * @param callback    回调
     * @param entityClass class
     */
    protected static void parseResult(Context context, String url, String body, String method, String result, InterfaceCallback callback, Class<?> entityClass) {
        if (result != null) {
            LogUtils.d("result：" + result);
            Object data = null;
            try {
                data = JsonUtils.gsonParseData(result, entityClass);
            } catch (JsonParseException e) {
                sendExceptionMsg(context, result, url, body, method, MyApplication.ERRTYPE_HTTP);
            }
            if (data == null) {
                ToastUtil.showMessage(context, context.getString(R.string.err_services));
            }
            callback.execute(data, result);
        } else {
            callback.execute(null, null);
            ToastUtil.showMessage(context, context.getString(R.string.err_services));
        }
    }

    /**
     * 设置请求信息
     *
     * @param mContext context
     * @param rp       请求参数
     */
    private static void addHeader(Context mContext, RequestParams rp) {
        EnterpriseUser enterpriseUser = LoginTokenKeeper
                .getEnterpriseUser(mContext);
        if (versionCode == null) {
            getVersionCode(mContext);
        }
        rp.setConnectTimeout(TIME_OUT);
        rp.addHeader("Authorization", TOKEN + enterpriseUser.getAuthorizationCode());
        rp.addHeader("enterprise_id", enterpriseUser.getEnterpriseId().toString());
        rp.addHeader("app_version_code", versionCode);
        rp.addHeader("device_type", MyApplication.deviceType);
        rp.addHeader("Content-type", HEADER);
        rp.setUseCookie(false);
    }

    /**
     * 请求头添加设备信息
     */
    public static void addDeviceInfo(Context context, RequestParams rp) {
        rp.addHeader("device_info", getDeviceInfo(context));
    }

    /**
     * 获取设备信息
     *
     * @param context context
     * @return 设备信息
     */
    public static String getDeviceInfo(Context context) {
        if (versionCode == null) {
            getVersionCode(context);
        }
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return "DeviceId(IMEI) = " + tm.getDeviceId() +
                ",DeviceId(ANDROID_ID) = " + Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID) +
                ",DeviceName =" + Build.MODEL +
                ",DeviceOsVersion = Android V" + Build.VERSION.RELEASE +
                ",DeviceSoftwareVersion = " + tm.getDeviceSoftwareVersion() +
                ",AppVersion = " + versionCode +
                ",NetworkCountryIso = " + tm.getNetworkCountryIso() +
                ",NetworkOperator = " + tm.getNetworkOperator() +
                ",NetworkOperatorName = " + tm.getNetworkOperatorName() +
                ",NetworkType = " + tm.getNetworkType() +
                ",PhoneType = " + tm.getPhoneType();
    }

    /**
     * 获取versioncode
     *
     * @param context context
     */
    public static String getVersionCode(Context context) {
        if (versionCode == null) {
            try {
                PackageManager pm = context.getPackageManager();
                PackageInfo pinfo = pm.getPackageInfo(context.getPackageName(),
                        PackageManager.GET_CONFIGURATIONS);

                versionCode = pinfo.versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return versionCode;
    }


    /**
     * 获得当前使用的IP地址（区分wifi或者移动网络）
     *
     * @param mContext context
     * @return ip
     */
    public static String getIP(Context mContext) {
        String defaultIP = "0.0.0.0";
        //判断网络环境
        if (!checkNetWorkState(mContext)) {
            //不存在网络环境
            return defaultIP;
        }
        if (connectivityManager == null)
            connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        //获取网络类型
        switch (connectivityManager.getActiveNetworkInfo().getType()) {
            case ConnectivityManager.TYPE_MOBILE:
                //移动网络
                try {
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumeration = intf.getInetAddresses(); enumeration.hasMoreElements(); ) {
                            InetAddress inetAddress = enumeration.nextElement();
                            if (!inetAddress.isLoopbackAddress()) {
//                               InetAddressUtils.isIPv4Address(defaultIP = inetAddress.getHostAddress());
                                defaultIP = inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }
                break;
            case ConnectivityManager.TYPE_WIFI:
                //wifi
                WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                int ip = wifiInfo.getIpAddress();
                defaultIP = (ip & 0xFF) + "." + ((ip >> 8) & 0xFF) + "." + ((ip >> 16) & 0xFF)
                        + "." + (ip >> 24 & 0xFF);
                break;
        }
        return defaultIP;
    }


    /**
     * 判断网络环境
     *
     * @param mContext context
     * @return true(网络有效) or false(网络无效)
     */
    public static boolean checkNetWorkState(Context mContext) {
        boolean flag = false;
        if (mContext == null)
            return false;
        if (connectivityManager == null)
            connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != connectivityManager && connectivityManager.getActiveNetworkInfo() != null)
            flag = connectivityManager.getActiveNetworkInfo().isAvailable();

        return flag;
    }

    /**
     * 异常信息发送
     *
     * @param context    context
     * @param result     请求结果
     * @param URL        请求的URL
     * @param body       请求参数
     * @param httpMethod 请求使用的方法(POST/GET)
     * @param type       错误类型
     */
    public static void sendExceptionMsg(Context context, String result, String URL, String body,
                                        String httpMethod, byte type) {
        EnterpriseUser enterpriseUser = LoginTokenKeeper.getEnterpriseUser(context);
        JSONObject JsonObject = new JSONObject();
        try {
            JsonObject.put("enterprise_id", enterpriseUser.getEnterpriseId());
            JsonObject.put("device_info", getDeviceInfoBrief(context));
            JsonObject.put("device_type", MyApplication.deviceType);
            JsonObject.put("request_url", HttpParams.URL + URL);
            JsonObject.put("request_body", body);
            JsonObject.put("response", result);
            JsonObject.put("http_method", httpMethod);
            JsonObject.put("err_type", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ApiError_Msg:" + JsonObject.toString());

        httpPost(context,
                HttpRequestParams.apiErrorRequest(),
                JsonObject, false, "", ApiErrorResponse.class, new InterfaceCallback() {
                    @Override
                    public void execute(Object obj, String results) {
                        ApiErrorResponse result = (ApiErrorResponse) obj;
                        if (result != null) {
                            if ("success".equals(result.getResult())) {
                                LogUtils.d("ApiError_Success");
                            } else {
                                LogUtils.d("ApiError_failure");
                            }
                        }

                    }

                    @Override
                    public void error(Object obj) {

                    }

                    @Override
                    public void finish(Object obj) {

                    }
                });

    }

    /**
     * 获取简要的设备信息
     *
     * @param context context
     * @return sb
     */
    private static String getDeviceInfoBrief(Context context) {
        StringBuilder sb = new StringBuilder();
        if (versionCode == null) {
            getVersionCode(context);
        }
        sb.append(",DeviceName =").append(Build.MODEL);
        sb.append(",DeviceOsVersion = Android V").append(Build.VERSION.RELEASE);
        sb.append(",AppVersion = ").append(versionCode);
        return sb.toString();
    }


}
