package com.zjkj.yuerbao.common.service;

import java.io.File;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.RemoteViews;

import com.zjkj.yuerbao.R;
import com.zjkj.yuerbao.common.util.FileUtils;
import com.zjkj.yuerbao.start.SplashActivity;

/***
 * 
 * @author Gxy
 * 
 * @TODO 更新版本
 * 
 */
public class UpdateService extends Service {
	private static final int DOWN_OK = 1;// 完成下载
	private static final int DOWN_ERROR = 0;// 下载未完成

	private NotificationManager notificationManager;
	private Notification notification;

	private Intent updateIntent;
	private PendingIntent pendingIntent;
	private int notification_id = 0;

	private String appName;
	private String downUrl;
	private File appFile;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		downUrl = intent.getStringExtra("down_url");

		// 创建文件
		appFile = FileUtils.createApkFile(appName = intent
				.getStringExtra("app_name"));

	
		// 创建通知栏
		createNotification();

		// 创建下载线程
		createThread();

		return super.onStartCommand(intent, flags, startId);

	}

	/***
	 * 开线程下载
	 */
	public void createThread() {
		/***
		 * 更新UI
		 */
		final Handler handler = new Handler() {
			@SuppressWarnings("deprecation")
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case DOWN_OK:
					// 下载完成，点击安装
					Uri uri = Uri.fromFile(appFile);
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setDataAndType(uri, "application/vnd.a"
							+ "ndroid.package-archive");// 用于打开一个apk文件

					pendingIntent = PendingIntent.getActivity(
							UpdateService.this, 0, intent, 0);

					notification.setLatestEventInfo(UpdateService.this,
							appName, "下载成功，点击安装", pendingIntent);

					notificationManager.notify(notification_id, notification);

					stopService(updateIntent);// 点击安装不会再跳转到BinStatus界面
					break;
				case DOWN_ERROR:
					notification.setLatestEventInfo(UpdateService.this,
							appName, "下载失败", pendingIntent);
					break;
				default:
					stopService(updateIntent);
					break;
				}

			}

		};

		final Message message = new Message();

		new Thread(new Runnable() {
			@Override
			public void run() {

				try {
					long downloadSize = downloadUpdateApkFile(downUrl, appFile);
					if (downloadSize > 0) {
						// 下载成功
						message.what = DOWN_OK;
						handler.sendMessage(message);
					}

				} catch (Exception e) {
					e.printStackTrace();
					message.what = DOWN_ERROR;
					handler.sendMessage(message);
				}

			}
		}).start();
	}

	/***
	 * 创建通知栏
	 */
	RemoteViews contentView;

	public void createNotification() {

		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);// 状态栏管理类
		notification = new Notification();// 状态栏

		/***
		 * 使用自定的view来显示通知
		 */
		notification.icon = R.drawable.logo;
		notification.tickerText = "开始下载";
		//notification.defaults = Notification.DEFAULT_SOUND;
		notification.when = System.currentTimeMillis();

		contentView = new RemoteViews(getPackageName(),
				R.layout.notification_item);
		contentView.setTextViewText(R.id.notificationTitle, "正在下载");
		contentView.setTextViewText(R.id.notificationPercent, "0%");
		contentView.setProgressBar(R.id.notificationProgress, 100, 0, false);

		notification.contentView = contentView;// 用于在扩展状态栏展示此通知

		updateIntent = new Intent(this, SplashActivity.class);
		updateIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);// If this is an
																// activity, it
																// must include
																// this flag
		pendingIntent = PendingIntent.getActivity(this, 0, updateIntent, 0);// 即将跳转的activity

		notification.contentIntent = pendingIntent;// 设置当扩展状态栏通知点击时跳转

		notificationManager.notify(notification_id, notification);// 触发通知

	}

	/**
	 * 下载文件
	 */
	public long downloadUpdateApkFile(String down_url, File file)
			throws Exception {

		HttpURLConnection httpURLConnection = FileUtils
				.getHttpURLConnection(down_url);
		InputStream inputStream = httpURLConnection.getInputStream();// 获取输入流
		OutputStream outputStream = new FileOutputStream(file, false);// 文件存在则覆盖掉

		byte buffer[] = new byte[1024];
		int readsize = 0;
		int downloadCount = 0;// 已经下载好的大小
		int updateCount = 0;// 已经上传的文件大小
		int down_step = 5;// 提示step
		int totalSize = httpURLConnection.getContentLength();// 文件总大小
		while ((readsize = inputStream.read(buffer)) != -1) {
			outputStream.write(buffer, 0, readsize);
			downloadCount += readsize;// 时时获取下载到的大小
			/**
			 * 每次增张5%
			 */
			if (updateCount == 0
					|| (downloadCount * 100 / totalSize - down_step) >= updateCount) {
				updateCount += down_step;

				notification.contentView.setTextViewText(
						R.id.notificationPercent, updateCount + "%");
				notification.contentView.setProgressBar(
						R.id.notificationProgress, 100, updateCount, false);

				notificationManager.notify(notification_id, notification);
			}
		}
		FileUtils.closeOutputStream(inputStream, outputStream);
		return downloadCount;
	}
}
