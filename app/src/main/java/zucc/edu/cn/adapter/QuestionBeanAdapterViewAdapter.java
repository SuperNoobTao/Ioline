package zucc.edu.cn.adapter;

import android.content.Context;
import android.util.Log;

import cn.bingoogolapple.androidcommon.adapter.BGAAdapterViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import zucc.edu.cn.Bean.QuestionBean;
import zucc.edu.cn.ioline.R;
import zucc.edu.cn.util.CurrentTime;


/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/21 上午12:39
 * 描述:
 */
public class QuestionBeanAdapterViewAdapter extends BGAAdapterViewAdapter<QuestionBean>{

    public QuestionBeanAdapterViewAdapter(Context context) {
        super(context, R.layout.question_listview);
    }
    @Override
    protected void setItemChildListener(BGAViewHolderHelper viewHolderHelper) {
        viewHolderHelper.setItemChildClickListener(R.id.question_lv_ll);
    }

    @Override
    protected void fillData(BGAViewHolderHelper viewHolderHelper, int position, QuestionBean model) {
//        Log.i("fillData", String.valueOf(model.getReplynum()));
        viewHolderHelper
                .setText(R.id.question_lv_callback_num, String.valueOf(model.getReplynum()))
                .setText(R.id.question_lv_school, model.getSchool())
                .setText(R.id.question_lv_name, model.getStudent_number())
                .setText(R.id.question_lv_time, CurrentTime.subDay(model.getTime()))
                .setText(R.id.question_lv_content, model.getQuestion());
            if(model.getReplynum() == 0){
                viewHolderHelper.setBackgroundColor(R.id.question_lv_ll, mContext.getResources().getColor(R.color.recall_2));
            }else{
                viewHolderHelper.setBackgroundColor(R.id.question_lv_ll, mContext.getResources().getColor(R.color.recall_1));
            }
    }


}