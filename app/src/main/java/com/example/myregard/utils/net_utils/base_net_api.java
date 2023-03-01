package com.example.myregard.utils.net_utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class base_net_api {

    public static final String base = "http://192.168.43.39:8080";

    public byte[] get_bytes() {
        byte[] bt = null;
        try {
            URL url = new URL("https://www.baidu.com");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            // 设置请求类型为Get类型
            conn.setRequestMethod("GET");
            // 判断请求Url是否成功
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("请求url失败");
            }
            InputStream inStream = conn.getInputStream();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                bt = inStream.readAllBytes();
            }
            inStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bt;
    }

    public static String get_from_tar(String path) throws Exception {
        byte[] buffer = new byte[2048];
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == 200) {
            InputStream in = conn.getInputStream();
            int len = in.read(buffer);
            byte[] data = new byte[len];
            System.arraycopy(buffer, 0, data, 0, data.length);
            String html = new String(data, "UTF-8");
            return html;
        }
        return null;
    }
}
