package zucc.edu.cn.fragment;



import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.List;

import info.hoang8f.android.segmented.SegmentedGroup;
import zucc.edu.cn.Bean.LocationBean;
import zucc.edu.cn.Bean.UserBean;
import zucc.edu.cn.Net.CommonRequest;
import zucc.edu.cn.db.LocationBeanDAO;
import zucc.edu.cn.ioline.R;

public class Tab04Fragment_reg extends Fragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener{
	public static final String MAN = "男";
	public static final String WOMAN = "女";
	private String sex = "";
	View view;
	private EditText register_user;
	private EditText etPassword;
	private EditText etRePassword;
	private EditText etStudent_name;
	private EditText etPhone;
	private EditText edSchool;

	private Button registerbut;
	private Button a_r_d_regbut;

	private LinearLayout tab04_3_lt1;
	private LinearLayout tab04_3_lt2;

	private LinearLayout l_back_to_login;
	private SegmentedGroup segmented3;



	/**
	 * fun：注册
	 * */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		if(view != null){
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null) {
				parent.removeView(view);
			}
			return view;
		}
		view=inflater.inflate(R.layout.tab04_3, container, false);
		init();

		return view;
	}

	private void init() {
		register_user = (EditText) view.findViewById(R.id.register_user);
		registerbut = (Button) view.findViewById(R.id.registerbut);
		registerbut.setOnClickListener(this);

		a_r_d_regbut = (Button) view.findViewById(R.id.a_r_d_regbut);
		a_r_d_regbut.setOnClickListener(this);

		tab04_3_lt1 = (LinearLayout) view.findViewById(R.id.tab04_3_lt1);
		tab04_3_lt2 = (LinearLayout) view.findViewById(R.id.tab04_3_lt2);

		etPassword = (EditText) view.findViewById(R.id.a_r_d_pwd);
		etRePassword = (EditText) view.findViewById(R.id.a_r_d_rpwd);
		etStudent_name= (EditText) view.findViewById(R.id.a_r_d_name);
		etPhone = (EditText) view.findViewById(R.id.a_r_d_phonenum);
		edSchool = (EditText) view.findViewById(R.id.a_r_d_school);

		segmented3 =   (SegmentedGroup) view.findViewById(R.id.segmented1);
//		segmented3.setTintColor(Color.parseColor("#FFD0FF3C"), Color.parseColor("#FF7B07B2"));


		l_back_to_login = (LinearLayout) view.findViewById(R.id.l_back_to_login);
		l_back_to_login.setOnClickListener(this);
		segmented3.setOnCheckedChangeListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.registerbut:
				queren();
				break;
			case R.id.a_r_d_regbut:
				register();
				break;
			case R.id.l_back_to_login:
				back_tologin();
				break;
		}
	}

	private void back_tologin() {
		//替换 当前的fragment
		FragmentTransaction transaction = getFragmentManager()
				.beginTransaction();
		/*
		 * When this container fragment is created, we fill it with our first
		 * "real" fragment
		 */
		transaction.replace(R.id.root_frame, new Tab04Fragment_login());
		transaction.commit();
	}

	private void queren() {
		UserBean ub = new UserBean();
		ub = new UserBean();
		if(TextUtils.isEmpty(edSchool.getText())){
			Toast.makeText(getContext(), "学校不能为空", Toast.LENGTH_SHORT).show();
			return;
		}else ub.setSchool(edSchool.getText().toString());


		if(TextUtils.isEmpty(register_user.getText())){
			Toast.makeText(this.getContext(), "账号不能为空", Toast.LENGTH_SHORT).show();
			return;
		}else ub.setStudent_number(register_user.getText().toString());


		String[] params = {ub.getUrl_QSingleStu()};
		QueRenTask mregtask;
		mregtask = new QueRenTask();
		mregtask.execute(params);
	}
	UserBean ub;

	private void register() {
		ub = new UserBean();
		if(TextUtils.isEmpty(register_user.getText())){
			Toast.makeText(this.getContext(), "账号不能为空", Toast.LENGTH_SHORT).show();
			return;
		}else ub.setStudent_number(register_user.getText().toString());

		if(TextUtils.isEmpty(etPassword.getText())){
			Toast.makeText(getContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		if(TextUtils.isEmpty(etRePassword.getText())){
			Toast.makeText(getContext(), "重输密码不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		if(!etRePassword.getText().toString().equals(etPassword.getText().toString())){
			Toast.makeText(getContext(), "两次密码要相等", Toast.LENGTH_SHORT).show();
			return;
		}
		if(TextUtils.isEmpty(etStudent_name.getText())){
			Toast.makeText(getContext(), "名字不能为空", Toast.LENGTH_SHORT).show();
			return;
		}else ub.setStudent_name(etStudent_name.getText().toString());


		if(sex.equals("")){
			Toast.makeText(getContext(), "性别不能为空", Toast.LENGTH_SHORT).show();
			return;
		}else ub.setSex(sex);


		if(TextUtils.isEmpty(etPhone.getText())){
			Toast.makeText(getContext(), "电话不能为空", Toast.LENGTH_SHORT).show();
			return;
		}else ub.setCell_phone(etPhone.getText().toString());

		if(TextUtils.isEmpty(edSchool.getText())){
			Toast.makeText(getContext(), "学校不能为空", Toast.LENGTH_SHORT).show();
			return;
		}else ub.setSchool(edSchool.getText().toString());


		String[] params = {String.valueOf(etPassword.getText()), ub.getUrl_Reg()};
			ub.getUrl_Reg();
		RegTask mregtask;
		mregtask = new RegTask();
		mregtask.execute(params);


	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
			case R.id.button_man:
//				Toast.makeText(getActivity(), "One", Toast.LENGTH_SHORT).show();
				sex = this.MAN;
				break;
			case R.id.button_woman:
				sex = this.WOMAN;
				break;
		}
	}


	/**
	 * func：QueRen
	 * */

	private class QueRenTask extends AsyncTask<String, Integer, String>{
		String result;
		@Override
		protected String doInBackground(String... params) {
			CommonRequest myrequest = new CommonRequest(params[0],params) {
				@Override
				public void convert(HttpURLConnection connection, String[] heads) {
				}
			};
			result = myrequest.getResult();//
			return null;
		}

		@Override
		protected void onPostExecute(String s) {
			super.onPostExecute(s);
			try {
				Gson gson = new Gson();
				UserBean ubr = gson.fromJson(result, UserBean.class);

//				new String("null".getBytes("iso-8859-1"),"UTF-8").equals(new String(result.trim().getBytes("iso-8859-1"), "UTF-8"))
				if(ubr == null){
					Log.i("QueRen", result);
                    Toast.makeText(getContext(), "没有该账号可以正常注册",Toast.LENGTH_SHORT).show();
					tab04_3_lt2.setVisibility(View.VISIBLE);
					a_r_d_regbut.setVisibility(View.VISIBLE);
					registerbut.setVisibility(View.GONE);
					register_user.setEnabled(false);
					edSchool.setEnabled(false);
                }
                else{
                    Toast.makeText(getContext(), "已存在该账号",Toast.LENGTH_SHORT).show();
                }
			} catch (Exception e) {
				e.printStackTrace();
			}


		}

	}

	/**
	 * func：Register
	 * */
	private class RegTask extends  AsyncTask<String, Integer, String>{
		String result;
		@Override
		protected String doInBackground(String... params) {
			result = new CommonRequest(params[1],params) {
				@Override
				public void convert(HttpURLConnection connection, String[] heads) {
					connection.setRequestProperty("password", heads[0]);
				}
			}.getResult();

			try {
				if(new String("success".getBytes("iso-8859-1"),"UTF-8").equals(new String(result.trim().getBytes("iso-8859-1"),"UTF-8"))){
                    //加载学校
                    LocationBean lb =  new LocationBean();
                    lb.setSchool(ub.getSchool());
                    CommonRequest request = new CommonRequest(lb.getUrl_SchoolLocation(),params) {
                        @Override
                        public void convert(HttpURLConnection connection, String[] heads) {
                        }
                    };
                    Log.i("Location**********",request.getResult());
                    LocationBeanDAO lbDAO = new LocationBeanDAO(getContext());
                    Gson gson_1 = new Gson();
                    Type type = new TypeToken<List<LocationBean>>(){}.getType();
                    lbDAO.deletAll();//先删去所有的
                    lbDAO.addlist((List<LocationBean>) gson_1.fromJson(request.getResult(),type)); //每次登陆的时候都会加载学校信息到数据库

                }
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			return null;
		}
		@Override
		protected void onPostExecute(String s) {
			try {
				if(new String("success".getBytes("iso-8859-1"),"UTF-8").equals(new String(result.trim().getBytes("iso-8859-1"),"UTF-8"))){

					Toast.makeText(getContext(), "注册成功", Toast.LENGTH_SHORT).show();
					//修改状态 这里的是为了记录登录状态的
					SharedPreferences sharedPreferences =
							getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
					SharedPreferences.Editor editor = sharedPreferences.edit();
					editor.putString("user_id", ub.getStudent_number()+"");
					Gson gson = new Gson();
					editor.putString("mUser", gson.toJson(ub));
					editor.commit();



					//替换 当前的fragment
					FragmentTransaction transaction = getFragmentManager()
							.beginTransaction();
					transaction.replace(R.id.root_frame, new Tab04Fragment_person());
					transaction.commit();

				}
				else {
					Toast.makeText(getContext(), "注册失败注册超时", Toast.LENGTH_SHORT).show();
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

		}
	}


}
