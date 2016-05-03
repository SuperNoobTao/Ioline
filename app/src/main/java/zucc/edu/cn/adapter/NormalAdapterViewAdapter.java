package zucc.edu.cn.adapter;

import android.content.Context;


import cn.bingoogolapple.androidcommon.adapter.BGAAdapterViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import zucc.edu.cn.Bean.BeanDan;
import zucc.edu.cn.ioline.R;



/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/21 上午12:39
 * 描述:
 */
public class NormalAdapterViewAdapter extends BGAAdapterViewAdapter<BeanDan> {
    public NormalAdapterViewAdapter(Context context) {
        super(context, R.layout.fragment_home_listview);
    }
    @Override
    protected void setItemChildListener(BGAViewHolderHelper viewHolderHelper) {
        viewHolderHelper.setItemChildClickListener(R.id.lv_item);
    }

    @Override
    public void fillData(BGAViewHolderHelper viewHolderHelper, int position, BeanDan model) {
        viewHolderHelper.setText(R.id.dan_lv_name, model.getName()).
                setText(R.id.dan_lv_time, model.getTime())
                .setText(R.id.dan_lv_school, model.getSchool())
                .setText(R.id.dan_lv_type, model.getType())
                .setText(R.id.dan_lv_content, model.getContent())
                .setText(R.id.dan_lv_state, model.getState()+"")
                .setText(R.id.dan_lv_money,model.getMoney()+"");
        if(model.getSex().equals("男")){
            viewHolderHelper.setImageResource(R.id.dan_lv_gender_pic,R.drawable.gender_symbol_boy);
        }
        else{
            viewHolderHelper.setImageResource(R.id.dan_lv_gender_pic,R.drawable.gender_symbol_girl);
        }

    }



}