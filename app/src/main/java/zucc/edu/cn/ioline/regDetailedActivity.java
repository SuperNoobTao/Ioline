package zucc.edu.cn.ioline;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import zucc.edu.cn.Bean.UserBean;

/**
 * Created by Administrator on 2016/3/15.
 */
public class regDetailedActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText etPassword;
    private EditText etRePassword;
    private EditText etStudent_name;
    private EditText etSex;
    private EditText etPhone;
    private EditText edSchool;

    private Button btnReg;

    private  MyTask mTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        etPassword = (EditText) findViewById(R.id.a_r_d_pwd);
        etRePassword = (EditText) findViewById(R.id.a_r_d_rpwd);
        etStudent_name= (EditText) findViewById(R.id.a_r_d_name);
        etSex = (EditText) findViewById(R.id.a_r_d_sex);
        etPhone = (EditText) findViewById(R.id.a_r_d_phonenum);
        edSchool = (EditText) findViewById(R.id.a_r_d_school);
        btnReg = (Button) findViewById(R.id.a_r_d_regbut);

        btnReg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.a_r_d_regbut:
                register();
                break;
        }
    }

    private void register() {
        UserBean ub = new UserBean();
        if(TextUtils.isEmpty(etPassword.getText())){
            Toast.makeText(regDetailedActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(etRePassword.getText())){
            Toast.makeText(regDetailedActivity.this, "重输密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!etRePassword.getText().equals(etPassword.getText())){
            Toast.makeText(regDetailedActivity.this, "两次密码要相等", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(etStudent_name.getText())){
            Toast.makeText(regDetailedActivity.this, "名字不能为空", Toast.LENGTH_SHORT).show();
            return;
        }else ub.setStudent_name(etStudent_name.getText().toString());
        if(TextUtils.isEmpty(etSex.getText())){
            Toast.makeText(regDetailedActivity.this, "性别不能为空", Toast.LENGTH_SHORT).show();
            return;
        }else ub.setSex(etSex.getText().toString());
        if(TextUtils.isEmpty(etPhone.getText())){
            Toast.makeText(regDetailedActivity.this, "电话不能为空", Toast.LENGTH_SHORT).show();
            return;
        }else ub.setCell_phone(etPhone.getText().toString());
        if(TextUtils.isEmpty(edSchool.getText())){
            Toast.makeText(regDetailedActivity.this, "学校不能为空", Toast.LENGTH_SHORT).show();
            return;
        }else ub.setSchool(edSchool.getText().toString());

        String[] params ={etPassword.getText().toString(),ub.getUrl_Reg()};
        mTask = new MyTask();
        mTask.execute(params);

    }

    private class MyTask extends AsyncTask<String, Integer, String>{
        String result;
        @Override
        protected String doInBackground(String... params) {
            BufferedReader reader = null;
            result = null;
            StringBuffer sbf = new StringBuffer();
            String httpUrl = "http://10.66.4.6:8080/ionline/register?";
            httpUrl += params[1];

            try {
                URL url = new URL(httpUrl);
                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                connection.setRequestMethod("GET");
                // 填入apikey到HTTP header
                connection.setRequestProperty("apikey", "www.falling.ga");
                connection.setRequestProperty("password",params[0]);//
                connection.connect();
                InputStream is = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String strRead = null;
                while ((strRead = reader.readLine()) != null) {
                    sbf.append(strRead);
                    sbf.append("\r\n");
                }
                reader.close();
                result = sbf.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //
            if(result.equals("success")){
                Toast.makeText(getApplicationContext(),"注册成功",Toast.LENGTH_SHORT);
                //记录登录信息 转到Main页面
            }
            else
            {
                Toast.makeText(getApplicationContext(),"注册失败",Toast.LENGTH_SHORT);
            }
        }
    }

}
