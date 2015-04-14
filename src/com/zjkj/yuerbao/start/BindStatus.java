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
 * @TODO ״̬�󶨽���
 *
 */
public class BindStatus extends Activity {

	private Intent intent;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bind_status);

		intent = this.getIntent();
		
		//��ȡ����״̬
//		NetworkState network = new NetworkState(getApplicationContext());
//
//		if (network.isConn()) {
//
//			if (!intent.getBooleanExtra(PreferenceData.IS_NEWVERSION, true)) {
//
//				//��ʾ�û��Ƿ����
//				alertUpgrade();
//			}
//
//		} else {
//			//���������ý���
//			network.openWirelessSettingActivity(getApplicationContext());
//		}
	}

	/**
	 * 
	 * @param v
	 *            Ԥ����ImageView
	 * 
	 *            �򿪰�Ԥ���ڵĽ���
	 */

	public void openHuayunActivity(View v) {
		intent.setClass(BindStatus.this, YuchanDate.class);
		startActivity(intent);
		finish();
	}

	/**
	 * 
	 * @param v
	 *            ������ImageView
	 * 
	 *            �򿪰󶨱����ڵĽ���
	 */

	public void openBeiyunActivity(View v) {
		intent.setClass(BindStatus.this, BeiyunDate.class);
		startActivity(intent);
		finish();
	}

	/**
	 * 
	 * @param v
	 *            ��������ImageView
	 * 
	 * @TODO           �򿪰󶨱������յĽ���
	 */

	public void openBaobaoActivity(View v) {
		intent.setClass(BindStatus.this, BaobaoDate.class);
		startActivity(intent);
		finish();
	}
	/**
	 * 
	 * @TODO  �����°汾����ʾ�û�����
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
			intent.setClass(getApplicationContext(), UpdateService.class);
			intent.putExtra("app_name",
					intent.getStringExtra(AppData.APP_NAME));// ָ�����ص�Ӧ������
			intent.putExtra("down_url",
					intent.getStringExtra(AppData.DOWNLOAD_URL));// ָ������Ӧ�õĵ�ַ

			startService(intent);
		}

	}
}
