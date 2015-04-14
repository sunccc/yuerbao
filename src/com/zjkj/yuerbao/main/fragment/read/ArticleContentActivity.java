package com.zjkj.yuerbao.main.fragment.read;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.zjkj.yuerbao.R;
import com.zjkj.yuerbao.common.widget.IXListViewLoadMore;
import com.zjkj.yuerbao.common.widget.XListView;
import com.zjkj.yuerbao.entity.bo.ArticleContent;
import com.zjkj.yuerbao.entity.bo.ArticleContent.ArticleType;
import com.zjkj.yuerbao.entity.bo.ArticleItem;
import com.zjkj.yuerbao.entity.po.Article;
import com.zjkj.yuerbao.main.adapter.ArticleContentAdapter;

public class ArticleContentActivity extends Activity implements
		IXListViewLoadMore {

	private XListView mListView;
	private Article articleInfo;
	private ProgressBar mProgressBar;
	private ArticleContentAdapter mAdapter;
	private List<ArticleContent> mDatas;
	private ArticleItem articleItem;
	private Integer id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_article_content);

		Bundle bundle = getIntent().getExtras();
		id = bundle.getInt("articleId");
		articleItem = new ArticleItem();
		mListView = (XListView) findViewById(R.id.id_listview);
		mProgressBar = (ProgressBar) findViewById(R.id.id_articlesContentPro);
		mAdapter = new ArticleContentAdapter(this);

		mListView.setAdapter(mAdapter);
		mListView.disablePullRefreash();
		mListView.disablePullLoad();
		mProgressBar.setVisibility(View.VISIBLE);
		new LoadDataTask().execute();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub

	}

	class LoadDataTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			try {
				mDatas = articleItem.getArticleDetail(id);
			} catch (Exception e) {
				Looper.prepare();
				Toast.makeText(getApplicationContext(), e.getMessage(),
						Toast.LENGTH_LONG).show();
				Looper.loop();
			}

			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			if (mDatas == null)
				return;
			mAdapter.addList(mDatas);
			mAdapter.notifyDataSetChanged();
			mProgressBar.setVisibility(View.GONE);
		}

	}

	/**
	 * 点击返回按钮
	 * 
	 * @param view
	 */
	public void back(View view) {
		finish();
	}

	public void share(View view) {
		Bundle extras = new Bundle();
		for (ArticleContent articleContent : mDatas) {

			switch (articleContent.getArticleType()) {
			case ArticleType.TITLE:
				extras.putString("title", articleContent.getTitle());
				break;
			case ArticleType.LASTUPDATE:
				extras.putString("lastUpdate", articleContent.getLastUpdate());
				break;
			case ArticleType.IMG:
				extras.putString("imgLinkUrl", articleContent.getImgLink());
				break;
			case ArticleType.CONTENT:
				extras.putString("content", articleContent.getContent());
				break;

			}

		}
		Intent intent = new Intent();
		intent.putExtra("articleContent", extras);
		intent.setClass(this, ArticleContentShareActivity.class);// 转入跳转的activity
		startActivity(intent);
	}

}
