package com.zjkj.yuerbao.common.upgrade;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;

import com.zjkj.yuerbao.common.service.UpdateService;
import com.zjkj.yuerbao.common.util.VersionXmlPullPaser;
import com.zjkj.yuerbao.common.util.YuerbaoProperties;

/***
 * 
 * @author Gxy
 * 
 * @TODO �汾��Ϣ������
 * 
 */
public class MyApplication {

	private int localVersion;// ���ذ�װ�汾

	private VersionInfo versionInfo;// �汾��Ϣ��

	private Context context;

	private YuerbaoProperties yp;

	/**
	 * @return �Ƿ����°汾
	 */
	public boolean isNewVersion() throws Exception {
		System.out.println(this.getLocalVersion()+"+"+this.versionInfo.getVersionCode()+"="+this.getLocalVersion()+this.versionInfo.getVersionCode());
		return this.getLocalVersion() >= this.versionInfo.getVersionCode();
	}

	public VersionInfo getVersionInfo() {

		// InputStream in = null;
		// AssetManager manager = applicaiton.getAssets();
		// try {
		// in = manager.open("version.xml");
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// System.out.println("no found file");
		// e.printStackTrace();
		// }

		return versionInfo;
	}

	public void setVersionInfo(VersionInfo versionInfo) {
		this.versionInfo = versionInfo;
	}

	public MyApplication(Context context) {
		super();
		this.context = context;
		yp = YuerbaoProperties.getInstance();
		Thread thread = new Thread(new Runnable() {

			// TODO ��ȡԶ�̷������汾��Ϣ
			@Override
			public void run() {

				getVersionInfoByHttpConn();
			};
		});
		thread.start();
		// ��ͣ5���ӣ���ȡ����˵İ汾��Ϣ
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public void getVersionInfoByHttpConn() {
		InputStream in = null;
		try {
			URL url1 = new URL(yp.getLoadVersionInfoXmlUrl());
			HttpURLConnection conn = (HttpURLConnection) url1.openConnection();
			conn.setConnectTimeout(50000);
			conn.setReadTimeout(50000);

			in = conn.getInputStream();
			// ���������˵İ汾��Ϣ�ļ�����ΪVersionInfo��
			setVersionInfo(VersionXmlPullPaser.readXML(in));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @return ���ذ汾��
	 */
	public int getLocalVersion() {

		try {
			localVersion = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionCode;
			return localVersion;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return localVersion;
	}

	public void setLocalVersion(int localVersion) {
		this.localVersion = localVersion;
	}

	/**
	 * ����Ƿ���°汾
	 * 
	 * @throws Exception
	 */
	public void checkVersion(Context context) throws Exception {

		// ��ȡ�汾��Ϣ��

		if (!isNewVersion()) {

			// �����°汾����ʾ�û�����
			AlertDialog.Builder alert = new AlertDialog.Builder(context);

			alert.setTitle("�������");
			alert.setMessage("�����°汾,������������ʹ��.");
			alert.setPositiveButton("����", new UpdateBtnOnclickListener());
			alert.setNegativeButton("ȡ��",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			alert.create().show();

		}
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
			Intent updateIntent = new Intent(getContext(), UpdateService.class);
			updateIntent.putExtra("app_name", getVersionInfo().getApkName());// ָ�����ص�Ӧ������
			updateIntent
					.putExtra("down_url", getVersionInfo().getDownloadURL());// ָ�����ص�Ӧ������

			getContext().startService(updateIntent);
		}

	}
}
