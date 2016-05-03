package zucc.edu.cn.adapter;

import android.content.Context;

import cn.bingoogolapple.androidcommon.adapter.BGAAdapterViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import zucc.edu.cn.Bean.OrderBean;
import zucc.edu.cn.ioline.R;

/**
 * Created by Administrator on 2016/3/19.
 */
public class OrderMsgAdapterViewAdapter extends BGAAdapterViewAdapter<OrderBean> {
    public OrderMsgAdapterViewAdapter(Context context) {
        super(context, R.layout.lv_click_activity);
    }
    @Override
    protected void setItemChildListener(BGAViewHolderHelper viewHolderHelper) {
        viewHolderHelper.setItemChildClickListener(R.id.lv_item);
    }
    @Override
    public void fillData(BGAViewHolderHelper viewHolderHelper, int position, OrderBean model) {
        viewHolderHelper.setText(R.id.dan_lv_name, model.getStudent_name()).
                setText(R.id.dan_lv_time, model.getTime()+"")
                .setText(R.id.dan_lv_school, model.getSchool())
                .setText(R.id.dan_lv_type, model.getLable())
                .setText(R.id.dan_lv_content, model.getContent())
                .setText(R.id.dan_lv_state, model.getState()+"")
                .setText(R.id.dan_lv_money,model.getTip()+"")
                .setText(R.id.dan_lv_location, model.getEndLocation());
        if(model.getSex().equals("男")){
            viewHolderHelper.setImageResource(R.id.dan_lv_gender_pic,R.drawable.gender_symbol_boy);
        }
        else{
            viewHolderHelper.setImageResource(R.id.dan_lv_gender_pic,R.drawable.gender_symbol_girl);
        }

    }
}
