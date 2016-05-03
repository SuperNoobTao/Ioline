package zucc.edu.cn.ioline;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;

import zucc.edu.cn.Bean.FedbackBean;
import zucc.edu.cn.Net.CommonRequest;
import zucc.edu.cn.util.ControlNumEditText;

/**
 * Created by Administrator on 2016/4/20.
 */
public class fedbackActivity extends BaseActivity{
    private LinearLayout quesiontLl;
    private LinearLayout fedbackBackbut;
    private ControlNumEditText fedbackEt;
    private TextView tvShow;
    private Button fedbackButton;

    private int MAX_NUM = 120;
    private String user_id = null;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.fedback_activiy);
        quesiontLl = (LinearLayout) findViewById(R.id.quesiont_ll);
        fedbackBackbut = (LinearLayout) findViewById(R.id.fedback_backbut);
        fedbackEt = (ControlNumEditText) findViewById(R.id.fedback_et);
        tvShow = (TextView) findViewById(R.id.tv_show);
        fedbackButton = (Button) findViewById(R.id.fedback_button);
    }

    @Override
    protected void setListener() {
        fedbackButton.setOnClickListener(this);
        fedbackBackbut.setOnClickListener(this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        //要先判断当前的登录状态
        SharedPreferences sharedPreferences =
                getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", "null");
    }
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fedback_button:
                fedback();
                break;
            case R.id.fedback_backbut:
                finish();
                break;
        }
    }

    /**
     *  投诉
     */
    private void fedback() {
        if(TextUtils.isEmpty(fedbackEt.getText())){
            Toast.makeText(getApplication(), "请输入内容", Toast.LENGTH_SHORT).show();
            return;
        }

        FedbackBean fbb = new FedbackBean(user_id, fedbackEt.getText().toString());
        String[] params = {fbb.getUrl_fedback()};
        FedBackTask sendTask = new FedBackTask();
        sendTask.execute(params);
    }

    public class FedBackTask extends AsyncTask<String,Integer,String> {
        String result;
        @Override
        protected String doInBackground(String... params) {
            CommonRequest mrequest = new CommonRequest(params[0],params) {
                @Override
                public void convert(HttpURLConnection connection, String[] heads) {
                }
            };
            result = mrequest.getResult();
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if(new String("success".getBytes("iso-8859-1"),"UTF-8").equals(new String(result.trim().getBytes("iso-8859-1"),"UTF-8"))){
                    Toast.makeText(getApplicationContext(), "谢谢您的意见", Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "网络错误，请检查你的网络", Toast.LENGTH_SHORT).show();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }
}
