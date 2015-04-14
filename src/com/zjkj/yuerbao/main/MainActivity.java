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

	private static Boolean isExit = false;// �Ƿ��˳��ı�ʶ�ֶ�
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
		// ���û�״̬
		setBindStatus();
		// ��ʼ����ͼ
		initViews();
		// ��ȡ����״̬
		/*
		 * NetworkState network = new NetworkState(getApplicationContext());
		 * 
		 * if (network.isConn()) {
		 * 
		 * 
		 * // ��ʾ�û��Ƿ���� try { if (!app.isNewVersion()) { alertUpgrade(); } }
		 * catch (Exception e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 * 
		 * } else { // ���������ý���
		 * network.openWirelessSettingActivity(getApplicationContext()); }
		 */
	}

	public void setBindStatus() {

		// ��������
		sp.putValue(AppData.ACTIVITY_ClASS, getClass().getName());// �´�����ֱ�ӽ���˽���

		Toast.makeText(
				this,
				"��¼����:" + sp.getValue("LoginType") + ",�ǳ�:"
						+ sp.getValue("nickname"), Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// exitBy2Click(); // ����˫���˳�����
			exitAlertDialog();
		}
		return false;
	}

	// ���ʻ�����˳�ģʽ
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
		alert.setTitle("��ʾ");
		alert.setMessage("��ȷ��Ҫ�˳���");
		alert.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				System.exit(0);
			}
		});
		alert.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		alert.create().show();

	}

	/**
	 * ˫���˳�����
	 */
	private void exitBy2Click() {
		Timer tExit = null;
		if (isExit == false) {
			isExit = true; // ׼���˳�
			Toast.makeText(this, "�ٰ�һ���˳�����", Toast.LENGTH_SHORT).show();
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false; // ȡ���˳�
				}
			}, 2000); // ���2������û�а��·��ؼ�����������ʱ��ȡ�����ղ�ִ�е�����

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
		BaseFragment readFragment = new ReadFragment("����",
				R.drawable.tab_read_selector);
		fragments.add(readFragment);

		AskFragment askFragment = new AskFragment("�ʴ�",
				R.drawable.tab_answer_selector);
		fragments.add(askFragment);

		BaseFragment registerActivity = new BaseFragment();
		registerActivity.setTitle("����");
		registerActivity.setIconId(R.drawable.tab_shop_selector);
		fragments.add(registerActivity);

		BaseFragment userFragment = new BaseFragment();
		userFragment.setTitle("��");
		userFragment.setIconId(R.drawable.tab_user_selector);
		fragments.add(userFragment);

		return fragments;
	}

	/**
	 * 
	 * @TODO �����°汾����ʾ�û�����
	 * 
	 */
	public void alertUpgrade() {

		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("�������");
		alert.setMessage("�����°汾,������������ʹ��.");
		alert.setPositiveButton("����", new UpdateBtnOnclickListener());
		alert.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		alert.create().show();
	}

	/**
	 * 
	 * @TODO ������°������¼�����
	 *
	 */
	class UpdateBtnOnclickListener implements DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			intent.setClass(app.getContext(), UpdateService.class);
			intent.putExtra("app_name", app.getVersionInfo().getApkName());// ָ�����ص�Ӧ������
			intent.putExtra("down_url", app.getVersionInfo().getDownloadURL());// ָ�����ص�Ӧ������

			startService(intent);
		}

	}
}
