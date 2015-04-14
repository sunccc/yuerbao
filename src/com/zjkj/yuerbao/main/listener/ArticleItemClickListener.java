package com.zjkj.yuerbao.main.listener;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.zjkj.yuerbao.entity.po.Article;
import com.zjkj.yuerbao.main.fragment.read.ArticleContentActivity;

public class ArticleItemClickListener implements OnItemClickListener {

	private Activity activity;
	private ListView listView;

	public ArticleItemClickListener(Activity activity, ListView listView) {
		super();
		this.activity = activity;
		this.listView = listView;
	}

	public ArticleItemClickListener() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

		Article article = (Article) listView.getAdapter().getItem(position);
		if (article.getId() != null) {
			Intent intent = new Intent();
			intent.putExtra("articleId", article.getId());
			intent.setClass(activity, ArticleContentActivity.class);// 转入跳转的activity
			activity.startActivity(intent);
		} else {
			Toast.makeText(activity, "文章已过期", Toast.LENGTH_LONG).show();
		}
	}
}
