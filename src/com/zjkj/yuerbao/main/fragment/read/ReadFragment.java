package com.zjkj.yuerbao.main.fragment.read;

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
import com.zjkj.yuerbao.main.fragment.BaseFragment;
import com.zjkj.yuerbao.main.listener.ArticleItemClickListener;

public class ReadFragment extends BaseFragment implements
		IXListViewRefreshListener, IXListViewLoadMore {
	private ArticleItemAdapter aiAdapter;
	private ArticleItemClickListener aicListener;
	private List<Article> mDatas = new ArrayList<Article>();
	private XListView mXListView;
	private ArticleItem articleItem;
	private NetworkState netWorkState;
	private int currentPage = 1;
	private static final int LOAD_MORE = 1;
	private static final int LOAD_REFREASH = 2;
	private static final int TIP_ERROR_NO_NETWORK = 3;

	public ReadFragment() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ReadFragment(String title, int iconId) {
		super(title, iconId);
		// TODO Auto-generated constructor stub
	}

	public ReadFragment(String title) {
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
		return inflater.inflate(R.layout.activity_main_fragment_read, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		articleItem = new ArticleItem();
		netWorkState = new NetworkState(getActivity());
		aiAdapter = new ArticleItemAdapter(getActivity(), mDatas);
		/**
		 * 初始化
		 */
		mXListView = (XListView) getView().findViewById(R.id.id_xlistView);
		aicListener = new ArticleItemClickListener(getActivity(), mXListView);
		System.out.println(mXListView);
		mXListView.setAdapter(aiAdapter);
		mXListView.setPullRefreshEnable(this);
		mXListView.setPullLoadEnable(this);
		// mXListView.NotRefreshAtBegin();
		/**
		 * 进来时直接刷新
		 */
		mXListView.setOnItemClickListener(aicListener);
		mXListView.startRefresh();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
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
			aiAdapter.setmDatas(articleItems);

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
			aiAdapter.setmDatas(articleItems);
		}
	}

	/**
	 * 记载数据的异步任务
	 * 
	 * @author gxy
	 * 
	 */
	class LoadDatasTask extends AsyncTask<Integer, Void, Integer> {

		// protected Void doInBackground(Void... params) {
		// List<Article> articleItems = articleItem
		// .getArticleItem(currentPage);
		// mDatas = articleItems;
		//
		// return null;
		// }
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
			switch (result) {
			case TIP_ERROR_NO_NETWORK:
				Toast.makeText(getActivity(), "没有网络连接！", Toast.LENGTH_LONG)
						.show();
				aiAdapter.setmDatas(mDatas);
				aiAdapter.notifyDataSetChanged();
				break;
			default:
				break;
			}
			aiAdapter.notifyDataSetChanged();
			mXListView.stopRefresh();
			mXListView.stopLoadMore();
		}
	}

}
