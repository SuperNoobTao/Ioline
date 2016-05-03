package zucc.edu.cn.ioline;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import zucc.edu.cn.Bean.OrderBean;
import zucc.edu.cn.Bean.UserBean;
import zucc.edu.cn.Net.CommonRequest;
import zucc.edu.cn.adapter.CommonAdapter;
import zucc.edu.cn.util.CurrentTime;
import zucc.edu.cn.util.ViewHolder;

/**
 * Created by Administrator on 2016/3/29.
 */
public class MyGrabActivity extends BaseActivity implements AdapterView.OnItemClickListener{

    private CommonAdapter mAdapter;
    private List<OrderBean> mDatas;
    private ListView mListView;

    private LinearLayout backbut;

    private UserBean ub;
    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.release_activity);
        backbut = getViewById(R.id.backbut);
        mListView = getViewById(R.id.release_activity_lv);
    }

    @Override
    protected void setListener() {
        backbut.setOnClickListener(this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

        SharedPreferences sharedPreferences =
                getApplication().getSharedPreferences("user", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        ub = gson.fromJson(sharedPreferences.getString("mUser", "null"), UserBean.class);

        if(mDatas == null){
            mDatas = new ArrayList<OrderBean>();
        }
        mAdapter = new CommonAdapter<OrderBean>(getApplicationContext(),mDatas,R.layout.lv_item_mygrab) {
            @Override
            public void convert(ViewHolder holder, OrderBean orderBean) {
                holder   .setText(R.id.dan_lv_name, orderBean.getStudent_name())
                        .setText(R.id.dan_lv_school, orderBean.getSchool())
                        .setText(R.id.dan_lv_type, orderBean.getLable())
                        .setText(R.id.dan_lv_content, orderBean.getContent())
                        .setText(R.id.dan_lv_money,"赚"+orderBean.getTip()+"元");

                switch ( orderBean.getState()){
                    case "0":
                        holder.setText(R.id.dan_lv_state, "新发布");
                        holder.setTextColor(R.id.dan_lv_state, mContext.getResources().getColor(R.color.orangered));
                        break;
                    case "1":
                        holder.setText(R.id.dan_lv_state, "被抢了");
                        holder.setTextColor(R.id.dan_lv_state, mContext.getResources().getColor(R.color.gray));
                        break;
                    case "2":
                        holder.setText(R.id.dan_lv_state, "已完成");
                        holder.setTextColor(R.id.dan_lv_state, mContext.getResources().getColor(R.color.gray));
                        break;
                    case "-1":
                        holder.setText(R.id.dan_lv_state, "已关闭");
                        holder.setTextColor(R.id.dan_lv_state, mContext.getResources().getColor(R.color.gray));
                        break;

                }

                if(orderBean.getSex().equals("男")){
                    holder.setImageResource(R.id.dan_lv_gender_pic,R.drawable.gender_symbol_boy);
                }
                else{
                    holder.setImageResource(R.id.dan_lv_gender_pic,R.drawable.gender_symbol_girl);
                }

                holder.setText(R.id.dan_lv_time, CurrentTime.subDay(orderBean.getTime()));
            }

        };
        mListView.setAdapter(mAdapter);


        mListView.setOnItemClickListener(this);

        NetTask nt = new NetTask();
        nt.execute();
    }
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backbut:
                finish();
                break;

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Bundle bundle = new Bundle();
        bundle.putSerializable("mygrab", mDatas.get(position));
        Intent intent = new Intent();
        intent.putExtra("bundle", bundle);
        intent.setClass(getApplicationContext(), MyGrabMsgActivity.class);
        startActivity(intent);

    }

    class NetTask extends AsyncTask<String,Integer,String>{
        String result;
        @Override
        protected String doInBackground(String... params) {
            OrderBean ob = new OrderBean();
            ob.setAcceptance_student_number(ub.getStudent_number()+"");
            //加载自己发布的单
            String[] parmas = {ob.getUrl_MyOrder_grab()};
            CommonRequest mrequest = new CommonRequest(parmas[0],parmas) {
                @Override
                public void convert(HttpURLConnection connection, String[] heads) {
                    connection.setRequestProperty("method","getAcceptedOrder");
                }
            };
            result = mrequest.getResult();
            Gson gson = new Gson();
            Type type = new TypeToken<List<OrderBean>>(){}.getType();

            List<OrderBean> tmp =  gson.fromJson(result,type) ;

            try {
                if(tmp != null && tmp.size() > 0)
                for (int i = 0;i<tmp.size();i++){
                    mDatas.add(tmp.get(i));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            mAdapter.notifyDataSetChanged();
        }
    }
}
