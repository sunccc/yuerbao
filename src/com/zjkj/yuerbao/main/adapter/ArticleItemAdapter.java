package com.zjkj.yuerbao.main.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.zjkj.yuerbao.R;
import com.zjkj.yuerbao.entity.po.Article;

public class ArticleItemAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<Article> mDatas;

	/**
	 * 使用了github开源的ImageLoad进行了数据加载
	 */
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;

	public ArticleItemAdapter(Context context, List<Article> datas) {
		this.mDatas = datas;
		this.mInflater = LayoutInflater.from(context);

		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.images)
				.showImageForEmptyUri(R.drawable.images)
				.showImageOnFail(R.drawable.images).cacheInMemory()
				.cacheOnDisc().displayer(new RoundedBitmapDisplayer(20))
				.displayer(new FadeInBitmapDisplayer(300)).build();
	}

	public void addAll(List<Article> mDatas) {
		this.mDatas.addAll(mDatas);
	}

	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public Object getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.articles_item, null);
			holder = new ViewHolder();

			holder.mContent = (TextView) convertView
					.findViewById(R.id.id_content);
			holder.mTitle = (TextView) convertView.findViewById(R.id.id_title);
			holder.mImg = (ImageView) convertView.findViewById(R.id.id_newsImg);
			holder.mDate = (TextView) convertView.findViewById(R.id.id_date);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Article article = mDatas.get(position);

		// 获取数据
		holder.mTitle.setText(article.getTitle());
		holder.mContent.setText("文章内容");
		holder.mDate.setText(article.getUpdateTime());
		if (article.getImgLink() != null) {
			holder.mImg.setVisibility(View.VISIBLE);
			imageLoader
					.displayImage(article.getImgLink(), holder.mImg, options);
		} else {
			imageLoader
					.displayImage(
							"http://img.yuerbao.cc/image/20150307/1425717477319024890.jpg",
							holder.mImg, options);
			// holder.mImg.setVisibility(View.GONE);
		}

		return convertView;
	}

	public List<Article> getmDatas() {
		return mDatas;
	}

	public void setmDatas(List<Article> mDatas) {
		this.mDatas = mDatas;
	}

	private final class ViewHolder {
		TextView mTitle;
		TextView mContent;
		ImageView mImg;
		TextView mDate;
	}

}
