package zucc.edu.cn.ioline;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import info.hoang8f.android.segmented.SegmentedGroup;
import info.hoang8f.widget.FButton;
import zucc.edu.cn.Bean.OrderBean;
import zucc.edu.cn.Net.CommonRequest;
import zucc.edu.cn.adapter.CommonAdapter;
import zucc.edu.cn.db.LocationBeanDAO;
import zucc.edu.cn.util.CurrentTime;
import zucc.edu.cn.util.ViewHolder;

/**
 * Created by Administrator on 2016/4/21.
 */
public class SearchActivity extends BaseActivity implements AdapterView.OnItemClickListener{
    public static final String PAO_TUI = "跑腿";
    public static final String ZU_LIN = "租赁";
    public static final String OTHER = "其他";

    public static final String 新发布 = "新发布";
    public static final String 被抢了 = "被抢了";
    public static final String ORDER_ITEM = "OrderItem";
    public static String SEARCH_WAY = "";
    public static final String SEARCH_BY_LABEL = "label";
    public static final String SEARCH_BY_LOCATION = "location";

    private Button orBack;
    private FButton primaryButton;
    private FButton successButton;
    private TextView searchTv;

    private ListView searchList;
    private CommonAdapter mAdpater;
    private List<OrderBean> mDatas;

