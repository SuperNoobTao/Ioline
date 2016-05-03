package zucc.edu.cn.ioline;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import zucc.edu.cn.Bean.OrderBean;

/**
 * Created by Administrator on 2016/3/23.
 */
public class MyChatMsgActivity extends BaseActivity {
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
        setContentView(R.layout.lv_question_item_activity);
        backbut = getViewById(R.id.backbut);
    }

    @Override
    protected void setListener() {
        backbut.setOnClickListener(this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backbut:
                finish();
                break;
        }
    }

}
