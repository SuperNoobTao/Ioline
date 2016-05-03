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
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildClickListener;
import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildLongClickListener;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;
import zucc.edu.cn.Bean.BeanDan;
import zucc.edu.cn.Bean.OrderBean;
import zucc.edu.cn.Bean.UserBean;
import zucc.edu.cn.Net.CommonRequest;
import zucc.edu.cn.adapter.NormalAdapterViewAdapter;
import zucc.edu.cn.adapter.OrderBeanAdapterViewAdapter;
import zucc.edu.cn.ioline.OrderMsgActivity;
import zucc.edu.cn.ioline.R;
import zucc.edu.cn.util.CheckNet;
import zucc.edu.cn.util.ToastUtil;


public class Tab01Fragment_1 extends Fragment implements
		BGARefreshLayout.BGARefreshLayoutDelegate,
		AdapterView.OnItemClickListener  ,BGAOnItemChildClickListener,
		BGAOnItemChildLongClickListener ,ListView.OnScrollListener{
	public static final String ORDER_ITEM = "OrderItem";
	private BGARefreshLayout mRefreshLayout;
	private ListView mListView;
	private OrderBeanAdapterViewAdapter mAdapter;
	private List<OrderBean> mDatas;
	private List<OrderBean> mTemp;
	private List<OrderBean> obl;
	private View view;
	private MyHandler handler;
	private int PagerNumber = 5;  //单页面的条数
	private int itemNubmer = 0; //总tiaohsu
	private int currentPageNumber = 0;
	private int CountPage = 0;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(view != null){
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null) {
				parent.removeView(view);
			}
			return view;
		}
		handler = new MyHandler();
		view = inflater.inflate(R.layout.tab01_1, container, false);
		initView(savedInstanceState);
		setListener();
		processLogic(savedInstanceState);
		return view;
	}

	protected void initView(Bundle savedInstanceState) {
		if(mDatas == null)
			mDatas = new ArrayList<OrderBean>();
		mRefreshLayout = (BGARefreshLayout) view.findViewById(R.id.rl_listview_refresh);
		mListView = (ListView) view.findViewById(R.id.tab01_lv_dan);
	}
	protected void setListener() {
		mRefreshLayout.setDelegate(this);
		mListView.setOnItemClickListener(this);
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
		mAdapter = new OrderBeanAdapterViewAdapter(getContext());
		mAdapter.setOnItemChildClickListener(this);
		mAdapter.setOnItemChildLongClickListener(this);
	}

	protected void processLogic(Bundle savedInstanceState) {
		mRefreshLayout.setRefreshViewHolder(new BGANormalRefreshViewHolder(getContext(), true));
		mRefreshLayout.setCustomHeaderView(null, false);
		mListView.setAdapter(mAdapter);
	}
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {

		}
	}

	@Override
	public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
		if (CheckNet.checkNetworkAvailable(getContext())) {
			// 如果网络可用，则加载网络数据
			new AsyncTask<Void, Void, Void>() {
				String result;
				@Override
				protected Void doInBackground(Void... params) {
					try {
						OrderBean ob = new OrderBean();
						//加载所有信息
						String[] parmas = {ob.getUrl_getOrderAll()};
						CommonRequest mrequest = new CommonRequest(parmas[0],parmas) {
							@Override
							public void convert(HttpURLConnection connection, String[] heads) {
								connection.setRequestProperty("method","getOrderAll");
							}
						};
						result = mrequest.getResult();
						Log.i("InitTask********",result);

						Gson gson = new Gson();
						Type type = new TypeToken<List<OrderBean>>(){}.getType();
						obl = gson.fromJson(result,type);

						mTemp  = obl;
						itemNubmer = mTemp.size();
						mDatas.clear();
//						int val = (itemNubmer)/PagerNumber !=0 ? 5:(itemNubmer);

							for(int i = 0; i < mTemp.size(); i++){
							mDatas.add(mTemp.get(i));
						}
//
//						currentPageNumber = 1;
//						if(itemNubmer%PagerNumber == 0){
//							CountPage = itemNubmer /PagerNumber;
//						}else 	CountPage = itemNubmer /PagerNumber +1;


					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
				}
				@Override
				protected void onPostExecute(Void aVoid) {
					// 加载完毕后在UI线程结束下拉刷新
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

	@Override
	public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
		if(currentPageNumber == CountPage){
			mRefreshLayout.endLoadingMore();
			Toast.makeText(getContext(), "没有更多数据了", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (CheckNet.checkNetworkAvailable(getContext())) {
			// 如果网络可用，则异步加载网络数据，并返回true，显示正在加载更多
			new AsyncTask<Void, Void, Void>() {
				@Override
				protected Void doInBackground(Void... params) {
					try {
//						int val = (itemNubmer-5*currentPageNumber)/5 !=0 ? 5:(itemNubmer-5*currentPageNumber);
//						for(int i = 5*currentPageNumber; i < val+5*currentPageNumber; i++){
//							mDatas.add(mTemp.get(i));
//						}
//						currentPageNumber++;
					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
				}
				@Override
				protected void onPostExecute(Void aVoid) {
					// 加载完毕后在UI线程结束加载更多
					//					mAdapter.setDatas(mDatas);
//					mListView.setSelection((currentPageNumber-1)*5-1);
//					mAdapter.notifyDataSetInvalidated();
//					mAdapter.notifyDataSetChanged();
//					mRefreshLayout.endLoadingMore();
////					mAdapter.addDatas(DataEngine.loadMoreData());
//					BeanDan tmp = new BeanDan("1", "陈琪", "2015", "男", "zucc", "新发布", "内容", 1, 10);
//					mDatas.add(tmp);
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
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Bundle bundle = new Bundle();
		bundle.putSerializable(ORDER_ITEM, mAdapter.getItem(position));
		Intent intent = new Intent();
		intent.putExtra("bundle", bundle);
		intent.setClass(getActivity(), OrderMsgActivity.class);
		startActivity(intent);
	}

	@Override
	public void onItemChildClick(ViewGroup parent, View childView, int position) {
		Bundle bundle = new Bundle();
		bundle.putSerializable(ORDER_ITEM, mAdapter.getItem(position));
		Intent intent = new Intent();
		intent.putExtra("bundle", bundle);
		intent.setClass(getActivity(), OrderMsgActivity.class);
		startActivity(intent);

	}

	@Override
	public boolean onItemChildLongClick(ViewGroup parent, View childView, int position) {
		return false;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

	}


	/**
 * initTask 加载任务信息
 * @author Administrator
 * @time 2016/3/18 19:16
 * method写在header里了
 * params[0] 放url、
 * param[1]+ 后放头信息
 */


	class InitTask extends AsyncTask<String, Integer, String>{
		String result;
		@Override
		protected String doInBackground(String... params) {
			try {
				CommonRequest mrequest = new CommonRequest(params[0],params) {
                    @Override
                    public void convert(HttpURLConnection connection, String[] heads) {
                        connection.setRequestProperty("method","getOrderAll");
                    }
                };

				result = mrequest.getResult();
				Log.i("InitTask********",result);


				Gson gson = new Gson();
				Type type = new TypeToken<List<OrderBean>>(){}.getType();
				obl = gson.fromJson(result,type);
				mDatas = obl;

				Message message = new Message();
				message.what = 2;
				handler.sendMessage(message);


			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

}
	class  MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what){
				case 1:
//					Toast.makeText(getActivity(), "Reflash ListView", Toast.LENGTH_SHORT).show();
//					BeanDan tmp = new BeanDan("1", "陈琪", "2015", "男", "zucc", "新发布", "内容", 1, 10);
					mDatas = obl;
					mAdapter.notifyDataSetChanged();
					break;
				case 2:
					mAdapter.notifyDataSetChanged();
					break;
			}
		}
	}



}

