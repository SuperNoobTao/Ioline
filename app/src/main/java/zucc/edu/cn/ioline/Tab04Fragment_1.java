package zucc.edu.cn.ioline;


import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class Tab04Fragment_1 extends Fragment implements View.OnClickListener{

	public Handler handler;
	private Thread mthread;
	View view;

	private RelativeLayout tab04_logout ;
	private RelativeLayout tab04_logsuccess;

	private LinearLayout tab04_mytask;
	private LinearLayout tab04_setting;

	private Button tab04_editbut;

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

		view=inflater.inflate(R.layout.tab04_1, container, false);

        //取sharedpreferences中数据的代码
        String user_id=null;
        SharedPreferences sharedPreferences =
                getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", "null");
        init();
        if(!user_id.equals("null")&&!user_id.equals(null)){
            tab04_logsuccess.setVisibility(View.VISIBLE);
            tab04_mytask.setVisibility(View.VISIBLE);
            tab04_setting.setVisibility(View.VISIBLE);
            tab04_logout.setVisibility(View.GONE);
        }
        else{
            tab04_logsuccess.setVisibility(View.GONE);
            tab04_mytask.setVisibility(View.GONE);
            tab04_setting.setVisibility(View.GONE);
            tab04_logout.setVisibility(View.VISIBLE);
        }
		return view;
	}

	private void init() {
		tab04_logout = (RelativeLayout) view.findViewById(R.id.tab04_logout);
		tab04_logsuccess = (RelativeLayout) view.findViewById(R.id.tab04_logsuccess);
		tab04_mytask = (LinearLayout) view.findViewById(R.id.tab04_mytask);
//		tab04_setting = (LinearLayout) view.findViewById(R.id.tab04_setting);
		tab04_editbut = (Button) view.findViewById(R.id.tab04_exitbut);
		tab04_editbut.setOnClickListener(this);
		tab04_logout.setOnClickListener(this);
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
		}

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
		transaction.replace(R.id.root_frame, new Tab04Fragment_2());
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
		tab04_setting.setVisibility(View.GONE);
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
				Tab04Fragment_1.this.handler.sendMessage(m);
			}
		}
	}


}
