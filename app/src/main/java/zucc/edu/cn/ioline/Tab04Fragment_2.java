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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;

import zucc.edu.cn.Bean.UserBean;
import zucc.edu.cn.Net.CommonRequest;

public class Tab04Fragment_2 extends Fragment implements View.OnClickListener{
	private EditText login_user;
	private EditText login_password;
	private TextView tab04_2_reg;
	private TextView tab04_2_back;
	private Button loginbut;
	View view;

	private LoginTask mTask;
	/**
	 * fun:登录fragment
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
		view=inflater.inflate(R.layout.tab04_2, container, false);
		init();
		return view;
	}

	private void init() {
		login_user = (EditText) view.findViewById(R.id.login_user);
		login_password = (EditText) view.findViewById(R.id.login_password);

		tab04_2_reg = (TextView) view.findViewById(R.id.tab04_2_reg);
		tab04_2_back = (TextView) view.findViewById(R.id.tab04_2_back);
		loginbut = (Button) view.findViewById(R.id.loginbut);

		tab04_2_reg.setOnClickListener(this);
		tab04_2_back.setOnClickListener(this);
		loginbut.setOnClickListener(this);

	}
	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.tab04_2_reg:
				register();
				break;
			case R.id.tab04_2_back:
				backtomain();
				break;
			case R.id.loginbut:
				login();
				break;
		}
	}

	/***
	 *
	 * func:back
	 *
	 */

	private void backtomain() {
		//替换 当前的fragment
		FragmentTransaction transaction = getFragmentManager()
				.beginTransaction();
		/*
		 * When this container fragment is created, we fill it with our first
		 * "real" fragment
		 */
		transaction.replace(R.id.root_frame, new Tab04Fragment_1());
		transaction.commit();
	}
/***
 *
 * func:register
 * first : judge the student_number wether exist
 * if student_number is exist Toast.maskText  账号存在
 * eles change current fragment
 */

	private void register() {
		//替换 当前的fragment
		FragmentTransaction transaction = getFragmentManager()
				.beginTransaction();
		/*
		 * When this container fragment is created, we fill it with our first
		 * "real" fragment
		 */
		transaction.replace(R.id.root_frame, new Tab04Fragment_3());
		transaction.addToBackStack(null);  //
		transaction.commit();
	}


	/**
	 * func:login
	 * */
	private UserBean ub;
	private void login() {
		ub = new UserBean();
		if(TextUtils.isEmpty(login_user.getText())){
			Toast.makeText(this.getContext(), "账号不能为空", Toast.LENGTH_SHORT).show();
			return;
		}else ub.setStudent_number(login_user.getText().toString());
		if(TextUtils.isEmpty(login_password.getText())){
			Toast.makeText(this.getContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		String[] params = {login_password.getText().toString(),ub.getUrl_Login()};  //前一个密码，后一个账号
		mTask = new LoginTask();
		mTask.execute(params);

	}

	/**
	 * func： 登陆
	 * */

	private class LoginTask extends AsyncTask<String, Integer, String> {
		String result;
		@Override
		protected String doInBackground(String... params) {
			try {
				CommonRequest myrequest = new CommonRequest(params[1],params) {
                    @Override
                    public void convert(HttpURLConnection connection, String[] heads) {
                        connection.setRequestProperty("password", heads[0].toString());
                    }
                };
				result = myrequest.getResult();

				if(new String("success".getBytes("iso-8859-1"),"UTF-8").equals(new String(result.trim().getBytes("iso-8859-1"),"UTF-8"))){
                    //修改状态
					CommonRequest request = new CommonRequest(ub.getUrl_QSingleStu(),params) {
						@Override
						public void convert(HttpURLConnection connection, String[] heads) {
						}
					};
					String mUser = request.getResult(); //登陆成功后要读取改账号的信息保存到本地
					Log.i("mUser************", mUser);
                    SharedPreferences sharedPreferences =
                            getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("user_id", ub.getStudent_number());
					editor.putString("mUser", mUser); //存的是 Json 字符串
                    editor.commit();
                }


			} catch (UnsupportedEncodingException e) {

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
					Toast.makeText(getContext(), "success", Toast.LENGTH_SHORT).show();
					//修改状态
					SharedPreferences sharedPreferences =
							getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
					SharedPreferences.Editor editor = sharedPreferences.edit();
					editor.putString("user_id", ub.getStudent_number());
					editor.commit();

					//替换 当前的fragment
					FragmentTransaction transaction = getFragmentManager()
							.beginTransaction();
					transaction.replace(R.id.root_frame, new Tab04Fragment_1());
					transaction.commit();
				}else{
					Toast.makeText(getContext(), "账号或密码错误", Toast.LENGTH_SHORT).show();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}


	}


}
