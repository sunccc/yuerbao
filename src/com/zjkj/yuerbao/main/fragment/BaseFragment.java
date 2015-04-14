package com.zjkj.yuerbao.main.fragment;

import android.support.v4.app.Fragment;

/**
 * User: Geek_Soledad(msdx.android@qq.com) Date: 2014-08-27 Time: 09:01 FIXME
 */
public class BaseFragment extends Fragment {
	private String title;
	private int iconId;

	public BaseFragment() {
		super();
		// TODO Auto-generated constructor stub
	}
	public BaseFragment(String title) {
		super();
		this.title = title;
	}

	public BaseFragment(String title, int iconId) {
		super();
		this.title = title;
		this.iconId = iconId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getIconId() {
		return iconId;
	}

	public void setIconId(int iconId) {
		this.iconId = iconId;
	}

}