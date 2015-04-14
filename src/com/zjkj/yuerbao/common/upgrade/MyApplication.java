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
 * @TODO 版本信息服务类
 * 
 */
public class MyApplication {

	private int localVersion;// 本地安装版本

	private VersionInfo versionInfo;// 版本信息类

	private Context context;

	private YuerbaoProperties yp;

	/**
	 * @return 是否有新版本
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

			// TODO 获取远程服务器版本信息
			@Override
			public void run() {

				getVersionInfoByHttpConn();
			};
		});
		thread.start();
		// 暂停5秒钟，获取服务端的版本信息
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
			// 将服务器端的版本信息文件解析为VersionInfo类
			setVersionInfo(VersionXmlPullPaser.readXML(in));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @return 本地版本号
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
	 * 检查是否更新版本
	 * 
	 * @throws Exception
	 */
	public void checkVersion(Context context) throws Exception {

		// 获取版本信息类

		if (!isNewVersion()) {

			// 发现新版本，提示用户更新
			AlertDialog.Builder alert = new AlertDialog.Builder(context);

			alert.setTitle("软件升级");
			alert.setMessage("发现新版本,建议立即更新使用.");
			alert.setPositiveButton("更新", new UpdateBtnOnclickListener());
			alert.setNegativeButton("取消",
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
	 * @TODO 点击更新按键的事件处理
	 *
	 */
	class UpdateBtnOnclickListener implements DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			Intent updateIntent = new Intent(getContext(), UpdateService.class);
			updateIntent.putExtra("app_name", getVersionInfo().getApkName());// 指定下载的应用名称
			updateIntent
					.putExtra("down_url", getVersionInfo().getDownloadURL());// 指定下载的应用名称

			getContext().startService(updateIntent);
		}

	}
}
