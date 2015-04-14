package com.zjkj.yuerbao.start;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zjkj.yuerbao.R;
import com.zjkj.yuerbao.common.preference.AppData;
import com.zjkj.yuerbao.common.service.UpdateService;
import com.zjkj.yuerbao.start.bind.BaobaoDate;
import com.zjkj.yuerbao.start.bind.BeiyunDate;
import com.zjkj.yuerbao.start.bind.YuchanDate;

/**
 * 
 * @author Gxy
 * 
 * @TODO 状态绑定界面
 *
 */
public class BindStatus extends Activity {

	private Intent intent;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bind_status);

		intent = this.getIntent();
		
		//获取网络状态
//		NetworkState network = new NetworkState(getApplicationContext());
//
//		if (network.isConn()) {
//
//			if (!intent.getBooleanExtra(PreferenceData.IS_NEWVERSION, true)) {
//
//				//提示用户是否更新
//				alertUpgrade();
//			}
//
//		} else {
//			//打开网络设置界面
//			network.openWirelessSettingActivity(getApplicationContext());
//		}
	}

	/**
	 * 
	 * @param v
	 *            预产期ImageView
	 * 
	 *            打开绑定预产期的界面
	 */

	public void openHuayunActivity(View v) {
		intent.setClass(BindStatus.this, YuchanDate.class);
		startActivity(intent);
		finish();
	}

	/**
	 * 
	 * @param v
	 *            备孕期ImageView
	 * 
	 *            打开绑定备孕期的界面
	 */

	public void openBeiyunActivity(View v) {
		intent.setClass(BindStatus.this, BeiyunDate.class);
		startActivity(intent);
		finish();
	}

	/**
	 * 
	 * @param v
	 *            宝宝生月ImageView
	 * 
	 * @TODO           打开绑定宝宝生日的界面
	 */

	public void openBaobaoActivity(View v) {
		intent.setClass(BindStatus.this, BaobaoDate.class);
		startActivity(intent);
		finish();
	}
	/**
	 * 
	 * @TODO  发现新版本，提示用户更新
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
			intent.setClass(getApplicationContext(), UpdateService.class);
			intent.putExtra("app_name",
					intent.getStringExtra(AppData.APP_NAME));// 指定下载的应用名称
			intent.putExtra("down_url",
					intent.getStringExtra(AppData.DOWNLOAD_URL));// 指定下载应用的地址

			startService(intent);
		}

	}
}
