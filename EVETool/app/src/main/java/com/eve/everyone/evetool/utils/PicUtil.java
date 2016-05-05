package com.eve.everyone.evetool.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class PicUtil {


    /**
     * 根据一个网络连接(String)获取bitmap图像
     *
     * @param imageUri
     * @return
     * @throws MalformedURLException
     */
    public static Bitmap getbitmap(String imageUri) {
        // 显示网络上的图片
        Bitmap bitmap = null;
        try {
            //Log.d("alert",encodeUrlFileName(imageUri));
            URL myFileUrl = new URL(encodeUrlFileName(imageUri));
            HttpURLConnection conn = (HttpURLConnection) myFileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
            conn.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    public static String encodeUrlFileName(String url) {
        int index = url.lastIndexOf("/");
        int pointIndex = url.lastIndexOf(".");
        if (index >= 0 && index < url.length() - 1 && pointIndex > index) {
            String fileName = encodeChineseChar(url.substring(index + 1));
            /*try {
                fileName = URLEncoder.encode(url.substring(index+1), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }*/
            //Log.d("alert",url.substring(0,index+1)+fileName);
            return url.substring(0, index + 1) + fileName;
        } else {
            return url;
        }
    }

    /**
     * 把字符串中的中文部分进行url编码，中文的utf-8编码结果为每个汉字3个字节编码，以0x1110xxxx 0x10xxxxxx 0x10xxxxxx进行编码
     *
     * @param src
     * @return
     */
    public static String encodeChineseChar(String src) {
        String tgt = null;
        try {
            byte[] srcByte = src.getBytes("utf-8");
            byte[] tgtByte = new byte[srcByte.length * 3];
            int tgtLength = 0;

            for (int i = 0; i < srcByte.length; i++) {
                if ((i < srcByte.length - 2) &&
                        ((srcByte[i] & 0xf0) == 0xe0) &&
                        ((srcByte[i + 1] & 0xc0) == 0x80) &&
                        ((srcByte[i + 2] & 0xc0) == 0x80)) {
                    for (int sub = 0; sub < 3; sub++) { //三个字节为一个中文的编码
                        String hex = Integer.toHexString(srcByte[i + sub] & 0xFF);
                        if (hex.length() == 1) {
                            hex = "%0" + hex;
                        } else {
                            hex = "%" + hex;
                        }
                        byte[] subBytes = hex.getBytes("utf-8");
                        for (int j = 0; j < subBytes.length; j++) {
                            tgtByte[tgtLength] = subBytes[j];
                            tgtLength++;
                        }
                    }
                    i += 2;
                } else {
                    tgtByte[tgtLength] = srcByte[i];
                    tgtLength++;
                }
            }
            tgt = new String(tgtByte, 0, tgtLength);
            srcByte = null;
            tgtByte = null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return tgt;
    }
}