package zucc.edu.cn.ioline;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import java.net.HttpURLConnection;

import info.hoang8f.android.segmented.SegmentedGroup;
import zucc.edu.cn.Bean.OrderBean;
import zucc.edu.cn.Bean.UserBean;
import zucc.edu.cn.Net.CommonRequest;
import zucc.edu.cn.db.LocationBeanDAO;

/**
 * Created by Administrator on 2016/3/13.
 */
public class ReleaseOrderActivity extends AppCompatActivity implements  RadioGroup.OnCheckedChangeListener, View.OnClickListener{
    public static final String PAO_TUI = "跑腿";
    public static final String ZU_LIN = "租赁";
    public static final String OTHER = "其他";
    private String currentLable = "";

    private EditText ro_content;
    private Spinner ro_start_location;             //起点输入框
//    private MultiAutoCompleteTextView ro_start_location_mauto;  //下拉提示框
    private Spinner ro_location;                 //送达地输入框
//    private MultiAutoCompleteTextView ro_location_mauto;        //下拉提示框
    private EditText ro_lable;
    private EditText ro_tip;
    private EditText ro_phone_num;
    private EditText or_name;

    private Button or_back;
    private Button or_send;

    private SegmentedGroup segmented3;
    OrderBean ob;
    UserBean  ub;
    String[] personData ;
    ArrayAdapter<String> pp ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.release_order_activity);
        // read current accout 's msg
        String json ;
        SharedPreferences sharedPreferences =
                getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        json = sharedPreferences.getString("mUser", "null");
        Gson gson = new Gson();
        ub = gson.fromJson(json, UserBean.class);

        LocationBeanDAO locationBeanDAO  = new LocationBeanDAO(getApplicationContext());
        personData = locationBeanDAO.getLocationToStringArg(locationBeanDAO.getAll());


        pp = new ArrayAdapter<String>(
                this,android.R.layout.simple_dropdown_item_1line,personData);

        init();

        //设置Spinner的样式
        pp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //为对话框设置标题
        //也可在XMl文件中通过“android:prompt”设置
        ro_start_location.setPrompt("你来自哪个省");
        //为Spinner设置适配器

        ro_start_location.setAdapter(pp);

        //添加Spinner事件监听
        ro_start_location.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                //设置显示当前选择的项
                arg0.setVisibility(View.VISIBLE);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        //添加Spinner事件监听
        ro_location.setAdapter(pp);

        ro_location.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                //设置显示当前选择的项
                arg0.setVisibility(View.VISIBLE);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void init() {
        ro_content      = (EditText) findViewById(R.id.ro_content);
        ro_start_location = (Spinner) findViewById(R.id.ro_start_location);                    ro_start_location.setAdapter(pp);
//        ro_start_location_mauto = (MultiAutoCompleteTextView) findViewById(R.id.ro_start_location_mauto);   ro_start_location_mauto.setAdapter(pp);
        ro_location     = (Spinner) findViewById(R.id.ro_location);                            ro_location.setAdapter(pp);
//        ro_location_mauto = (MultiAutoCompleteTextView) findViewById(R.id.ro_location_mauto);               ro_location_mauto.setAdapter(pp);
        ro_tip          = (EditText) findViewById(R.id.ro_tip);
        ro_phone_num    = (EditText) findViewById(R.id.ro_phone_num);
        or_name         = (EditText) findViewById(R.id.or_name);
        ro_phone_num.setText(ub.getCell_phone());
        or_name.setText(ub.getStudent_name());
        or_back = (Button) findViewById(R.id.or_back);
        or_send = (Button) findViewById(R.id.or_send);
        segmented3 = (SegmentedGroup) findViewById(R.id.segmented3);
        segmented3.setOnCheckedChangeListener(this);
        or_back.setOnClickListener(this);
        or_send.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.or_back:
                back();
                break;
            case R.id.or_send:
                send();
                break;
        }
    }


    private void back() {
        finish();
    }

    private void send() {
        ob = new OrderBean();
        //判空
//        ro_tip.getText().toString().matches(("[0-9]+"))  //正则表达，有错之后改
        if(TextUtils.isEmpty(ro_tip.getText())){
            Toast.makeText(ReleaseOrderActivity.this, "金额不能为空,不能为非数字", Toast.LENGTH_SHORT).show();
            return;
        }else ob.setTip(Double.parseDouble(ro_tip.getText().toString()));

        if(currentLable.equals("")){
            Toast.makeText(ReleaseOrderActivity.this, "标签能为空", Toast.LENGTH_SHORT).show();
            return;
        }else  ob.setLable(currentLable);


        if(ro_location.getSelectedItem().equals("")){
            Toast.makeText(ReleaseOrderActivity.this, "请选择送达地址", Toast.LENGTH_SHORT).show();
            return;
        }else ob.setEndLocation(ro_location.getSelectedItem().toString());


        if (ro_start_location.getSelectedItem().equals("")){
            Toast.makeText(ReleaseOrderActivity.this, "请选择起始地址", Toast.LENGTH_SHORT).show();
            return;
        }else ob.setStartLocation(ro_start_location.getSelectedItem().toString());

        if(TextUtils.isEmpty(ro_content.getText())){
            Toast.makeText(ReleaseOrderActivity.this, "备注内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }else ob.setContent(ro_content.getText().toString());

        ob.setRelease_student_number(ub.getStudent_number()+"");
        Log.i("URL********", ob.getUrl_releaseOrder());
        String[] params = {ob.getUrl_releaseOrder()};
        SendTask sendTask = new SendTask();
        sendTask.execute(params);

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.button31:
                currentLable = this.PAO_TUI;
                break;
            case R.id.button32:
                currentLable = this.ZU_LIN;
                break;
            case R.id.button33:
                currentLable = this.OTHER;
                break;
        }

    }

    /**
     *
     * @author Administrator
     * @time 2016/3/17 19:02
     * fun： 发布任务
     */

    private class SendTask extends AsyncTask<String, Integer, String> {
        String result;
        @Override
        protected String doInBackground(String... params) {
            try {
                CommonRequest myrequest = new CommonRequest(params[0],params) {
                    @Override
                    public void convert(HttpURLConnection connection, String[] heads) {
                        connection.setRequestProperty("method", "releaseOrder");
                    }
                };
                result = myrequest.getResult();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                Log.i("result********", result);
                if(new String("success".getBytes("iso-8859-1"),"UTF-8").equals(new String(result.trim().getBytes("iso-8859-1"),"UTF-8"))){
                    Toast.makeText(getApplicationContext(), "插入成功", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "插入失败", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }
}
