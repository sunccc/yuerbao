package com.zjkj.yuerbao.main.adapter;

import java.util.List;

import org.jsoup.helper.StringUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.zjkj.yuerbao.R;
import com.zjkj.yuerbao.entity.po.Article;

public class CircleItemAdapter extends BaseAdapter {
	private LayoutInflater mLayoutInflater;
	private List<Article> mDatas;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;

	public CircleItemAdapter(Context context, List mDatas) {
		super();
		this.mLayoutInflater = LayoutInflater.from(context);
		this.mDatas = mDatas;
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

	public List<Article> getmDatas() {
		return mDatas;
	}

	public void setmDatas(List<Article> mDatas) {
		this.mDatas = mDatas;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDatas.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder layout = null;
		if (convertView == null) {
			convertView = this.mLayoutInflater.inflate(R.layout.circles_item,
					null);
			layout = new ViewHolder();
			layout.mTitle = (TextView) convertView.findViewById(R.id.id_title);
			layout.mContent = (TextView) convertView
					.findViewById(R.id.id_content);
			layout.mDate = (TextView) convertView.findViewById(R.id.id_date);
			layout.mImg = (ImageView) convertView.findViewById(R.id.id_newsImg);
			convertView.setTag(layout);
		} else {
			layout = (ViewHolder) convertView.getTag();
		}
		Article article = mDatas.get(position);
		layout.mTitle.setText(article.getTitle());
		layout.mContent.setText(article.getContent());
		layout.mDate.setText(article.getUpdateTime());
		if (StringUtil.isBlank(article.getImgLink())) {
			layout.mImg.setVisibility(View.VISIBLE);
			imageLoader
					.displayImage("http://img.yuerbao.cc/image/20150307/1425717477319024890.jpg", layout.mImg, options);

		} else {
			imageLoader
					.displayImage(
							"http://img.yuerbao.cc/image/20150307/1425717477319024890.jpg",
							layout.mImg, options);
		}
		return convertView;
	}

	private final class ViewHolder {
		TextView mTitle;
		TextView mContent;
		ImageView mImg;
		TextView mDate;

	}

}