    private OrderBean ob;
    private String currentLable = "";
    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.search_activity);
        primaryButton = getViewById(R.id.primary_button);
        successButton = getViewById(R.id.success_button);
        searchList = getViewById(R.id.search_list);
        searchTv = getViewById(R.id.search_tv);
        orBack = getViewById(R.id.or_back);
    }

    @Override
    protected void setListener() {
        primaryButton.setOnClickListener(this);
        successButton.setOnClickListener(this);
        orBack.setOnClickListener(this);
    }
    @Override
    protected void processLogic(Bundle savedInstanceState) {
        if(ob ==null){
            ob = new OrderBean();
        }
        if(mDatas == null){
            mDatas = new ArrayList<OrderBean>();
        }
        mAdpater  = new CommonAdapter<OrderBean>(getApplication(), mDatas, R.layout.fragment_home_listview) {
            @Override
            public void convert(ViewHolder holder, OrderBean model) {
                holder.setText(R.id.dan_lv_name, model.getStudent_name())
                        .setText(R.id.dan_lv_school, model.getSchool())
                        .setText(R.id.dan_lv_type, model.getLable())
                        .setText(R.id.dan_lv_content, model.getContent())
                        .setText(R.id.dan_lv_money,"赚"+model.getTip()+"元");;
                //状态设置
                switch ( model.getState()){
                    case "0":
                        holder.setText(R.id.dan_lv_state, 新发布);
                        holder.setTextColor(R.id.dan_lv_state, mContext.getResources().getColor(R.color.orangered));
                        break;
                    case "1":
                        holder.setText(R.id.dan_lv_state, 被抢了);
                        holder.setTextColor(R.id.dan_lv_state, mContext.getResources().getColor(R.color.gray));
                        break;
                    case "2":
                        holder.setText(R.id.dan_lv_state, "已完成");
                        holder.setTextColor(R.id.dan_lv_state, mContext.getResources().getColor(R.color.gray));
                        break;

                }
                //性别
                if(model.getSex().equals("男")){
                    holder.setImageResource(R.id.dan_lv_gender_pic,R.drawable.gender_symbol_boy);
                }
                else{
                    holder.setImageResource(R.id.dan_lv_gender_pic,R.drawable.gender_symbol_girl);
                }
                //时间
                holder.setText(R.id.dan_lv_time, CurrentTime.subDay(model.getTime()));

            }
        };
        searchList.setOnItemClickListener(this);

        searchList.setAdapter(mAdpater);
        View emptyView = findViewById(R.id.empty);
        searchList.setEmptyView(emptyView);
    }

    public void onClick(View v) {

        switch (v.getId()){
            case R.id.primary_button:
                //弹窗选择
                SEARCH_WAY = SEARCH_BY_LABEL;
                //Create color picker view
                View view = this.getLayoutInflater().inflate(R.layout.label_pick_dialog, null);
                if (view == null) return;
                SegmentedGroup segmented_diaglog = (SegmentedGroup) view.findViewById(R.id.segmented_dialog);

                segmented_diaglog.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.button_d1://跑腿
                                ob.setLable(PAO_TUI);
                                currentLable = PAO_TUI;
                                break;
                            case R.id.button_d2://租赁
                                ob.setLable(ZU_LIN);
                                currentLable = ZU_LIN;
                                break;
                            case R.id.button_d3://其他
                                ob.setLable(OTHER);
                                currentLable = OTHER;
                                break;
                        }
                    }
                });
                //Config dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setView(view);
                builder.setTitle("选择类型");
                builder.setCancelable(true);
                builder.setNegativeButton("取消", null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 先判断又没有选
                        //如果有选，则进行网络操作读取数据
                        if(currentLable.equals("")){
                            Toast.makeText(SearchActivity.this, "请选择 单子类型", Toast.LENGTH_SHORT).show();
                            searchTv.setText("条件:"+"");
                        }
                        else{
                            searchTv.setText("条件:"+ob.getLable());
                        }
                        search();
                    }
                });
                builder.create().show();
                break;

            case R.id.success_button:
                //弹窗选择
                SEARCH_WAY = SEARCH_BY_LOCATION;
                //Create color picker view
                View view1 = this.getLayoutInflater().inflate(R.layout.location_pick_dialog, null);
                //read locatin from db
                LocationBeanDAO locationBeanDAO  = new LocationBeanDAO(getApplicationContext());
                String[]   personData = locationBeanDAO.getLocationToStringArg(locationBeanDAO.getAll());
                // set data
                ArrayAdapter<String>   pp = new ArrayAdapter<String>(
                        this,android.R.layout.simple_dropdown_item_1line,personData);

                final Spinner roStartLocation = (Spinner)view1.findViewById(R.id.ro_start_location);   roStartLocation.setAdapter(pp);
                final Spinner roLocation = (Spinner) view1.findViewById(R.id.ro_location);              roLocation.setAdapter(pp);

                //设置Spinner的样式
                pp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                roStartLocation.setPrompt("选择起始点");
                roLocation.setPrompt("选择送达点");
                //为Spinner设置适配器
                roStartLocation.setAdapter(pp);
                roLocation.setAdapter(pp);
                //添加Spinner事件监听

                roStartLocation.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        // TODO Auto-generated method stub
                        //设置显示当前选择的项
                            arg0.setVisibility(View.VISIBLE);
                        ob.setStartLocation(roStartLocation.getSelectedItem().toString());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }

                });

                //添加Spinner事件监听
                roLocation.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                            arg0.setVisibility(View.VISIBLE);
                        ob.setEndLocation(roLocation.getSelectedItem().toString());

                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub
                    }
                });


                //Config dialog
                final AlertDialog.Builder builder_1 = new AlertDialog.Builder(this);
                builder_1.setView(view1);
                builder_1.setTitle("选择类型");
                builder_1.setCancelable(true);
                builder_1.setNegativeButton("取消", null);
                builder_1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 先判断又没有选
                        //如果有选，则进行网络操作读取数据

                        if(roLocation.getSelectedItem().equals("")){
                            Toast.makeText(SearchActivity.this, "本校地址信息还没有录入，敬请期待", Toast.LENGTH_SHORT).show();
                            return;
                        }else ob.setEndLocation(roLocation.getSelectedItem().toString());

                        if (roStartLocation.getSelectedItem().equals("")){
                            Toast.makeText(SearchActivity.this, "本校地址信息还没有录入，敬请期待", Toast.LENGTH_SHORT).show();
                            return;
                        }else ob.setStartLocation(roStartLocation.getSelectedItem().toString());
                        search();
                    }
                });
                builder_1.create().show();
                break;
            case R.id.or_back:
                finish();
                break;
        }
    }

    private void search() {
        switch (SEARCH_WAY){
            case SEARCH_BY_LABEL:
                String[] params = {ob.getUrl_getOrderByLable(), "getOrderByLable"};
                SendTask sendTask = new SendTask();
                sendTask.execute(params);
                break;
            case SEARCH_BY_LOCATION:
                String[] params_1 = {ob.getUrl_getOrderByLocation(), "getOrderByLocation"};
                SendTask sendTask_1 = new SendTask();
                sendTask_1.execute(params_1);
                break;
        }
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ORDER_ITEM, (Serializable) mAdpater.getItem(position));
        Intent intent = new Intent();
        intent.putExtra("bundle", bundle);
        intent.setClass(getApplicationContext(), OrderMsgActivity.class);
        startActivity(intent);
    }

    private class SendTask extends AsyncTask<String, Integer, String> {
        String result;
        @Override
        protected String doInBackground(String... params) {
            try {
                CommonRequest request = new CommonRequest(params[0],params) {
                    @Override
                    public void convert(HttpURLConnection connection, String[] heads) {
                        connection.setRequestProperty("method", heads[1]);
                    }
                };
                result = request.getResult();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(isLegal(result)) {
                Gson gson = new Gson();
                Type type = new TypeToken<List<OrderBean>>(){}.getType();
                 gson.fromJson(result,type);
                mDatas.clear();
                mDatas.addAll((Collection<? extends OrderBean>) gson.fromJson(result,type));
                mAdpater.notifyDataSetChanged();
            }
        }

    }



}
