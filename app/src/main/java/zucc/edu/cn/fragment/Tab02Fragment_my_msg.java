package zucc.edu.cn.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
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
import zucc.edu.cn.Bean.QuestionBean;
import zucc.edu.cn.Net.CommonRequest;
import zucc.edu.cn.adapter.QuestionBeanAdapterViewAdapter;
import zucc.edu.cn.ioline.AddQuestion;
import zucc.edu.cn.ioline.QuestionItemActivity;
import zucc.edu.cn.ioline.R;
import zucc.edu.cn.util.CheckNet;

public class Tab02Fragment_my_msg extends Fragment implements
		BGARefreshLayout.BGARefreshLayoutDelegate,
		OnScrollListener,AdapterView.OnItemClickListener,BGAOnItemChildClickListener{
	public static final String QUESTION_ITEM = "QUESTION_ITEM";
	public Handler handler;
	private ListView mListView;
	private BGARefreshLayout mRefreshLayout;
	private QuestionBeanAdapterViewAdapter mAdapter;
	private List<QuestionBean> mDatas;
	View view;

	String user_id = null;
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
		view = inflater.inflate(R.layout.tab02_qlv, container, false);
		initView(savedInstanceState);
		initEvent();
		control(savedInstanceState);
		return view;
	}

	private void initView(Bundle savedInstanceState) {
		if(mDatas == null)
			mDatas = new ArrayList<QuestionBean>();
		mRefreshLayout = (BGARefreshLayout) view.findViewById(R.id.r02_listview_refresh);
		mListView = (ListView) view.findViewById(R.id.tab02_lv_1);
	}
	private void initEvent() {
		mRefreshLayout.setDelegate(this);
		mListView.setOnItemClickListener(this);
		mListView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			}
		});
		mAdapter = new QuestionBeanAdapterViewAdapter(getContext());
		mAdapter.setOnItemChildClickListener(this);
	}
	private void control(Bundle savedInstanceState) {
		mRefreshLayout.setRefreshViewHolder(new BGANormalRefreshViewHolder(getContext(), true));
		mRefreshLayout.setCustomHeaderView(null, false);
		mListView.setAdapter(mAdapter);

		SharedPreferences sharedPreferences =
				getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
		user_id = sharedPreferences.getString("user_id", "null");

	}
	@Override
	public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {


		//要先判断当前的登录状态
		if(!user_id.equals("null")&&!user_id.equals(null)){
			//登入成功，进入AddQuestion activity
			if (CheckNet.checkNetworkAvailable(getContext())) {
				// 如果网络可用，则加载网络数据
				new AsyncTask<Void, Void, Void>() {
					String result;
					@Override
					protected Void doInBackground(Void... params) {
						try {
							QuestionBean questionBean = new QuestionBean();
								questionBean.setStudent_number(user_id);
							String [] param = {questionBean.getUrl_getMyQuestion()};
							CommonRequest request = new CommonRequest(param[0],param) {
								@Override
								public void convert(HttpURLConnection connection, String[] heads) {
									connection.setRequestProperty("method", "getMyQuestions");
								}
							};
							result = request.getResult();
							Gson gson = new Gson();
							mDatas.clear();
							Type type = new TypeToken<List<QuestionBean>>(){}.getType();
							List<QuestionBean> list = gson.fromJson(result,type);
							for(int i = 0; i < list.size();i++){
								mDatas.add(list.get(i));
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						return null;
					}
					@Override
					protected void onPostExecute(Void aVoid) {
						try {
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
		else
			Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();

	}

	@Override
	public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {

		if (CheckNet.checkNetworkAvailable(getContext())) {
			// 如果网络可用，则异步加载网络数据，并返回true，显示正在加载更多
			new AsyncTask<Void, Void, Void>() {
				@Override
				protected Void doInBackground(Void... params) {
					return null;
				}
				@Override
				protected void onPostExecute(Void aVoid) {
					// 加载完毕后在UI线程结束加载更多
					//					mAdapter.setDatas(mDatas);
//					mAdapter.notifyDataSetInvalidated();
//					mAdapter.notifyDataSetChanged();
					mRefreshLayout.endLoadingMore();
				}
			}.execute();

			return true;
		} else {
			// 网络不可用，返回false，不显示正在加载更多
			Toast.makeText(getContext(), "网络不可用", Toast.LENGTH_SHORT).show();
			return false;
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	}

	@Override
	public void onItemChildClick(ViewGroup parent, View childView, int position) {
		Bundle bundle = new Bundle();
		bundle.putSerializable(QUESTION_ITEM, mAdapter.getItem(position));
		Intent intent = new Intent();
		intent.putExtra("bundle", bundle);
		intent.setClass(getActivity(), QuestionItemActivity.class);
		startActivity(intent);
	}

}
