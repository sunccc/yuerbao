package com.zjkj.yuerbao.main.fragment.ask;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zjkj.yuerbao.R;
import com.zjkj.yuerbao.common.util.NetworkState;
import com.zjkj.yuerbao.common.widget.IXListViewLoadMore;
import com.zjkj.yuerbao.common.widget.IXListViewRefreshListener;
import com.zjkj.yuerbao.common.widget.XListView;
import com.zjkj.yuerbao.entity.bo.ArticleItem;
import com.zjkj.yuerbao.entity.po.Article;
import com.zjkj.yuerbao.main.adapter.ArticleItemAdapter;
import com.zjkj.yuerbao.main.adapter.CircleItemAdapter;
import com.zjkj.yuerbao.main.fragment.BaseFragment;

public class CircleFragment extends BaseFragment implements
		IXListViewRefreshListener, IXListViewLoadMore {
	private NetworkState netWorkState;
	private XListView mXlistView;
	private ArticleItem articleItem;
	private CircleItemAdapter circleItemAdapter;
	private List<Article> mDatas = new ArrayList<Article>();
	private int currentPage = 1;
	private static final int LOAD_MORE = 1;
	private static final int LOAD_REFREASH = 2;
	private static final int TIP_ERROR_NO_NETWORK = 3;

	public CircleFragment() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CircleFragment(String title, int iconId) {
		super(title, iconId);
		// TODO Auto-generated constructor stub
	}

	public CircleFragment(String title) {
		super(title);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.activity_main_fragment_ask_circles,
				null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		netWorkState = new NetworkState(getActivity());
		articleItem = new ArticleItem();
		mXlistView = (XListView) getView().findViewById(R.id.circle_xListView);
		circleItemAdapter = new CircleItemAdapter(getActivity(), mDatas);
		mXlistView.setAdapter(circleItemAdapter);
		mXlistView.setPullRefreshEnable(this);
		mXlistView.setPullLoadEnable(this);
//		mXlistView.startRefresh();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		new LoadDatasTask().execute(LOAD_MORE);
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		new LoadDatasTask().execute(LOAD_REFREASH);

	}

	/**
	 * 下拉刷新数据
	 */
	public Integer refreashData() {

		if (netWorkState.isNetworkConnected()) {
			// 获取最新数据
			List<Article> articleItems = articleItem
					.getArticleItem(currentPage);
			circleItemAdapter.setmDatas(articleItems);

			return -1;
		} else {
			return TIP_ERROR_NO_NETWORK;
		}

	}

	/**
	 * 
	 * TODO 会根据当前网络情况，判断是从数据库加载还是从网络继续获取 void
	 */
	public void loadMoreData() {
		// 当前数据是从网络获取的
		if (netWorkState.isNetworkConnected()) {
			currentPage += 1;
			List<Article> articleItems = articleItem
					.getArticleItem(currentPage);
			circleItemAdapter.setmDatas(articleItems);
		}
	}

	class LoadDatasTask extends AsyncTask<Integer, Void, Integer> {

		@Override
		protected Integer doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			switch (params[0]) {
			case LOAD_MORE:
				loadMoreData();
				break;
			case LOAD_REFREASH:
				return refreashData();
			}
			return -1;
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			switch (result) {
			case TIP_ERROR_NO_NETWORK:
				Toast.makeText(getActivity(), "没有网络连接！", Toast.LENGTH_LONG)
						.show();
				circleItemAdapter.setmDatas(mDatas);
				circleItemAdapter.notifyDataSetChanged();
				break;
			default:
				break;
			}
			circleItemAdapter.notifyDataSetChanged();
			mXlistView.stopRefresh();
			mXlistView.stopLoadMore();
		}
	}
}