package zucc.edu.cn.ioline;



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
import android.widget.Toast;


import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import zucc.edu.cn.Bean.UserBean;
import zucc.edu.cn.Net.CommonRequest;

public class Tab04Fragment_3 extends Fragment implements View.OnClickListener{
	View view;
	private EditText register_user;
	private EditText etPassword;
	private EditText etRePassword;
	private EditText etStudent_name;
	private EditText etSex;
	private EditText etPhone;
	private EditText edSchool;

	private Button registerbut;
	private Button a_r_d_regbut;

	private LinearLayout tab04_3_lt1;
	private LinearLayout tab04_3_lt2;



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
		etSex = (EditText) view.findViewById(R.id.a_r_d_sex);
		etPhone = (EditText) view.findViewById(R.id.a_r_d_phonenum);
		edSchool = (EditText) view.findViewById(R.id.a_r_d_school);

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
		}
	}

	private void queren() {
		UserBean ub = new UserBean();
		ub = new UserBean();
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
		if(TextUtils.isEmpty(etSex.getText())){
			Toast.makeText(getContext(), "性别不能为空", Toast.LENGTH_SHORT).show();
			return;
		}else ub.setSex(etSex.getText().toString());
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
			return null;
		}
		@Override
		protected void onPostExecute(String s) {
			super.onPostExecute(s);
			try {
				if(new String("success".getBytes("iso-8859-1"),"UTF-8").equals(new String(result.trim().getBytes("iso-8859-1"),"UTF-8"))){

					Toast.makeText(getContext(), "注册成功", Toast.LENGTH_SHORT).show();
					//修改状态 这里的是为了记录登录状态的
					SharedPreferences sharedPreferences =
							getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
					SharedPreferences.Editor editor = sharedPreferences.edit();
					editor.putString("user_id", ub.getStudent_number());
					Gson gson = new Gson();
					editor.putString("mUser", gson.toJson(ub));

					editor.commit();



					//替换 当前的fragment
					FragmentTransaction transaction = getFragmentManager()
							.beginTransaction();
					transaction.replace(R.id.root_frame, new Tab04Fragment_1());
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
