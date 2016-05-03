package zucc.edu.cn.ioline;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;

import zucc.edu.cn.Bean.QuestionBean;
import zucc.edu.cn.Net.CommonRequest;
import zucc.edu.cn.util.ControlNumEditText;

/**
 * Created by Administrator on 2016/4/22.
 */
public class AddQuestion extends BaseActivity{
    private ControlNumEditText questionEt;
    private TextView tvShow;
    private Button button;
    private LinearLayout backbut;

    private int MAX_NUM = 120;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.addquestion_activiy);
        questionEt = (ControlNumEditText) findViewById(R.id.fedback_et);
        tvShow = (TextView) findViewById(R.id.tv_show);
        button = (Button) findViewById(R.id.fedback_button);
        backbut = (LinearLayout) findViewById(R.id.backbut);
    }

    @Override
    protected void setListener() {

        button.setOnClickListener(this);
        backbut.setOnClickListener(this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        //下面这两个方法你只需要设置一个就行，因为这两个方法都会知道一个最大的输入值。
        //You just need to write one of these two method below.Because you need to know the max number.
        questionEt.setMaxNum(MAX_NUM);
        MAX_NUM = questionEt.getMaxNum();
        questionEt.setOnTextEditListener(new ControlNumEditText.onTextEditListener() {
            @Override
            public void textChanged(int cur_num) {
                tvShow.setText(String.valueOf((MAX_NUM - cur_num)));
            }
        });

    }
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fedback_button:
                putQuestion();
                break;
            case R.id.backbut:
                finish();
                break;
        }

    }

    /**
     * 提交问题
     */
    private void putQuestion() {
        if(TextUtils.isEmpty(questionEt.getText())){
            Toast.makeText(AddQuestion.this, "请输入问题", Toast.LENGTH_SHORT).show();
            return;
        }
        String user_id=null;
        SharedPreferences sharedPreferences =
                getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", "null");

        QuestionBean qb = new QuestionBean();
        qb.setStudent_number(user_id);
        qb.setQuestion(questionEt.getText().toString());

        String[] params = {qb.getUrl_getPutQuestion(),"putQuestion"};
        NetTask sendTask = new NetTask();
        sendTask.execute(params);

    }
    class NetTask extends AsyncTask<String,Integer,String> {
        String result;
        @Override
        protected String doInBackground(String... params) {
            //加载自己发布的单
            CommonRequest mrequest = new CommonRequest(params[0],params) {
                @Override
                public void convert(HttpURLConnection connection, String[] heads) {
                    connection.setRequestProperty("method",heads[1]);
                }
            };
            result = mrequest.getResult();
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            Log.i("result********", result);
            try {
                if(new String("success".getBytes("iso-8859-1"),"UTF-8").equals(new String(result.trim().getBytes("iso-8859-1"),"UTF-8"))){
                    Toast.makeText(getApplicationContext(), "插入成功", Toast.LENGTH_SHORT).show();
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

