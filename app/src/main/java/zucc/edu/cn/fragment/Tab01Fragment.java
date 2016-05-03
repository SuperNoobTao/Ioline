package zucc.edu.cn.fragment;



import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import info.hoang8f.widget.FButton;
import zucc.edu.cn.ioline.R;
import zucc.edu.cn.ioline.ReleaseOrderActivity;
import zucc.edu.cn.ioline.SearchActivity;


public class Tab01Fragment extends Fragment implements View.OnClickListener{
	View view;


	private RadioGroup radioGroup;
	private FButton tab01_releasebut;
	private FButton tab01_search;

	private FragmentManager fragmentManager;
	private FragmentTransaction transaction;
	private Fragment Tab01Fragment_1 ;
	private Fragment Tab01Fragment_2 ;
	private Fragment Tab01Fragment_3 ;
	private Handler handler;


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
		handler = new MyHandler();
		view=inflater.inflate(R.layout.tab01, container, false);
		init();
		fragmentManager = getChildFragmentManager();

		transaction=fragmentManager.beginTransaction();
		transaction.replace(R.id.tab01_frag, Tab01Fragment_1);
		transaction.commit();

		radioGroup = (RadioGroup)view.findViewById(R.id.radioGroup1);
		((RadioButton)radioGroup.findViewById(R.id.radio0)).setChecked(true);

		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
					case R.id.radio0:
						transaction = fragmentManager.beginTransaction();
//						Fragment homeFragment = new Tab01Fragment_1();
						transaction.replace(R.id.tab01_frag, Tab01Fragment_1);
						transaction.commit();
						break;
					case R.id.radio1:
						transaction = fragmentManager.beginTransaction();
//						Fragment homeFragment = new Tab01Fragment_2();
						transaction.replace(R.id.tab01_frag, Tab01Fragment_2);
						transaction.commit();
						break;
					case R.id.radio2:
						transaction = fragmentManager.beginTransaction();
//						Fragment homeFragment = new Tab01Fragment_3();
						transaction.replace(R.id.tab01_frag, Tab01Fragment_3);
						transaction.commit();
						break;
				}

			}
		});

		return view;
	}

	private void init(){
		Tab01Fragment_1 = new Tab01Fragment_1();
		Tab01Fragment_2 = new Tab01Fragment_2();
		Tab01Fragment_3 = new Tab01Fragment_3();

		tab01_releasebut = (FButton) view.findViewById(R.id.tab01_releasebut);
		tab01_releasebut.setOnClickListener(this);

		tab01_search = (FButton) view.findViewById(R.id.tab01_search);
		tab01_search.setOnClickListener(this);

	}
	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.tab01_releasebut:
				releaseOrder();
				break;
			case R.id.tab01_search:
				searchOrder()
				;
				break;

		}
	}

	/**
	 * 查询任务
	 * @author Administrator
	 * @time 2016/3/29 12:02
	 *
	 */
	private void searchOrder() {
		//要先判断当前的登录状态

		String user_id=null;
		SharedPreferences sharedPreferences = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
		user_id = sharedPreferences.getString("user_id", "null");
		if(!user_id.equals("null")&&!user_id.equals(null)){

				Intent intent=new Intent();
			intent.setClass(this.getActivity(), SearchActivity.class);
			startActivity(intent);
		}
		else{
			Message message = new Message();
			message.what = 1;
			handler.sendMessage(message);
		}


	}

	/**
	 * 发布任务
	 * @author Administrator
	 * @time 2016/3/18 10:40
	 *
	 */

	private void releaseOrder() {
		//要先判断当前的登录状态

		String user_id=null;
		SharedPreferences sharedPreferences =
				getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
		user_id = sharedPreferences.getString("user_id", "null");

		if(!user_id.equals("null")&&!user_id.equals(null)){

			Intent intent=new Intent();
			intent.setClass(this.getActivity(), ReleaseOrderActivity.class);
			startActivity(intent);
		}
		else{
			Message message = new Message();
			message.what = 1;
			handler.sendMessage(message);
		}


	}


	@Override
	public void onResume() {
		super.onResume();
	}
	class  MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what){
				case 1:
					Toast.makeText(getActivity(), "请先登录",Toast.LENGTH_SHORT).show();
					break;

			}
		}
	}

}
