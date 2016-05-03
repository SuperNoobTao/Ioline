package zucc.edu.cn.ioline;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import zucc.edu.cn.Bean.OrderBean;
import zucc.edu.cn.fragment.Tab01Fragment_3;
import zucc.edu.cn.util.CurrentTime;

/**
 * Created by Administrator on 2016/3/23.
 */
public class MyGrabMsgActivity extends BaseActivity {
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

    private OrderBean ob;

    private LinearLayout backbut;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.lv_mygrab_item_activity);

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
    }

    @Override
    protected void setListener() {
        dan_tv_phone.setOnClickListener(this);
        backbut.setOnClickListener(this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        Bundle bundle=getIntent().getBundleExtra("bundle");
        ob = (OrderBean) bundle.get(Tab01Fragment_3.MYGRAB);
        dan_lv_name.setText(ob.getStudent_name());
        dan_lv_time.setText(CurrentTime.subDay(ob.getTime()));
        dan_lv_school.setText(ob.getSchool());
        dan_lv_type.setText(ob.getLable());
        dan_lv_location.setText(ob.getEndLocation());
        dan_lv_content.setText(ob.getContent());
        dan_lv_money.setText("赚"+ob.getTip()+"元");
        dan_tv_phone.setText(ob.getCell_phone());
        if(ob.getSex().equals("男")){
            sex.setImageResource(R.drawable.gender_symbol_boy);
        }
        else{
            sex.setImageResource(R.drawable.gender_symbol_girl);
        }

        //状态设置
        switch (ob.getState()){
            case "0": //未接单可以删除
                dan_lv_state.setText("新发布");
                dan_lv_state.setTextColor( getApplicationContext().getResources().getColor(R.color.orangered));
//                dan_lv_delete.setVisibility(View.GONE);
                break;
            case "1"://已经接单可以点完成
                dan_lv_state.setText("被抢了");
                dan_lv_state.setTextColor( getApplicationContext().getResources().getColor(R.color.gray));
                break;
            case "2"://已经完成可以评价
                dan_lv_state.setText("已完成");
                dan_lv_state.setTextColor( getApplicationContext().getResources().getColor(R.color.gray));
                if(ob.getScore()!=-1){
                }
                break;
            case "-1"://已被删除
                dan_lv_state.setText("已被删除");
                dan_lv_state.setTextColor( getApplicationContext().getResources().getColor(R.color.gray));
                break;
        }

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dan_tv_phone:
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

}
