package com.zjkj.androidproject.test;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zjkj.yuerbao.R;
import com.zjkj.yuerbao.main.adapter.FragmentAdapter;
import com.zjkj.yuerbao.main.fragment.BaseFragment;
import com.zjkj.yuerbao.main.fragment.read.ReadFragment;
import com.zjkj.yuerbao.main.indicator.TitlePageIndicator;
import com.zjkj.yuerbao.main.indicator.TitlePageIndicator.IndicatorStyle;

public class AskFragment extends BaseFragment {
	FragmentAdapter mAdapter;
	ViewPager mPager;
	TitlePageIndicator mIndicator;

	public AskFragment() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AskFragment(String title, int iconId) {
		super(title, iconId);
		// TODO Auto-generated constructor stub
	}

	public AskFragment(String title) {
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
		return inflater.inflate(R.layout.simple_titles, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		List<BaseFragment> fragments = initFragment();
		// mAdapter = new FragmentAdapter(fragments, getFragmentManager());
		mAdapter = new FragmentAdapter(fragments, getFragmentManager());
		mPager = (ViewPager) getView().findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);
		final float density = getResources().getDisplayMetrics().density;

		mIndicator = (TitlePageIndicator) getView()
				.findViewById(R.id.indicator);
		mIndicator.setViewPager(mPager);
		 mIndicator.setFooterColor(0xE3007B00);
		mIndicator.setTextColor(0xAA000000);
		mIndicator.setSelectedColor(0xFF000000);
		mIndicator.setFooterColor(0xFFAA2222);
		mIndicator.setFooterLineHeight(1 * density); // 1dp
		mIndicator.setFooterIndicatorHeight(3 * density); // 3dp
		mIndicator.setSelectedBold(true);
		mIndicator.setFooterIndicatorStyle(IndicatorStyle.Underline);
	}

	private List<BaseFragment> initFragment() {
		// TODO Auto-generated method stub
		List<BaseFragment> fragments = new ArrayList<BaseFragment>();
//		CircleFragment qz = new CircleFragment("圈子");
//		ReadFragment qz=new ReadFragment("圈子");
		BaseFragment jx = new BaseFragment("精选");
//		fragments.add(qz);
		fragments.add(jx);
		return fragments;
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}


}
