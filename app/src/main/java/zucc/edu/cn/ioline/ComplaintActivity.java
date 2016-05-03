package zucc.edu.cn.ioline;

import android.content.Intent;
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

import zucc.edu.cn.Bean.OrderBean;
import zucc.edu.cn.Bean.QuestionBean;
import zucc.edu.cn.Net.CommonRequest;
import zucc.edu.cn.fragment.Tab01Fragment_1;
import zucc.edu.cn.util.ControlNumEditText;

/**
 * Created by Administrator on 2016/4/22.
 */
public class ComplaintActivity extends BaseActivity{
    private ControlNumEditText questionEt;
    private TextView tvShow;
    private Button button;
    private LinearLayout backbut;
    private int MAX_NUM = 120;

    private OrderBean ob;
    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.addquestion_activiy);
        questionEt = (ControlNumEditText) findViewById(R.id.fedback_et);
        tvShow = (TextView) findViewById(R.id.tv_show);
        button = (Button) findViewById(R.id.fedback_button);
        backbut = (LinearLayout) findViewById(R.id.backbut);
        button.setText("举报");

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

        Bundle bundle=getIntent().getBundleExtra("bundle");
        ob = (OrderBean) bundle.get(MyReleaseMsgActivity.MY_RELEASE_MSG_ACTIVITY);
    }
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fedback_button:
                complaint();

                break;
            case R.id.backbut:
                finish();
                break;
        }

    }
    private void complaint() {
        if(TextUtils.isEmpty(questionEt.getText())){
            Toast.makeText(ComplaintActivity.this, "请输入举报内容", Toast.LENGTH_SHORT).show();
            return;
        }
        OrderBean orderBean = new OrderBean();
        orderBean.setOrder_id(ob.getOrder_id());
        orderBean.setComplaint_content(questionEt.getText().toString());
        orderBean.setComplaint_student_number(ob.getStudent_name());

        String[] params = {orderBean.getUrl_Complaint(),"complaint"};
        NetTask sendTask = new NetTask();
        sendTask.execute(params);
//        /order?order_id=1&complaint_content=没送过来&complaint_student_number=31301154
//        需要order_id,举报内容，举报人。
//        String[] params = {userBean.getUrl_QSingleStu()};
//        LoadGraberTask loadGraberTask = new LoadGraberTask();
//        loadGraberTask.execute(params);


    }

    class NetTask extends AsyncTask<String,Integer,String> {
        String result;
        @Override
        protected String doInBackground(String... params) {
            //加载自己发布的单
            CommonRequest mrequest = new CommonRequest(params[0],params) {
                @Override
                public void convert(HttpURLConnection connection, String[] heads) {
                    connection.setRequestProperty("method", heads[1]);
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
                    Toast.makeText(getApplicationContext(), "投诉成功", Toast.LENGTH_SHORT).show();

                    Intent intent=new Intent();
                    setResult(RESULT_OK, intent);
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

