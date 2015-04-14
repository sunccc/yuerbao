package com.zjkj.yuerbao.common.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.provider.Settings;

/**
 * 
 * @author Gxy
 *
 *         ��ȡ��������״̬��
 */
public class NetworkState {
	private Context context;
	private ConnectivityManager connMan;
	private NetworkInfo mobile;
	private NetworkInfo wifi;

	// TODO Auto-generated constructor stub
	public NetworkState(Context context) {
		this.context = context;
		connMan = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		this.wifi = connMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		this.mobile = connMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
	}

	/*
	 * @TODO �Ƿ������������
	 */
	public boolean isNetworkConnected() {
		if (context != null) {
			NetworkInfo mNetworkInfo = connMan.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/*
	 * @TODO ��ȡ������������
	 */

	public String getNetworkType() {
		String networkType = null;
		if (mobile.getState() == State.CONNECTED
				|| mobile.getState() == State.CONNECTING)
			return networkType = "�ƶ�����";
		else if (wifi.getState() == State.CONNECTED
				|| wifi.getState() == State.CONNECTING)
			return networkType = "wifi����";
		return networkType;
	}

	public boolean isMobileNetwork() {
		return mobile.getState() == State.CONNECTED
				|| mobile.getState() == State.CONNECTING;
	}

	public boolean isWifiNetwork() {
		return wifi.getState() == State.CONNECTED
				|| wifi.getState() == State.CONNECTING;
	}

	/*
	 * @TODO ���������������ý���
	 */
	public void openWirelessSettingActivity(final Context context) {
		// �����°汾����ʾ�û�����
		AlertDialog.Builder alert = new AlertDialog.Builder(context);

		alert.setTitle("����״̬");
		alert.setMessage("��ǰ����������");
		alert.setPositiveButton("����", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

				context.startActivity(new Intent(
						Settings.ACTION_WIRELESS_SETTINGS));
			}
		});
		alert.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		alert.create().show();
	}
}
