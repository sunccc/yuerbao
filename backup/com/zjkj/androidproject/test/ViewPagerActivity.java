package com.zjkj.androidproject.test;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

public class ViewPagerActivity extends Activity {

	private ViewPager mViewPager;// ����ViewPager����
	private PagerTitleStrip mPagerTitleStrip;// ������������
	private ImageView mPageImg;// ����ͼƬ
	private int currIndex = 0;// ��ǰҳ��
	private ImageView mPage0, mPage1, mPage2;// ��������ͼƬ����

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_viewpager);
//		mViewPager = (ViewPager) findViewById(R.id.viewpager);
//		MyOnPageChangeListener myOnPageChangeListener = new MyOnPageChangeListener();
//		mViewPager.setOnPageChangeListener(myOnPageChangeListener);
//		mPagerTitleStrip = (PagerTitleStrip) findViewById(R.id.pagertitle);

//		mPage0 = (ImageView) findViewById(R.id.page0);
//		mPage1 = (ImageView) findViewById(R.id.page1);
//		mPage2 = (ImageView) findViewById(R.id.page2);
		final ArrayList<View> views = new ArrayList<View>();
		// ��Ҫ��ҳ��ʾ��Viewװ��������
		LayoutInflater mLi = LayoutInflater.from(this);
//		View view1 = mLi.inflate(R.layout.activity_main, null);
//		View view2 = mLi.inflate(R.layout.activity_bottom, null);
//		View view3 = mLi.inflate(R.layout.activity_main, null);

		// ÿ��ҳ���view����
//		views.add(view1);
//		views.add(view2);
//		views.add(view3);
		// views.add(view4);
		// views.add(view5);
		// views.add(view6);
		// views.add(view7);
		// views.add(view8);
		// views.add(view9);
		// ÿһ��Ҳû�ñ���
		final ArrayList<String> titles = new ArrayList<String>();
		titles.add("��");
		titles.add("��");
		titles.add("��");
		// titles.add("��");
		// titles.add("��");
		// titles.add("��");
		// titles.add("��");
		// titles.add("��");
		// titles.add("��");

		// ���ViewPager��������������������д����
		PagerAdapter mPagerAdapter = new PagerAdapter() {

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return views.size();
			}

			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewPager) container).removeView(views.get(position));
			}

			@Override
			public CharSequence getPageTitle(int position) {
				System.out.println(position);
				return titles.get(position);
			}

			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager) container).addView(views.get(position));
				return views.get(position);
			}
		};

		mViewPager.setAdapter(mPagerAdapter);// ��ListView�÷���ͬ��������д��Adapter��������ʵ����ViewPager�Ļ���Ч����

	}

	public void startbutton(View v)  {
		Intent intent = new Intent();
		intent.setClass(ViewPagerActivity.this, MainActivity.class);// ���뵽����Ч����Activity
		startActivity(intent);
		this.finish();// ������Activity
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) { // ���/����/���η��ؼ�
			// do something
			return false;
		} else {
			// ���������û�з��ؽ����
			return super.onKeyDown(keyCode, event);
		}
	}
/*
	public class MyOnPageChangeListener implements OnPageChangeListener {

		public void onPageSelected(int arg0) {// ����arg0Ϊѡ�е�View

			Animation animation = null;// ������������
			switch (arg0) {
			case 0: // ҳ��һ
				mPage0.setImageDrawable(getResources().getDrawable(
						R.drawable.page_now1));// �����һ������ҳ�棬СԲ��Ϊѡ��״̬����һ��ҳ���СԲ����δѡ��״̬��
				mPage1.setImageDrawable(getResources().getDrawable(
						R.drawable.page));
				if (currIndex == arg0 + 1) {
					animation = new TranslateAnimation(arg0 + 1, arg0, 0, 0);// Բ���ƶ�Ч���������ӵ�ǰView�ƶ�����һ��View
				}
				break;
			case 1: // ҳ���
				mPage1.setImageDrawable(getResources().getDrawable(
						R.drawable.page_now1));// ��ǰView
				mPage0.setImageDrawable(getResources().getDrawable(
						R.drawable.page));// ��һ��View
				mPage2.setImageDrawable(getResources().getDrawable(
						R.drawable.page));// ��һ��View
				if (currIndex == arg0 - 1) {// �����������һ��View
					animation = new TranslateAnimation(arg0 - 1, arg0, 0, 0); // Բ���ƶ�Ч���������ӵ�ǰView�ƶ�����һ��View

				} else if (currIndex == arg0 + 1) {// Բ���ƶ�Ч���������ӵ�ǰView�ƶ�����һ��View����ͬ��

					animation = new TranslateAnimation(arg0 + 1, arg0, 0, 0);
				}
				break;
			case 2: // ҳ����
				mPage2.setImageDrawable(getResources().getDrawable(
						R.drawable.page_now1));
				mPage1.setImageDrawable(getResources().getDrawable(
						R.drawable.page));
				if (currIndex == arg0 - 1) {
					animation = new TranslateAnimation(arg0 - 1, arg0, 0, 0);
				} else if (currIndex == arg0 + 1) {
					animation = new TranslateAnimation(arg0 + 1, arg0, 0, 0);
				}
				break;
			}
			currIndex = arg0;// ���õ�ǰView
			animation.setFillAfter(true);// True:����ͼƬͣ�ڶ�������λ��
			animation.setDuration(300);// ���ö�������ʱ��
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

	}*/
}