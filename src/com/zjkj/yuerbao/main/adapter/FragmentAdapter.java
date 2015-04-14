package com.zjkj.yuerbao.main.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.TimeUtils;

import com.zjkj.yuerbao.main.fragment.BaseFragment;

public class FragmentAdapter extends FragmentPagerAdapter implements
		IconPagerAdapter {
	private List<BaseFragment> fragments;

	public FragmentAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	public FragmentAdapter(List<BaseFragment> fragments, FragmentManager fm) {
		super(fm);
		this.fragments = fragments;
		// TODO Auto-generated constructor stub
	}

	@Override
	public CharSequence getPageTitle(int position) {
		// TODO Auto-generated method stub
		// return title[position];
		return this.fragments.get(position).getTitle();
	}

	@Override
	public int getIconResId(int index) {
		// TODO Auto-generated method stub
		return this.fragments.get(index).getIconId();
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return this.fragments.get(arg0);
	}

	public List<BaseFragment> getFragments() {
		return fragments;
	}

	public void setFragments(List<BaseFragment> fragments) {
		this.fragments = fragments;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.fragments.size();
	}

}
