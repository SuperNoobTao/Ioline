package zucc.edu.cn.ioline;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import zucc.edu.cn.Net.MyRequest;

/**
 * Created by Chenqi on 2016/3/14.
 */
public class RegisterActivity extends Activity implements  View.OnClickListener{

    private LinearLayout l_back_to_login ;

    private EditText register_user;
    private EditText register_password;

    private Button registerbut;
    private ImageButton register_user_clear;
    private ImageButton register_password_clear;

    private String reg_user;
    private MyTask mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        initEvent();
    }
    /**
     * fun：1、加载控件
     *      2、加载数据
     * */
    private void init() {
        l_back_to_login   = (LinearLayout) findViewById(R.id.l_back_to_login);
        register_user     = (EditText) findViewById(R.id.register_user);
        register_password = (EditText) findViewById(R.id.register_password);

        registerbut       = (Button) findViewById(R.id.registerbut);
        register_user_clear = (ImageButton) findViewById(R.id.register_user_clear);
        register_password_clear = (ImageButton) findViewById(R.id.register_password_clear);

    }

    /**
     * func:给控件加上数据
     * */
    private void initEvent() {
        l_back_to_login.setOnClickListener(this);//back to login page

        registerbut.setOnClickListener(this);
        register_user_clear.setOnClickListener(this);
        register_password_clear.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.registerbut:
                register();
                break;
            case R.id.register_user_clear:
                register_user.setText("");
                break;
            case R.id.register_password_clear:
                register_password.setText("");
                break;
            case R.id.l_back_to_login:
                backTologin();
                break;
        }

    }

    private void backTologin() {

    }

    private void register() {
        if(TextUtils.isEmpty(register_user.getText())){
            Toast.makeText(RegisterActivity.this, "注册账号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(register_password.getText())){
            Toast.makeText(RegisterActivity.this, "注册账号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        reg_user = register_user.getText().toString();
        String reg_password = register_password.getText().toString();

        String url = "user?student_number="+reg_user; //先查找有没有这个账号，没有的话到下一个页面填写具体的数据
        mTask = new MyTask();
        mTask.execute(url);

    }

    private class MyTask extends AsyncTask<String, Integer, String> {
        private boolean pReg = false;
        @Override
        protected String doInBackground(String... params) {
//            Toast.makeText(RegisterActivity.this, "已经存在该账号", Toast.LENGTH_SHORT).show();
            try {

                if(new MyRequest(params[0]).getResult().equals(""))
                    pReg = true;
                else
                    pReg = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(pReg){
                Toast.makeText(RegisterActivity.this, "请继续填写详细信息", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent();
                intent.putExtra("userid", reg_user);
                intent.setClass(getApplicationContext(),regDetailedActivity.class);
            }
            else
            Toast.makeText(RegisterActivity.this, "已经存在该账号，注册失败", Toast.LENGTH_SHORT).show();

        }
    }

}
