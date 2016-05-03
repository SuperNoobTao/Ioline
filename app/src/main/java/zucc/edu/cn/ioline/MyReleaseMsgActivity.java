package zucc.edu.cn.ioline;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;

import info.hoang8f.android.segmented.SegmentedGroup;
import zucc.edu.cn.Bean.OrderBean;
import zucc.edu.cn.Bean.UserBean;
import zucc.edu.cn.Net.CommonRequest;
import zucc.edu.cn.adapter.OrderMsgAdapterViewAdapter;
import zucc.edu.cn.fragment.Tab01Fragment_2;
import zucc.edu.cn.util.CurrentTime;

/**
 * Created by Administrator on 2016/3/19.
 */
public class MyReleaseMsgActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener{
    public static final String COMPLETE_ORDER = "completeOrder";
    public static final String DELETE_ORDER = "deleteOrder";
    public static final String UPDATE_SCORE = "updateScore";
    public static final String MY_RELEASE_MSG_ACTIVITY = "MyReleaseMsgActivity";
    public static final int REQUEST_CODE = 1;
    String pinfeng = "";
    private TextView dan_lv_name;
    private TextView dan_lv_time;
    private TextView dan_lv_school;
    private TextView dan_lv_type;
    private TextView dan_lv_location;
    private TextView dan_lv_content;
    private TextView dan_lv_state;
    private TextView dan_lv_money;
    private TextView dan_tv_phone;

    private TextView recName;
    private TextView recSchool;
    private TextView recPhonenum;

    private ImageView recGenderPic;
    private ImageView sex;

    /*删除*/
    private LinearLayout dan_lv_delete;
    private Button lv_click_deletebut;
    /*完成单*/
    private LinearLayout dan_lv_finish;
    private Button lv_click_finishbut;
    /*评价*/
    private EditText et_evaluate;
    private Button but_evaluate;
    private LinearLayout ll_evaluate;

    private LinearLayout backbut;
    private OrderMsgAdapterViewAdapter mAdapter;
    /*接单人信息*/
    private LinearLayout llGrabMsg;

    /*举报*/
    private LinearLayout danLvJubao;
    private Button lvJubaoBtn;


