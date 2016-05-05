package com.eve.everyone.evetool.utils;

import android.content.Context;
import android.util.Log;

import com.maxfun.R;
import com.maxfun.entity.SuperIdGroup;
import com.maxfun.entity.SuperIdGroupUserList;
import com.maxfun.entityparams.HttpParams;
import com.maxfun.entityparams.HttpRequestParams;
import com.maxfun.interfaces.InterfaceCallback;
import com.maxfun.interfaces.MyCallBack;
import com.maxfun.response.SuperIdGroupResponse;
import com.maxfun.response.SuperIdGroupResultResponse;
import com.maxfun.response.SuperIdGroupUserListResponse;
import com.maxfun.response.SuperResultListResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Kavli on 2015/12/8.
 */
public class SuperIdHttpUtil extends HttpUtils {
    public static final String URL = "https://api.superid.me/v1/";

    public static void getGroupId(final Context mContext, long groupId, final InterfaceCallback ac) {
        //post(context,HttpRequestEnum.SuperIdAddtoGroup,);
        Map<String, String> map = new HashMap<String, String>();
        final String tag;
        if (!isDemo()) {
            tag = "enterprise_" + groupId;
        } else {
            tag = "demo_ent_" + groupId;
        }
        map.put("tag", tag);
        get(mContext, HttpRequestParams.GroupListRequest(), map, SuperIdGroupResultResponse.class, new InterfaceCallback() {
            @Override
            public void execute(Object obj, String results) {
                if (obj != null) {
                    SuperIdGroupResultResponse groupResultVO = (SuperIdGroupResultResponse) obj;
                    if (groupResultVO.getStatus().equals(HttpParams.SUCCESSSTR)) {
                        List<SuperIdGroup> groupList = groupResultVO.getGroupResultVO().getGroupList();
                        if (groupList.size() == 1) {
                            //执行添加
                            ac.execute(groupList.get(0).getId(), results);
                        } else {
                            //增加groupId
                            createGroup(mContext, tag, ac);
                        }
                    } else {
                        ac.error(groupResultVO.getMessage());
                    }
                } else {
                    //Log.d("SuperID","add user to Group failed");
                    ac.error(mContext.getString(R.string.dialog_face_init));
                }
            }

            @Override
            public void error(Object obj) {
                ac.error(obj);
            }

            @Override
            public void finish(Object obj) {

            }
        }, mContext.getString(R.string.face_request));
    }

