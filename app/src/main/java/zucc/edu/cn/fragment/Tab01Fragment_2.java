package zucc.edu.cn.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildClickListener;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import zucc.edu.cn.Bean.BeanDan;
import zucc.edu.cn.Bean.OrderBean;
import zucc.edu.cn.Bean.UserBean;
import zucc.edu.cn.Net.CommonRequest;
import zucc.edu.cn.adapter.MyGrabOrderAdapterViewAdapter;
import zucc.edu.cn.adapter.OrderBeanAdapterViewAdapter;
import zucc.edu.cn.ioline.MyReleaseMsgActivity;
import zucc.edu.cn.ioline.OrderMsgActivity;
import zucc.edu.cn.ioline.R;
import zucc.edu.cn.adapter.CommonAdapter;
import zucc.edu.cn.util.CheckNet;
import zucc.edu.cn.util.RefreshableView;
import zucc.edu.cn.util.ViewHolder;
/**
 * 我的发布
 * @author Administrator
 * @time 2016/3/20 12:06
 *
 */

public class Tab01Fragment_2 extends Fragment implements
		BGARefreshLayout.BGARefreshLayoutDelegate,
		BGAOnItemChildClickListener,AdapterView.OnItemClickListener   {

	private static final String ORDER_ITEM = "";
	public static final String MY_ORDER_ITEM = "MY_ORDER_ITEM";
	private BGARefreshLayout mRefreshLayout;
	private ListView mListView;
	private MyGrabOrderAdapterViewAdapter mAdapter;
	private List<OrderBean> mDatas;
	private List<OrderBean> obl;

	View view;
	MyHandler handler;
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
		view = inflater.inflate(R.layout.bgareflash_myrelease, container, false);
		initView(savedInstanceState);
		setListener();
		processLogic(savedInstanceState);
		return view;
	}

	private void initView(Bundle savedInstanceState) {
		if(mDatas == null)
			mDatas = new ArrayList<OrderBean>();
		mRefreshLayout = (BGARefreshLayout) view.findViewById(R.id.rl_listview_refresh);
		mListView = (ListView) view.findViewById(R.id.tab01_lv_dan);
	}
	private void setListener() {
		mRefreshLayout.setDelegate(this);
		mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
//                Log.i(TAG, "滚动状态变化");
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                Log.i(TAG, "正在滚动");
			}
		});
		mListView.setOnItemClickListener(this);


		mAdapter = new MyGrabOrderAdapterViewAdapter(getContext());
		mAdapter.setOnItemChildClickListener(this);
	}
	private void processLogic(Bundle savedInstanceState) {
		mRefreshLayout.setRefreshViewHolder(new BGANormalRefreshViewHolder(getContext(), true));
		mRefreshLayout.setCustomHeaderView(null, false);
		mListView.setAdapter(mAdapter);
	}

	String user_id=null;
	UserBean ub ;
	@Override
	public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
		SharedPreferences sharedPreferences =
				getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
		Gson gson = new Gson();
		ub = gson.fromJson(sharedPreferences.getString("mUser", "null"), UserBean.class);
		user_id = sharedPreferences.getString("user_id", "null");
		if(user_id.equals("null")&&!user_id.equals(null)){
			Message message = new Message();
			message.what = 4;
			handler.sendMessage(message);
			mRefreshLayout.endRefreshing();
			return;
		}
		if (CheckNet.checkNetworkAvailable(getContext())) {
			new AsyncTask<Void, Void, Void>(){
			String result;
				@Override
				protected Void doInBackground(Void... params) {
					OrderBean ob = new OrderBean();
					ob.setRelease_student_number(ub.getStudent_number()+"");
					//加载自己发布的单
					String[] parmas = {ob.getUrl_MyOrder_releases()};
					CommonRequest mrequest = new CommonRequest(parmas[0],parmas) {
						@Override
						public void convert(HttpURLConnection connection, String[] heads) {
							connection.setRequestProperty("method","getReleasedOrder");
						}
					};
					result = mrequest.getResult();
					Gson gson = new Gson();
					Type type = new TypeToken<List<OrderBean>>(){}.getType();
					obl = gson.fromJson(result,type);
					mDatas = obl;
					return null;
				}
				@Override
				protected void onPostExecute(Void aVoid) {
					try {
						Log.i("InitTask********",result);
						mAdapter.setDatas(mDatas);
						mRefreshLayout.endRefreshing();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}.execute();
		}

		else
		{
			// 网络不可用，结束下拉刷新
			Toast.makeText(getContext(), "网络不可用", Toast.LENGTH_SHORT).show();
			mRefreshLayout.endRefreshing();
		}


	}

	@Override
	public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
		if (CheckNet.checkNetworkAvailable(getContext())) {
			new AsyncTask<Void, Void, Void>(){
				@Override
				protected Void doInBackground(Void... params) {
					return null;
				}
				@Override
				protected void onPostExecute(Void aVoid) {
					super.onPostExecute(aVoid);
					mRefreshLayout.endRefreshing();
				}
			}.execute();
		}
		else
		{
			// 网络不可用，结束下拉刷新
			Toast.makeText(getContext(), "网络不可用", Toast.LENGTH_SHORT).show();
			mRefreshLayout.endRefreshing();
		}


		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	}
	@Override
	public void onItemChildClick(ViewGroup parent, View childView, int position) {
		Bundle bundle = new Bundle();
		bundle.putSerializable(MY_ORDER_ITEM, mAdapter.getItem(position));
		Intent intent = new Intent();
		intent.putExtra("bundle", bundle);
		intent.setClass(getActivity(), MyReleaseMsgActivity.class);
		startActivity(intent);
	}


	class  MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what){
				case 1:
					break;
				case 4:
					Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();
					break;
			}
		}
	}



}
