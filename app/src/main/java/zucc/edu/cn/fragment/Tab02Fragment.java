package zucc.edu.cn.fragment;



import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import info.hoang8f.android.segmented.SegmentedGroup;
import zucc.edu.cn.ioline.AddQuestion;
import zucc.edu.cn.ioline.R;
import zucc.edu.cn.ioline.ReleaseOrderActivity;
import zucc.edu.cn.ioline.SearchActivity;


public class Tab02Fragment extends Fragment implements  RadioGroup.OnCheckedChangeListener,View.OnClickListener{
	View view;
	private FragmentManager fragmentManager;
	private FragmentTransaction transaction;
	private Fragment Tab02Fragment_qlv ;
	private Fragment Tab02Fragment_my_msg ;
	private SegmentedGroup segmented4;
	private Button tab02AddBtn;

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
		view=inflater.inflate(R.layout.tab02, container, false);
		init();
		fragmentManager = getFragmentManager();
		transaction=fragmentManager.beginTransaction();
		transaction.replace(R.id.tab02_frag, Tab02Fragment_qlv);
		transaction.commit();
		return view;
	}
	private void init() {
		segmented4 = (SegmentedGroup) view.findViewById(R.id.segmented4);
		Tab02Fragment_qlv = new Tab02Fragment_qlv();
		Tab02Fragment_my_msg = new Tab02Fragment_my_msg();
		segmented4.setOnCheckedChangeListener(this);
		tab02AddBtn = (Button) view.findViewById(R.id.tab02_add_btn);
		tab02AddBtn.setOnClickListener(this);
	}
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
			case R.id.button41:
				transaction = fragmentManager.beginTransaction();
//						Fragment homeFragment = new Tab01Fragment_1();
				transaction.replace(R.id.tab02_frag, Tab02Fragment_qlv);
				transaction.commit();
				break;
			case R.id.button42:
				transaction = fragmentManager.beginTransaction();
//						Fragment homeFragment = new Tab01Fragment_1();
				transaction.replace(R.id.tab02_frag, Tab02Fragment_my_msg);
				transaction.commit();
				break;

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.tab02_add_btn:
				addQuestion();				
				break;
		}

	}
	private void addQuestion() {
		//要先判断当前的登录状态
		String user_id=null;
		SharedPreferences sharedPreferences =
				getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
		user_id = sharedPreferences.getString("user_id", "null");
		if(!user_id.equals("null")&&!user_id.equals(null)){
			//登入成功，进入AddQuestion activity
			Intent intent=new Intent();
			intent.setClass(this.getActivity(), AddQuestion.class);
			startActivity(intent);
		}
		else
			Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();



	}
}
