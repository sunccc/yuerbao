package com.zjkj.yuerbao.main.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.zjkj.yuerbao.R;
import com.zjkj.yuerbao.entity.bo.ArticleContent;
import com.zjkj.yuerbao.entity.bo.ArticleContent.ArticleType;
import com.zjkj.yuerbao.entity.po.Article;

public class ArticleContentAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<ArticleContent> mDatas = new ArrayList<ArticleContent>();

	private ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;

	public ArticleContentAdapter(Context context) {
		mInflater = LayoutInflater.from(context);

		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.images)
				.showImageForEmptyUri(R.drawable.images)
				.showImageOnFail(R.drawable.images).cacheInMemory()
				.cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new FadeInBitmapDisplayer(300)).build();
	}

	public void addList(List<ArticleContent> datas) {
		mDatas.addAll(datas);
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
	public int getItemViewType(int position) {
		switch (mDatas.get(position).getArticleType()) {
		case ArticleType.TITLE:
			return 1;
		case ArticleType.LASTUPDATE:
			return 2;
		case ArticleType.IMG:
			return 3;
		case ArticleType.CONTENT:
			return 4;
		}
		return -1;
	}

	/*
	 * @Override public int getViewTypeCount() { return 3; }
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ArticleContent article = mDatas.get(position); // 获取当前项数据
		ViewHolder holder = null;
		if (null == convertView) {
			holder = new ViewHolder();
			switch (article.getArticleType()) {
			case ArticleType.TITLE:
				convertView = mInflater.inflate(
						R.layout.article_content_title_item, null);
				holder.mTextView = (TextView) convertView
						.findViewById(R.id.text);
				break;
			case ArticleType.LASTUPDATE:
				convertView = mInflater.inflate(
						R.layout.article_content_summary_item, null);
				holder.mTextView = (TextView) convertView
						.findViewById(R.id.text);
				break;
			case ArticleType.IMG:
				convertView = mInflater.inflate(
						R.layout.article_content_img_item, null);
				holder.mImageView = (ImageView) convertView
						.findViewById(R.id.imageView);
				break;
			case ArticleType.CONTENT:
				convertView = mInflater.inflate(R.layout.article_content_item,
						null);
				holder.mTextView = (TextView) convertView
						.findViewById(R.id.text);
				break;
			/*
			 * case ArticleType.BOLD_TITLE: convertView = mInflater.inflate(
			 * R.layout.article_content_bold_title_item, null); holder.mTextView
			 * = (TextView) convertView .findViewById(R.id.text); break;
			 */
			}
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (null != article) {
			switch (article.getArticleType()) {
			case ArticleType.TITLE:
				holder.mTextView.setText(article.getTitle());
				break;
			case ArticleType.LASTUPDATE:
				holder.mTextView.setText(article.getLastUpdate().toString());
				break;
			case ArticleType.IMG:
				imageLoader.displayImage(article.getImgLink(),
						holder.mImageView, options);
				break;
			case ArticleType.CONTENT:
				holder.mTextView.setText("\u3000\u3000"+article.getContent());
				break;
			/*
			 * case ArticleType.BOLD_TITLE:
			 * holder.mTextView.setText("\u3000\u3000" +
			 * Html.fromHtml(article.getContent()));
			 */
			default:

				// holder.mTextView.setText(Html.fromHtml(item.getContent(),
				// null, new MyTagHandler()));
				// holder.content.setText(Html.fromHtml("<ul><bold>加粗</bold>sdfsdf<ul>",
				// null, new MyTagHandler()));
				break;
			}
		}
		return convertView;
	}

	private final class ViewHolder {
		TextView mTextView;
		ImageView mImageView;
	}
}