    private OrderBean ob;
    UserBean  ub;
    private MyHandler handler;
    private SegmentedGroup segmented3;
    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.lv_myrelease_item_activity);
        handler = new MyHandler();
        dan_lv_name = getViewById(R.id.dan_lv_name);
        dan_lv_time = getViewById(R.id.dan_lv_time);
        dan_lv_school = getViewById(R.id.dan_lv_school);
        dan_lv_type = getViewById(R.id.dan_lv_type);
        dan_lv_location = getViewById(R.id.dan_lv_location);
        dan_lv_content = getViewById(R.id.dan_lv_content);
        dan_lv_state = getViewById(R.id.dan_lv_state);
        dan_lv_money = getViewById(R.id.dan_lv_money);
        dan_tv_phone = getViewById(R.id.dan_tv_phone);
        sex = getViewById(R.id.dan_lv_gender_pic);
        backbut = getViewById(R.id.backbut);

        recName = (TextView) findViewById(R.id.rec_name);
        recGenderPic = (ImageView) findViewById(R.id.rec_gender_pic);
        recSchool = (TextView) findViewById(R.id.rec_school);
        recPhonenum = (TextView) findViewById(R.id.rec_phonenum);

         /*删除*/
        dan_lv_delete = getViewById(R.id.dan_lv_delete);
        lv_click_deletebut = getViewById(R.id.lv_click_deletebut);
        /*完成单*/
        dan_lv_finish = getViewById(R.id.dan_lv_finish);
        lv_click_finishbut = getViewById(R.id.lv_click_finishbut);
          /*评价*/
        but_evaluate = getViewById(R.id.but_evaluate);
        ll_evaluate = getViewById(R.id.ll_evaluate);
        segmented3 = getViewById(R.id.segmented3);
        /*接单人信息*/
        llGrabMsg = (LinearLayout) findViewById(R.id.ll_grab_msg);
        /*举报*/
        danLvJubao = (LinearLayout) findViewById(R.id.dan_lv_jubao);
        lvJubaoBtn = (Button) findViewById(R.id.lv_jubao_btn);

    }

    @Override
    protected void setListener() {
        lv_click_deletebut.setOnClickListener(this);
        lv_click_finishbut.setOnClickListener(this);
        but_evaluate.setOnClickListener(this);
        backbut.setOnClickListener(this);
        segmented3.setOnCheckedChangeListener(this);
        lvJubaoBtn.setOnClickListener(this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        Bundle bundle=getIntent().getBundleExtra("bundle");
        ob = (OrderBean) bundle.get(Tab01Fragment_2.MY_ORDER_ITEM);

        String json ;
        SharedPreferences sharedPreferences =
                getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        json = sharedPreferences.getString("mUser", "null");
        Gson gson = new Gson();
        ub = gson.fromJson(json, UserBean.class);
        dan_lv_name.setText(ob.getStudent_name());
        dan_lv_time.setText(CurrentTime.subDay(ob.getTime()));
        dan_lv_school.setText(ob.getSchool());
        dan_lv_type.setText(ob.getLable());
        dan_lv_location.setText("启始:"+ob.getStartLocation()+"  "+"送达:"+ob.getEndLocation());
        dan_lv_content.setText(ob.getContent());
        dan_lv_money.setText("赚"+ob.getTip()+"元");
        if(ob.getSex().equals("男")){
            sex.setImageResource(R.drawable.gender_symbol_boy);
        }
        else{
            sex.setImageResource(R.drawable.gender_symbol_girl);
        }
        //状态设置
        switch (ob.getState()){
            case "0": //未接单可以删除
                ll_evaluate.setVisibility(View.GONE);
                dan_lv_finish.setVisibility(View.GONE);
                dan_lv_state.setText("新发布");
                dan_lv_state.setTextColor( getApplicationContext().getResources().getColor(R.color.orangered));
//                dan_lv_delete.setVisibility(View.GONE);
                llGrabMsg.setVisibility(View.GONE);
                break;
            case "1"://已经接单可以点完成
                ll_evaluate.setVisibility(View.GONE);
                dan_lv_delete.setVisibility(View.GONE);
                dan_lv_state.setText("被抢了");
                dan_lv_state.setTextColor( getApplicationContext().getResources().getColor(R.color.gray));
                loadGraberMsg();
                break;
            case "2"://已经完成可以评价
                dan_lv_state.setText("已完成");
                dan_lv_state.setTextColor( getApplicationContext().getResources().getColor(R.color.gray));
                if(ob.getScore()!=-1){
                    ll_evaluate.setVisibility(View.GONE);
                }
                dan_lv_finish.setVisibility(View.GONE);
                dan_lv_delete.setVisibility(View.GONE);
                loadGraberMsg();
                break;
            case "-1"://已被删除
                dan_lv_state.setText("已被删除");
                dan_lv_state.setTextColor( getApplicationContext().getResources().getColor(R.color.gray));
                ll_evaluate.setVisibility(View.GONE);
                dan_lv_finish.setVisibility(View.GONE);
                dan_lv_delete.setVisibility(View.GONE);
                llGrabMsg.setVisibility(View.GONE);
                break;
        }
//        Log.i("getRelease_student_number",ob.getRelease_student_number()+"");
//        Log.i("getStudent_number",ub.getStudent_number()+"");

        try {
            if(URLEncoder.encode(ob.getState(), "UTF-8").equals("1")||URLEncoder.encode(ob.getState(), "UTF-8").equals("2")){
                lv_click_deletebut.setVisibility(View.GONE);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if(ob.getScore() != -1&&!isLegal(ob.getComplaint_student_number())){
            danLvJubao.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 加载 抢单人的信息
     */
    private void loadGraberMsg() {
        UserBean userBean = new UserBean();
        userBean.setStudent_number(ob.getAcceptance_student_number());
        String[] params = {userBean.getUrl_QSingleStu()};
        LoadGraberTask loadGraberTask = new LoadGraberTask();
        loadGraberTask.execute(params);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.backbut:
                finish();
                break;
            case R.id.lv_click_deletebut:
                deleteTask();
                break;
            case R.id.lv_click_finishbut:
                finishTask();
                break;
            case R.id.but_evaluate:
                evaluateTask();
                break;
            case R.id.lv_jubao_btn:
                jubao();
                break;
        }
    }

    /**
     * 举报
     */
    private void jubao() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(MY_RELEASE_MSG_ACTIVITY, ob);
        Intent intent = new Intent();
        intent.putExtra("bundle", bundle);
        intent.setClass(getApplicationContext(), ComplaintActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode== REQUEST_CODE) {
            switch (resultCode){
                case RESULT_OK:
                    danLvJubao.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
//            Message message = new Message();
//            message.what = 1;
//            handler.sendMessage(message);
//            finish();
//            Intent intent = new Intent(MyReleaseMsgActivity.this, MyReleaseMsgActivity.class);
//            startActivity(intent);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }



    /**
     * 评分
     * @author Administrator
     * @time 2016/3/21 17:31
     *
     */
    private void evaluateTask() {
        if(pinfeng.equals("")){
            Toast.makeText(getApplicationContext(), "请先选择分数", Toast.LENGTH_SHORT).show();
            return;
        }
        OrderBean obt = new OrderBean();
        obt.setOrder_id(ob.getOrder_id());
        obt.setRelease_student_number(ob.getRelease_student_number());
        obt.setScore(Double.parseDouble(pinfeng));
        String[] params = {obt.getUrl_updateScore(), UPDATE_SCORE};
        OrderTask ot = new OrderTask();
        ot.execute(params);
    }

    /**
     *  完成任务
     * @author Administrator
     * @time 2016/3/21 17:15
     *
     */

    private void finishTask() {
        OrderBean obt = new OrderBean();
        obt.setOrder_id(ob.getOrder_id());
        obt.setRelease_student_number(ob.getRelease_student_number());
        String[] params = {obt.getUrl_finishTask(), COMPLETE_ORDER};

        OrderTask ot = new OrderTask();
        ot.execute(params);
    }
    /**
     * 删除未接单的任务
     * @author Administrator
     * @time 2016/3/20 17:09
     * 成功返回 success
     */
    public boolean isLegal(String str) {
        return str != null && str.length() > 0;
    }
    private void deleteTask() {
        OrderBean obt = new OrderBean();
        obt.setOrder_id(ob.getOrder_id());
        obt.setRelease_student_number(ob.getRelease_student_number());
        String[] params = {obt.getUrl_deleteTask(), DELETE_ORDER};

        OrderTask ot = new OrderTask();
        ot.execute(params);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.button31:
                this.pinfeng = "1";
                break;
            case R.id.button32:
                this.pinfeng = "2";
                break;
            case R.id.button33:
                this.pinfeng = "3";
                break;
            case R.id.button34:
                this.pinfeng = "4";
                break;
            case R.id.button35:
                this.pinfeng = "5";
                break;
            default:
                // Nothing to do
        }
    }

    /**
     * 单的处理网络操作
     * @author Administrator
     * @time 2016/3/20 11:42
     *
     */

    class OrderTask extends AsyncTask<String,Integer,String>{
        String result;
        String orderType;
        @Override
        protected String doInBackground(final String... params) {
            orderType = params[1];
            CommonRequest myrequest = new CommonRequest(params[0],params) {
                @Override
                public void convert(HttpURLConnection connection, String[] heads) {
                    connection.setRequestProperty("method",heads[1]);
                }
            };
            result = myrequest.getResult();
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            try {
                switch (orderType){
                    case DELETE_ORDER:
                        if(new String("success".getBytes("iso-8859-1"),"UTF-8").equals(new String(result.trim().getBytes("iso-8859-1"),"UTF-8"))){
                            Toast.makeText( getApplication(), "删除成功", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "已被人接单", Toast.LENGTH_SHORT).show();
                            ll_evaluate.setVisibility(View.GONE);
                            dan_lv_finish.setVisibility(View.GONE);
                            dan_lv_delete.setVisibility(View.GONE);
                            dan_lv_finish.setVisibility(View.VISIBLE);
                        }
                        break;
                    case COMPLETE_ORDER:
                        if(new String("success".getBytes("iso-8859-1"),"UTF-8").equals(new String(result.trim().getBytes("iso-8859-1"),"UTF-8"))){
                            Toast.makeText(getApplicationContext(), "已被人接单", Toast.LENGTH_SHORT).show();
                            ll_evaluate.setVisibility(View.VISIBLE);
                            dan_lv_finish.setVisibility(View.GONE);
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "网络出现故障", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        break;
                    case UPDATE_SCORE:
                        if(new String("success".getBytes("iso-8859-1"),"UTF-8").equals(new String(result.trim().getBytes("iso-8859-1"),"UTF-8"))){
                            Toast.makeText(getApplicationContext(), "评价成功", Toast.LENGTH_SHORT).show();
                            ll_evaluate.setVisibility(View.GONE);
                            danLvJubao.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "网络出现故障", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        break;

                }


            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    class LoadGraberTask extends AsyncTask<String, Integer, String>{
        String result = "";
        UserBean grab_ub;
        @Override
        protected String doInBackground(String... params) {
            CommonRequest myrequest = new CommonRequest(params[0],params) {
                @Override
                public void convert(HttpURLConnection connection, String[] heads) {
                }
            };
            result = myrequest.getResult();
            Log.i("LoadGraberTask", result);
            Gson gson = new Gson();
            grab_ub = gson.fromJson(result, UserBean.class);
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(result == null || !(result.length()>0)){
            }else{
                recName.setText(grab_ub.getStudent_name());
                recSchool.setText(grab_ub.getSchool());
                recPhonenum.setText(grab_ub.getCell_phone());
                recPhonenum.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + recPhonenum.getText().toString()));
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        startActivity(intent);
                    }
                });
                if(grab_ub.getSex().equals("男")){
                    recGenderPic.setImageResource(R.drawable.gender_symbol_boy);
                }
                else{
                    recGenderPic.setImageResource(R.drawable.gender_symbol_girl);
                }
            }
        }

    }


    class  MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    break;

            }
        }
    }
}
