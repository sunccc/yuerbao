package com.zjkj.yuerbao.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.Toast;

import com.zjkj.yuerbao.R;
import com.zjkj.yuerbao.common.SharedPreferencesHelper;
import com.zjkj.yuerbao.common.preference.AppData;
import com.zjkj.yuerbao.common.service.UpdateService;
import com.zjkj.yuerbao.common.upgrade.MyApplication;
import com.zjkj.yuerbao.common.widget.CustomDialog;
import com.zjkj.yuerbao.main.adapter.FragmentAdapter;
import com.zjkj.yuerbao.main.fragment.BaseFragment;
import com.zjkj.yuerbao.main.fragment.ask.AskFragment;
import com.zjkj.yuerbao.main.fragment.read.ReadFragment;
import com.zjkj.yuerbao.main.indicator.IconTabPageIndicator;

public class MainActivity extends FragmentActivity {

	private static Boolean isExit = false;// 是否退出的标识字段
	private SharedPreferencesHelper sp;
	private ViewPager mViewPager;
	private IconTabPageIndicator mIndicator;
	private Intent intent;
	private MyApplication app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_my);
		sp = new SharedPreferencesHelper(this, AppData.PREFS_NAME);
		intent = new Intent();
		app = new MyApplication(this);
		// 绑定用户状态
		setBindStatus();
		// 初始化视图
		initViews();
		// 获取网络状态
		/*
		 * NetworkState network = new NetworkState(getApplicationContext());
		 * 
		 * if (network.isConn()) {
		 * 
		 * 
		 * // 提示用户是否更新 try { if (!app.isNewVersion()) { alertUpgrade(); } }
		 * catch (Exception e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 * 
		 * } else { // 打开网络设置界面
		 * network.openWirelessSettingActivity(getApplicationContext()); }
		 */
	}

	public void setBindStatus() {

		// 放入数据
		sp.putValue(AppData.ACTIVITY_ClASS, getClass().getName());// 下次启动直接进入此界面

		Toast.makeText(
				this,
				"登录类型:" + sp.getValue("LoginType") + ",昵称:"
						+ sp.getValue("nickname"), Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// exitBy2Click(); // 调用双击退出函数
			exitAlertDialog();
		}
		return false;
	}

	// 带问话框的退出模式
	@SuppressLint("NewApi")
	public void exitAlertDialog()

	{
		/*
		 * final Dialog dialog = new Dialog(this, R.style.dialog);
		 * 
		 * dialog.setContentView(R.layout.exit_dialog);
		 * 
		 * Button btnYes = (Button) dialog.findViewById(R.id.yes); Button btnNo
		 * = (Button) dialog.findViewById(R.id.no);
		 * btnYes.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub System.exit(0);
		 * 
		 * } }); btnNo.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub dialog.dismiss(); } }); // dialog.show();
		 */
		CustomDialog.Builder alert = new CustomDialog.Builder(this);
		alert.setTitle("提示");
		alert.setMessage("您确定要退出吗？");
		alert.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				System.exit(0);
			}
		});
		alert.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		alert.create().show();

	}

	/**
	 * 双击退出函数
	 */
	private void exitBy2Click() {
		Timer tExit = null;
		if (isExit == false) {
			isExit = true; // 准备退出
			Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false; // 取消退出
				}
			}, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

		} else {
			finish();
			System.exit(0);
		}
	}

	private void initViews() {
		mViewPager = (ViewPager) findViewById(R.id.view_pager);
		mIndicator = (IconTabPageIndicator) findViewById(R.id.indicator);
		List<BaseFragment> fragments = initFragments();
		FragmentAdapter adapter = new FragmentAdapter(fragments,
				getSupportFragmentManager());
		mViewPager.setAdapter(adapter);
		mIndicator.setViewPager(mViewPager);
	}

	private List<BaseFragment> initFragments() {
		List<BaseFragment> fragments = new ArrayList<BaseFragment>();
		BaseFragment readFragment = new ReadFragment("跟读",
				R.drawable.tab_read_selector);
		fragments.add(readFragment);

		AskFragment askFragment = new AskFragment("问答",
				R.drawable.tab_answer_selector);
		fragments.add(askFragment);

		BaseFragment registerActivity = new BaseFragment();
		registerActivity.setTitle("购物");
		registerActivity.setIconId(R.drawable.tab_shop_selector);
		fragments.add(registerActivity);

		BaseFragment userFragment = new BaseFragment();
		userFragment.setTitle("我");
		userFragment.setIconId(R.drawable.tab_user_selector);
		fragments.add(userFragment);

		return fragments;
	}

	/**
	 * 
	 * @TODO 发现新版本，提示用户更新
	 * 
	 */
	public void alertUpgrade() {

		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("软件升级");
		alert.setMessage("发现新版本,建议立即更新使用.");
		alert.setPositiveButton("更新", new UpdateBtnOnclickListener());
		alert.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		alert.create().show();
	}

	/**
	 * 
	 * @TODO 点击更新按键的事件处理
	 *
	 */
	class UpdateBtnOnclickListener implements DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			intent.setClass(app.getContext(), UpdateService.class);
			intent.putExtra("app_name", app.getVersionInfo().getApkName());// 指定下载的应用名称
			intent.putExtra("down_url", app.getVersionInfo().getDownloadURL());// 指定下载的应用名称

			startService(intent);
		}

	}
}
