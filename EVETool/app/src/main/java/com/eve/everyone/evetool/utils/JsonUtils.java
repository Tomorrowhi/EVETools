package com.eve.everyone.evetool.utils;

import com.google.gson.Gson;

/**
 * Created by Tao_office on 2016/1/19 0019.
 * Json解析工具类
 */
public class JsonUtils {

    /**
     * * 解析json数据
     *
     * @param JsonString  json数据
     * @param formatClass 需要格式化的对象class
     * @return 返回转换后的数据对象
     */
    public static Object gsonParseData(String JsonString, Class<?> formatClass) {
        Gson gson = new Gson();
        return gson.fromJson(JsonString, formatClass);
    }
}