    public static void getGroupUserList(final Context mContext, long groupId) {
        getGroupId(mContext, groupId, new InterfaceCallback() {
            @Override
            public void execute(Object obj, String results) {
                String group_id = (String) obj;
                Map<String, String> map = new HashMap<String, String>();
                map.put("group_id", group_id);
                SuperIdHttpUtil.get(mContext, HttpRequestParams.DelFormGroupRequest(), map, SuperIdGroupUserListResponse.class, new InterfaceCallback() {
                    @Override
                    public void execute(Object obj, String results) {
                        if (obj != null) {
                            SuperIdGroupUserListResponse resultVO = (SuperIdGroupUserListResponse) obj;
                            if (resultVO.getGroupUserList() != null) {
                                SuperIdGroupUserList userList = resultVO.getGroupUserList();
                                LogUtils.d(LogUtils.TAG_SUPERID, "group count=" + userList.getCount() + ",list:");
                                for (int i = 0; i < userList.getUserList().size(); i++) {
                                    LogUtils.d(LogUtils.TAG_SUPERID, userList.getUserList().get(i));
                                }
                            } else {
                                LogUtils.d("get list fail");
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

            @Override
            public void error(Object obj) {

            }

            @Override
            public void finish(Object obj) {

            }
        });
    }

    public static void removeUserFromGroup(final Context mContext, long groupId, final String openId) {
        getGroupId(mContext, groupId, new InterfaceCallback() {
            @Override
            public void execute(Object obj, String results) {
                String group_id = (String) obj;
                Map<String, String> map = new HashMap<String, String>();
                map.put("group_id", group_id);
                map.put("open_id", openId);
                SuperIdHttpUtil.delete(mContext, HttpRequestParams.DelFormGroupRequest(), map, SuperResultListResponse.class, new InterfaceCallback() {
                    @Override
                    public void execute(Object obj, String results) {
                        if (obj != null) {
                            SuperResultListResponse resultVO = (SuperResultListResponse) obj;
                            if (resultVO.getListResult() != null) {
                                LogUtils.d("succeed size:" + resultVO.getListResult().getSucceedList().size());
                                LogUtils.d("failed size:" + resultVO.getListResult().getFailedList().size());
                            } else {
                                LogUtils.d("get list fail");
                            }
                        }
                    }

                    @Override
                    public void error(Object obj) {

                    }

                    @Override
                    public void finish(Object obj) {

                    }
                }, mContext.getString(R.string.app_sending));
            }

            @Override
            public void error(Object obj) {

            }

            @Override
            public void finish(Object obj) {

            }
        });
    }

    public static void addUserToGroup(final Context mContext, String groupId, String openId, final InterfaceCallback ac) {
        JSONObject json = new JSONObject();
        try {
            json.accumulate("group_id", groupId);
            json.accumulate("open_id", openId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SuperIdHttpUtil.post(mContext, HttpRequestParams.AddtoGroupRequest(), json, SuperResultListResponse.class, new InterfaceCallback() {
            @Override
            public void execute(Object obj, String results) {
                if (obj != null) {
                    SuperResultListResponse resultVO = (SuperResultListResponse) obj;
                    //Log.d("alert", resultVO.getMessage());
                    if (resultVO.getStatus().equals(HttpParams.SUCCESSSTR)) {
                        ac.execute(resultVO.getListResult().getSucceedList().size(), results);
                    } else {
                        ac.error(resultVO.getMessage());
                    }
                } else {
                    ac.error(R.string.err_data_parse_err);
                }
            }

            @Override
            public void error(Object obj) {
                ac.error(mContext.getString(R.string.err_add_group_err));
            }

            @Override
            public void finish(Object obj) {

            }
        }, mContext.getString(R.string.app_sending));
    }

    private static void createGroup(final Context mContext, String tag, final InterfaceCallback ac) {
        JSONObject json = new JSONObject();
        try {
            json.accumulate("name", tag);
            json.accumulate("tag", tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SuperIdHttpUtil.post(mContext, HttpRequestParams.NewGroupCreateRequest(), json, SuperIdGroupResponse.class, new InterfaceCallback() {
            @Override
            public void execute(Object obj, String results) {
                if (obj != null) {
                    SuperIdGroupResponse resultVO = (SuperIdGroupResponse) obj;
                    Log.d("alert", resultVO.getMessage());
                    if (resultVO.getStatus().equals(HttpParams.SUCCESSSTR)) {
                        String group_id = resultVO.getGroup().getId();
                        ac.execute(group_id, results);
                    } else {
                        ac.error(resultVO.getMessage());
                    }
                } else {
                    ac.error(mContext.getString(R.string.err_data_parse_err));
                }
            }

            @Override
            public void error(Object obj) {
                ac.error(mContext.getString(R.string.err_sys));
            }

            @Override
            public void finish(Object obj) {

            }
        }, mContext.getString(R.string.app_sending));
    }

    public static Callback.Cancelable get(final Context mContext, String url,
                                          Map<String, String> map, final Class<?> z, final InterfaceCallback ac,
                                          CharSequence... strings) // url里面带参数
    {
        String uString = updateUrl(URL + url, map);
        signatureParams(map, mContext);

        if (strings != null && strings.length > 0) {
            ExDialogUtil.show(mContext, strings[0]);
        }
        LogUtils.d("URL：" + uString);
        final RequestParams rp = new RequestParams(uString);
        rp.setMethod(HttpMethod.GET);
        addHeader(mContext, rp);
        if (null != map) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                rp.addParameter(entry.getKey(), entry.getValue());
            }
        }

        LogUtils.d("request：" + rp.toJSONString() + ",Header:" + rp.getHeaders().toString());
        return getCancelable(mContext, url, z, ac, rp);
    }

    public static void delete(final Context mContext, String url,
                              Map<String, String> map, final Class<?> z, final InterfaceCallback callback,
                              CharSequence... strings) {

        final String uString = updateUrl(URL + url, map);
        signatureParams(map, mContext);
        if (strings != null && strings.length > 0) {
            ExDialogUtil.show(mContext, strings[0]);
        }
        /*final RequestParams rp = new RequestParams(uString);
        rp.setMethod(HttpMethod.DELETE);
        addHeader(mContext, rp);
        if (null != map) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                rp.addParameter(entry.getKey(), entry.getValue());
            }
        }*/

        StringBuffer sb = new StringBuffer(uString);
        if (null != map) {
            if (map.size() > 0) {
                sb.append("?");
            }
            for (Map.Entry<String, String> entry : map.entrySet()) {
                sb.append(entry.getKey());
                sb.append("=");
                sb.append(entry.getValue());
                sb.append("&");
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        final RequestParams rp = new RequestParams(sb.toString());
        rp.setMethod(HttpMethod.DELETE);
        addHeader(mContext, rp);


        LogUtils.d("URL：" + sb.toString());
        LogUtils.d("request：" + rp.toJSONString() + ",Header:" + rp.getHeaders().toString());

        x.http().request(HttpMethod.DELETE, rp, new MyCallBack<String>() {
            public void onSuccess(String result) {
                super.onSuccess(result);
                parseResult(mContext, uString, rp.toJSONString(), HTTP_GET, result, callback, z);
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                super.onError(throwable, b);
                httpErrorMsgDispose(throwable, mContext, uString, rp, HTTP_GET, callback);
            }

            @Override
            public void onFinished() {
                super.onFinished();
                //请求结束
                callback.finish(null);
            }
        });
    }

    public static Callback.Cancelable post(final Context mContext, String url,
                                           JSONObject jsonObject, final Class<?> z,
                                           final InterfaceCallback callback, CharSequence... strings) // post数据使用，返回普通的手
    {
        if (strings != null && strings.length > 0) {
            ExDialogUtil.show(mContext, strings[0]);
        }
        final String uString = updateUrl(URL + url, jsonObject);
        signatureParams(jsonObject, mContext);

        LogUtils.d("URL：" + uString);
        final RequestParams rp = new RequestParams(uString);

        rp.setMethod(HttpMethod.POST);
        rp.setAsJsonContent(true);
        rp.setBodyContent(jsonObject.toString());
        LogUtils.d("request：" + rp.toJSONString() + ",Header:" + rp.getHeaders().toString());
        return x.http().post(rp, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                parseResult(mContext, uString, rp.toJSONString(), HTTP_POST, result, callback, z);
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                super.onError(throwable, b);
                httpErrorMsgDispose(throwable, mContext, uString, rp, HTTP_POST, callback);

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
     * 设置请求信息
     *
     * @param mContext context
     * @param rp       请求参数
     */
    private static void addHeader(Context mContext, RequestParams rp) {
        rp.setConnectTimeout(TIME_OUT);
        rp.addHeader("Content-type", HEADER);
        rp.setUseCookie(false);
    }

    private static void signatureParams(JSONObject jsonObject, Context mContext) {

        try {
            jsonObject.accumulate("app_id", SuperIdHttpUtil.getSuperIdAppKey(mContext));
            jsonObject.accumulate("timestamp", Long.toString(new Date().getTime() / 1000));
            jsonObject.accumulate("noncestr", UUID.randomUUID().toString().substring(0, 16).replaceAll("-", "a"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Iterator<String> iterator = jsonObject.keys();
        ArrayList<String> list = new ArrayList<String>();
        while (iterator.hasNext()) {
            String key = iterator.next();
            try {
                String value = jsonObject.getString(key);
                list.add(key + "=" + value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String[] result = new String[list.size()];
        result = (String[]) list.toArray(result);
        Arrays.sort(result);
        StringBuffer sortResult = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sortResult.append(result[i]);
            sortResult.append("&");
        }
        sortResult.deleteCharAt(sortResult.length() - 1);
        sortResult.append(":");
        sortResult.append(SuperIdHttpUtil.getSuperIdToken(mContext));
        //Log.d("alert",sortResult.toString());
        try {
            jsonObject.accumulate("signature", ToolUtils.MD5(sortResult.toString()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.d("alert",jsonObject.toString());
    }

    private static void signatureParams(Map<String, String> map, Context mContext) {
        map.put("app_id", SuperIdHttpUtil.getSuperIdAppKey(mContext));
        map.put("timestamp", Long.toString(new Date().getTime() / 1000));
        map.put("noncestr", UUID.randomUUID().toString().substring(0, 16).replaceAll("-", "a"));

        Iterator<String> iterator = map.keySet().iterator();
        ArrayList<String> list = new ArrayList<String>();
        while (iterator.hasNext()) {
            String key = iterator.next();
            try {
                String value = map.get(key);
                list.add(key + "=" + value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String[] result = new String[list.size()];
        result = (String[]) list.toArray(result);
        Arrays.sort(result);
        StringBuffer sortResult = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sortResult.append(result[i]);
            sortResult.append("&");
        }
        sortResult.deleteCharAt(sortResult.length() - 1);
        sortResult.append(":");
        sortResult.append(SuperIdHttpUtil.getSuperIdToken(mContext));
        map.put("signature", ToolUtils.MD5(sortResult.toString()));
    }


    private static String updateUrl(String url, Map<String, String> map) {
        int first = url.indexOf('%');
        if (first < 0) {
            return url;
        }
        int second = url.indexOf('%', first + 1);
        if (second > 0) {
            //Log.d("alert",url);
            String replaceStr = url.substring(first, second + 1);
            //Log.d("alert",replaceStr);
            String key = replaceStr.substring(1, replaceStr.length() - 1);
            String tgt = map.get(key);
            map.remove(key);
            return url.replace(replaceStr, tgt);
        }
        return null;//出错
    }

    /**
     * 替换url中的%%里面的内容
     *
     * @param url
     * @param json
     */
    private static String updateUrl(String url, JSONObject json) {
        int first = url.indexOf('%');
        if (first < 0) {
            return url;
        }
        int second = url.indexOf('%', first + 1);
        if (second > 0) {
            //Log.d("alert",url);
            String replaceStr = url.substring(first, second + 1);
            //Log.d("alert",replaceStr);
            try {
                String key = replaceStr.substring(1, replaceStr.length() - 1);
                String tgt = json.getString(key);
                json.remove(key);
                return url.replace(replaceStr, tgt);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;//出错
    }

    public static String getSuperIdAppKey(Context mContext) {
        return ToolUtils.getApplicationMetaValue(mContext, "SUPERID_APPKEY");
    }

    public static String getSuperIdSecret(Context mContext) {
        return ToolUtils.getApplicationMetaValue(mContext, "SUPERID_SECRET");
    }

    public static String getSuperIdToken(Context mContext) {
        return ToolUtils.getApplicationMetaValue(mContext, "SUPERID_TOKEN");
    }


}
