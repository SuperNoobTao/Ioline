package zucc.edu.cn.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import zucc.edu.cn.Bean.UserBean;
import zucc.edu.cn.ioline.AboutActivity;
import zucc.edu.cn.ioline.MyGrabActivity;
import zucc.edu.cn.ioline.MyReleaseActivity;
import zucc.edu.cn.ioline.R;
import zucc.edu.cn.ioline.UpdatePwd;
import zucc.edu.cn.ioline.fedbackActivity;

public class Tab04Fragment_person extends Fragment implements View.OnClickListener{

	public Handler handler;
	private Thread mthread;
	View view;

	private RelativeLayout tab04_logout ;
	private RelativeLayout tab04_logsuccess;

	private LinearLayout tab04_mytask;
	private LinearLayout fedbackLl;
	private LinearLayout aboutLl;
	private LinearLayout updatepwd_ll;

	private ImageView mine_title1;
	private TextView mine_title2;
	private TextView mine_title3;
	private Button tab04_editbut;

	private UserBean ub;

	private LinearLayout my_release_but;
	private LinearLayout my_grab_but;

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

		view = inflater.inflate(R.layout.tab04_1, container, false);
		String json ;
		//取sharedpreferences中数据的代码
		String user_id=null;
		SharedPreferences sharedPreferences =
				getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
		user_id = sharedPreferences.getString("user_id", "null");
		json = sharedPreferences.getString("mUser", "null");

		Gson gson = new Gson();
		ub = gson.fromJson(json, UserBean.class);


		init();
		if(!user_id.equals("null")&&!user_id.equals(null)){
			tab04_logsuccess.setVisibility(View.VISIBLE);
			tab04_mytask.setVisibility(View.VISIBLE);
//            tab04_setting.setVisibility(View.VISIBLE);
			tab04_logout.setVisibility(View.GONE);

			if(ub.getSex().equals("男")){
				mine_title1.setImageResource(R.drawable.boy);
			}else mine_title1.setImageResource(R.drawable.girl);
			mine_title2.setText(ub.getStudent_name());
			mine_title3.setText(ub.getSchool());
		}
		else{
			tab04_logsuccess.setVisibility(View.GONE);
			tab04_mytask.setVisibility(View.GONE);
//            tab04_setting.setVisibility(View.GONE);
			tab04_logout.setVisibility(View.VISIBLE);
		}
		return view;
	}

	private void init() {
		tab04_logout = (RelativeLayout) view.findViewById(R.id.tab04_logout);
		tab04_logsuccess = (RelativeLayout) view.findViewById(R.id.tab04_logsuccess);
		tab04_mytask = (LinearLayout) view.findViewById(R.id.tab04_mytask);
		fedbackLl = (LinearLayout) view.findViewById(R.id.fedback_ll);
		aboutLl = (LinearLayout) view.findViewById(R.id.about_ll);

		mine_title1 = (ImageView) view.findViewById(R.id.mine_title1);
		mine_title2 = (TextView) view.findViewById(R.id.mine_title2);
		mine_title3 = (TextView) view.findViewById(R.id.mine_title3);
		tab04_editbut = (Button) view.findViewById(R.id.tab04_exitbut);
		my_release_but = (LinearLayout) view.findViewById(R.id.my_release_but);
		my_grab_but = (LinearLayout) view.findViewById(R.id.my_grab_but);
		updatepwd_ll = (LinearLayout) view.findViewById(R.id.updatepwd_ll);


		my_release_but.setOnClickListener(this);
		my_grab_but.setOnClickListener(this);
		tab04_editbut.setOnClickListener(this);
		tab04_logout.setOnClickListener(this);
		fedbackLl.setOnClickListener(this);
		aboutLl.setOnClickListener(this);
		updatepwd_ll.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId())
		{
			case R.id.tab04_exitbut:
				logout();
				break;
			case R.id.tab04_logout:
				login();
				break;
			case R.id.my_release_but:
				toReleasePage();
				break;
			case R.id.my_grab_but:
				toGrabPage();
				break;
			case R.id.fedback_ll:
				fedback();
				break;
			case R.id.about_ll:
				aboutLl();
				break;
			case R.id.updatepwd_ll:
				updatepwd();
				break;
		}

	}
/**
 *	修改密码
 * @author Administrator
 * @time 2016/4/23 14:28
 *
 */
	private void updatepwd() {
		String user_id=null;
		SharedPreferences sharedPreferences =
				getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
		user_id = sharedPreferences.getString("user_id", "null");

		if(!user_id.equals("null")&&!user_id.equals(null)){
			Intent intent=new Intent();
			intent.setClass(this.getActivity(), UpdatePwd.class);
			startActivity(intent);
		}
		else{
			Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * turn to about activity
	 */
	private void aboutLl() {
		Intent intent=new Intent();
		intent.setClass(this.getActivity(), AboutActivity.class);
		startActivity(intent);
	}

	/**
	 *  turn to fedback activity
	 */
	private void fedback() {
		//要先判断当前的登录状态
		String user_id=null;
		SharedPreferences sharedPreferences =
				getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
		user_id = sharedPreferences.getString("user_id", "null");

		if(!user_id.equals("null")&&!user_id.equals(null)){
			Intent intent=new Intent();
			intent.setClass(this.getActivity(), fedbackActivity.class);
			startActivity(intent);
		}
		else{
			Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();
		}
	}

	private void toGrabPage() {
		Intent intent = new Intent();
		intent.setClass(getActivity(), MyGrabActivity.class);
		startActivity(intent);
	}
	private void toReleasePage() {
		Intent intent = new Intent();
		intent.setClass(getActivity(), MyReleaseActivity.class);
		startActivity(intent);
	}

	/**
	 * func:登录
	 *

	 *
	 *
	 * */
	private void login() {
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

	/**
	 * func:登出
	 * 1.线要删除登录状态
	 * 2.再把登录成功的那块界面隐藏
	 * 3.把隐藏的界面显示
	 *
	 *
	 * */
	private void logout() {
		//1.线要删除登录状态
		SharedPreferences sharedPreferences =
				getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.clear();
		editor.commit();

		// 2.再把登录成功的那块界面隐藏
		tab04_logsuccess.setVisibility(View.GONE);
		tab04_mytask.setVisibility(View.GONE);
//		tab04_setting.setVisibility(View.GONE);
		// 3.把隐藏的界面显示
		tab04_logout.setVisibility(View.VISIBLE);
	}

	@Override
	public void onResume() {
		super.onResume();
	}



	class myThread extends Thread {
		public void run() {
			super.run();
			while (Thread.interrupted() == false) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Message m = new Message();
				Tab04Fragment_person.this.handler.sendMessage(m);
			}
		}
	}


}
