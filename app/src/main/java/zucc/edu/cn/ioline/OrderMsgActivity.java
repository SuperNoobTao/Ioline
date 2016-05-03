package zucc.edu.cn.ioline;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;

import zucc.edu.cn.Bean.OrderBean;
import zucc.edu.cn.Bean.UserBean;
import zucc.edu.cn.Net.CommonRequest;
import zucc.edu.cn.adapter.OrderMsgAdapterViewAdapter;
import zucc.edu.cn.fragment.Tab01Fragment_1;
import zucc.edu.cn.util.CurrentTime;

/**
 * Created by Administrator on 2016/3/19.
 */
public class OrderMsgActivity extends BaseActivity{
    private TextView dan_lv_name;
    private TextView dan_lv_time;
    private TextView dan_lv_school;
    private TextView dan_lv_type;
    private TextView dan_lv_location;
    private TextView dan_lv_content;
    private TextView dan_lv_state;
    private TextView dan_lv_money;
    private TextView dan_tv_phone;

    private ImageView sex;

    private Button lv_click_grabbut;
    private LinearLayout dan_lv_phone;
    private LinearLayout backbut;
    private OrderMsgAdapterViewAdapter mAdapter;

    private OrderBean ob;
    UserBean  ub;
    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.lv_click_activity);
        dan_lv_name = getViewById(R.id.dan_lv_name);
        dan_lv_time = getViewById(R.id.dan_lv_time);
        dan_lv_school = getViewById(R.id.dan_lv_school);
        dan_lv_type = getViewById(R.id.dan_lv_type);
        dan_lv_location = getViewById(R.id.dan_lv_location);
        dan_lv_content = getViewById(R.id.dan_lv_content);
        dan_lv_state = getViewById(R.id.dan_lv_state);
        dan_lv_money = getViewById(R.id.dan_lv_money);
        lv_click_grabbut = getViewById(R.id.lv_click_grabbut);

        dan_lv_phone = getViewById(R.id.dan_lv_phone);
        dan_tv_phone = getViewById(R.id.dan_tv_phone);
        sex = getViewById(R.id.dan_lv_gender_pic);
        backbut = getViewById(R.id.backbut);


    }

    @Override
    protected void setListener() {
        lv_click_grabbut.setOnClickListener(this);
        dan_lv_phone.setOnClickListener(this);
        backbut.setOnClickListener(this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        Bundle bundle=getIntent().getBundleExtra("bundle");
        ob = (OrderBean) bundle.get(Tab01Fragment_1.ORDER_ITEM);
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
        dan_lv_location.setText("地点："+ob.getEndLocation());
        dan_lv_content.setText(ob.getContent());

        dan_lv_state.setText(ob.getState());

        switch ( ob.getState()){
            case "0":
                dan_lv_state.setText( "新发布");
                dan_lv_state.setTextColor(getApplicationContext().getResources().getColor(R.color.orangered));
                break;
            case "1":
                dan_lv_state.setText("被抢了");
                dan_lv_state.setTextColor( getApplicationContext().getResources().getColor(R.color.gray));
                break;
            case "2":
                dan_lv_state.setText("已完成");
                dan_lv_state.setTextColor( getApplicationContext().getResources().getColor(R.color.gray));
                break;
        }
        dan_lv_money.setText("赚"+ob.getTip()+"元");

        if(ob.getSex().equals("男")){
            sex.setImageResource(R.drawable.gender_symbol_boy);
        }
        else{
            sex.setImageResource(R.drawable.gender_symbol_girl);
        }
//        Log.i("getRelease_student_number",ob.getRelease_student_number()+"");
//        Log.i("getUser_id",ub.getUser_id()+"");

        try {
            if(ob.getRelease_student_number().equals(ub.getStudent_number()+"")){
                lv_click_grabbut.setVisibility(View.GONE);

            }
            if(ob.getState().equals("1")||ob.getState().equals("2")){
                lv_click_grabbut.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.lv_click_grabbut:
                GrabTask();
                break;
            case R.id.dan_lv_phone:
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + dan_tv_phone.getText().toString()));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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
                break;
            case R.id.backbut:
                finish();
                break;
        }
    }

    /**
     * 抢单
     * @author Administrator
     * @time 2016/3/20 11:42
     *
     */

    private void GrabTask() {
        String user_id=null;
        SharedPreferences sharedPreferences =
                getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", "null");

        if(!user_id.equals("null")&&!user_id.equals(null)){
            OrderBean or = new OrderBean();
            or.setAcceptance_student_number(ub.getStudent_number()+"");
            or.setOrder_id(ob.getOrder_id());

            String[] params = {or.getUrl_AcceptOrder()};
            AcceptOrder ao = new AcceptOrder();
            ao.execute(params);
        }
        else{
            Toast.makeText(getApplicationContext(),"请先登录",Toast.LENGTH_SHORT).show();
        }

    }
    /**
     * 抢单的网络操作
     * @author Administrator
     * @time 2016/3/20 11:42
     *
     */

    class AcceptOrder extends AsyncTask<String,Integer,String>{
        String result;
        @Override
        protected String doInBackground(String... params) {
            CommonRequest myrequest = new CommonRequest(params[0],params) {
                @Override
                public void convert(HttpURLConnection connection, String[] heads) {
                    connection.setRequestProperty("method","acceptOrder");
                }
            };
            result = myrequest.getResult();
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if(new String("success".getBytes("iso-8859-1"),"UTF-8").equals(new String(result.trim().getBytes("iso-8859-1"),"UTF-8"))) {
                //抢单成功
                    Toast.makeText(getApplicationContext(), "抢单成功", Toast.LENGTH_SHORT).show();
                    dan_lv_phone.setVisibility(View.VISIBLE);
                    dan_tv_phone.setText(ob.getCell_phone());
                    lv_click_grabbut.setVisibility(View.GONE);
                    dan_lv_state.setText("被抢了");
                }
                else{
                    Toast.makeText(getApplicationContext(), "已被抢走", Toast.LENGTH_SHORT).show();
                    lv_click_grabbut.setVisibility(View.GONE);
                    dan_lv_state.setText("被抢了");
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
    }
}
