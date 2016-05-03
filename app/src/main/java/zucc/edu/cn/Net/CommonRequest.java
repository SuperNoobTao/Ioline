package zucc.edu.cn.Net;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by Chenqi on 2016/3/14.
 */
public abstract class CommonRequest {
    private String httpUrl = URLPath.headUrl;
    private String result;
    private String[]heads;
    public CommonRequest(String url, String[] heads){
        this.heads = heads;
        this.httpUrl += url;
        Log.i("watch url",httpUrl);
        request();
    }
    public void request() {
        BufferedReader reader = null;
        result = null;
        StringBuffer sbf = new StringBuffer();
        try {
//            httpUrl = URLEncoder.encode(URLEncoder.encode(httpUrl,"UTF-8"), "UTF-8");
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("apikey", "www.falling.ga");
             convert(connection,heads);

            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));

            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            result = sbf.toString();
            Log.i("watch result",result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String getResult() {
        return result;
    }
    public abstract void convert(HttpURLConnection connection,String[] heads);
}
