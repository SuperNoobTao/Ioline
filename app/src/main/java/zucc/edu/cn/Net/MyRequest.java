package zucc.edu.cn.Net;

import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Chenqi on 2016/3/14.
 */
public class MyRequest {
    private String httpUrl;
    private String result;
    //调用构造器传入 url  从result中得到值从 result 中获得 要自己解析
    public MyRequest(String url){
        httpUrl = "http://10.66.4.6:8080/ionline/";
        this.httpUrl += url;
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                request();
//            }
//        }).start();

        request();
    }
    public String getResult() {
        return result;
    }
    public void request() {
        BufferedReader reader = null;
        result = null;
        StringBuffer sbf = new StringBuffer();
        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("GET");
            // 填入apikey到HTTP header
            connection.setRequestProperty("apikey", "www.falling.ga");
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            this.result = sbf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
