package zucc.edu.cn.ioline;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import zucc.edu.cn.Bean.AnswerBean;
import zucc.edu.cn.Bean.QuestionBean;
import zucc.edu.cn.Bean.UserBean;
import zucc.edu.cn.Net.CommonRequest;
import zucc.edu.cn.fragment.Tab02Fragment_qlv;
import zucc.edu.cn.util.CommonAdapter;
import zucc.edu.cn.util.CurrentTime;
import zucc.edu.cn.util.ViewHolder;

/**
 * Created by Administrator on 2016/4/22.
 */
public class QuestionItemActivity extends BaseActivity {

    public static final String PUT_ANSWER = "putAnswer";
    public static final String GET_ANSWERS = "getAnswers";
    private LinearLayout backbut;
    private ImageView danLvTouxiangPic;
    private TextView danLvName;
    private TextView danLvTime;
    private ImageView questionLvGenderPic;
    private TextView danLvSchool;
    private TextView danLvContent;
    private EditText answerEdit;
    private Button answerBtn;

    private QuestionBean qb;
    private AnswerBean   ab;
    private UserBean     ub;
    private ListView answerLv;
    private CommonAdapter mAdapter;
    private List<AnswerBean> mDatas;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

//    private

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.lv_question_item_activity);
        backbut = (LinearLayout) findViewById(R.id.backbut);
        danLvTouxiangPic = (ImageView) findViewById(R.id.dan_lv_touxiang_pic);
        danLvName = (TextView) findViewById(R.id.dan_lv_name);
        danLvTime = (TextView) findViewById(R.id.dan_lv_time);
        questionLvGenderPic = (ImageView) findViewById(R.id.question_lv_gender_pic);
        danLvSchool = (TextView) findViewById(R.id.dan_lv_school);
        danLvContent = (TextView) findViewById(R.id.dan_lv_content);
        answerLv = (ListView) findViewById(R.id.answer_lv);
        answerEdit = (EditText) findViewById(R.id.answer_edit);
        answerBtn = (Button) findViewById(R.id.answer_btn);
    }

    @Override
    protected void setListener() {
        backbut.setOnClickListener(this);
        answerBtn.setOnClickListener(this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        if (mDatas == null) {
            mDatas = new ArrayList<AnswerBean>();
        }

        mAdapter = new CommonAdapter<AnswerBean>(getApplicationContext(), mDatas, R.layout.answer_listview_item) {
            @Override
            public void convert(ViewHolder holder, AnswerBean answerBean) {
                holder.setText(R.id.answer_name, answerBean.getStudent_name())
                        .setText(R.id.answer_time, CurrentTime.subDay(answerBean.getTime()))
                        .setText(R.id.answer_content, answerBean.getContent())
                        .setText(R.id.answer_school, answerBean.getSchool());
                //性别
                if(answerBean.getSex().equals("男")){
                    holder.setImageResource(R.id.answer_gender_pic, R.drawable.gender_symbol_boy);
                }
                else{
                    holder.setImageResource(R.id.answer_gender_pic, R.drawable.gender_symbol_girl);
                }

            }

        };
        //从传过来的item 得到当前 该question 的基本信息
        answerLv.setAdapter(mAdapter);
        Bundle bundle = getIntent().getBundleExtra("bundle");
        qb = (QuestionBean) bundle.get(Tab02Fragment_qlv.QUESTION_ITEM);

        danLvName.setText(qb.getStudent_name());
        danLvTime.setText(CurrentTime.subDay(qb.getTime()));
        danLvSchool.setText(qb.getSchool());
        danLvContent.setText(qb.getQuestion());

        if (qb.getSex().equals("男")) {
            questionLvGenderPic.setImageResource(R.drawable.gender_symbol_boy);
        } else {
            questionLvGenderPic.setImageResource(R.drawable.gender_symbol_girl);
        }
        //刚进来的时候加载数据
        loadAnwser();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backbut:
                finish();
                break;
            case R.id.answer_btn:
                answer();
                break;
        }

    }


    /**
     *  加载 answer
     */
    private void loadAnwser() {
        AnswerBean ab = new AnswerBean();
        ab.setQuestion_id(qb.getId());
        String[] params = {ab.getUrl_getAnswers(), GET_ANSWERS};
        AnswerTask la = new AnswerTask();
        la.execute(params);
    }
    /**
     * 回复question
     */
    private void answer() {
        String user_id ;
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", "null");
        if(!user_id.equals("null")&&!user_id.equals(null)){
            //有账号登陆
            if(TextUtils.isEmpty(answerEdit.getText())){
                Toast.makeText(QuestionItemActivity.this, "请输入内容", Toast.LENGTH_SHORT).show();
            }
            else{
                if(ab ==null){
                    ab = new AnswerBean();
                }
                ab.setStudent_number(user_id);
                ab.setQuestion_id(qb.getId());
                ab.setContent(answerEdit.getText().toString());

                String[] params = {ab.getUrl_putAnswer(), PUT_ANSWER};
                AnswerTask la = new AnswerTask();
                la.execute(params);
            }
        }
        else
            Toast.makeText(QuestionItemActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
    }

    /**
     * 网路操做
     */
    class AnswerTask extends AsyncTask<String, Integer, String> {
        String result;
        @Override
        protected String doInBackground(String... params) {
            CommonRequest myrequest = new CommonRequest(params[0], params) {
                @Override
                public void convert(HttpURLConnection connection, String[] heads) {
                    connection.setRequestProperty("method", heads[1]);
                }
            };
            switch (params[1]){
                case PUT_ANSWER:
                    loadAnwser();
                    break;
                case GET_ANSWERS:
                    result = myrequest.getResult();
                    Log.i("QuestionItemActivtiy", result);
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<AnswerBean>>() {
                    }.getType();
                    mDatas.clear();
                    mDatas.addAll((Collection<? extends AnswerBean>) gson.fromJson(result, type));
                    break;
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            answerEdit.setText("");
            mAdapter.notifyDataSetChanged();
        }

    }
}
