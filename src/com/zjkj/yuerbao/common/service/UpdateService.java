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
 * @TODO ���°汾
 * 
 */
public class UpdateService extends Service {
	private static final int DOWN_OK = 1;// �������
	private static final int DOWN_ERROR = 0;// ����δ���

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

		// �����ļ�
		appFile = FileUtils.createApkFile(appName = intent
				.getStringExtra("app_name"));

	
		// ����֪ͨ��
		createNotification();

		// ���������߳�
		createThread();

		return super.onStartCommand(intent, flags, startId);

	}

	/***
	 * ���߳�����
	 */
	public void createThread() {
		/***
		 * ����UI
		 */
		final Handler handler = new Handler() {
			@SuppressWarnings("deprecation")
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case DOWN_OK:
					// ������ɣ������װ
					Uri uri = Uri.fromFile(appFile);
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setDataAndType(uri, "application/vnd.a"
							+ "ndroid.package-archive");// ���ڴ�һ��apk�ļ�

					pendingIntent = PendingIntent.getActivity(
							UpdateService.this, 0, intent, 0);

					notification.setLatestEventInfo(UpdateService.this,
							appName, "���سɹ��������װ", pendingIntent);

					notificationManager.notify(notification_id, notification);

					stopService(updateIntent);// �����װ��������ת��BinStatus����
					break;
				case DOWN_ERROR:
					notification.setLatestEventInfo(UpdateService.this,
							appName, "����ʧ��", pendingIntent);
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
						// ���سɹ�
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
	 * ����֪ͨ��
	 */
	RemoteViews contentView;

	public void createNotification() {

		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);// ״̬��������
		notification = new Notification();// ״̬��

		/***
		 * ʹ���Զ���view����ʾ֪ͨ
		 */
		notification.icon = R.drawable.logo;
		notification.tickerText = "��ʼ����";
		//notification.defaults = Notification.DEFAULT_SOUND;
		notification.when = System.currentTimeMillis();

		contentView = new RemoteViews(getPackageName(),
				R.layout.notification_item);
		contentView.setTextViewText(R.id.notificationTitle, "��������");
		contentView.setTextViewText(R.id.notificationPercent, "0%");
		contentView.setProgressBar(R.id.notificationProgress, 100, 0, false);

		notification.contentView = contentView;// ��������չ״̬��չʾ��֪ͨ

		updateIntent = new Intent(this, SplashActivity.class);
		updateIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);// If this is an
																// activity, it
																// must include
																// this flag
		pendingIntent = PendingIntent.getActivity(this, 0, updateIntent, 0);// ������ת��activity

		notification.contentIntent = pendingIntent;// ���õ���չ״̬��֪ͨ���ʱ��ת

		notificationManager.notify(notification_id, notification);// ����֪ͨ

	}

	/**
	 * �����ļ�
	 */
	public long downloadUpdateApkFile(String down_url, File file)
			throws Exception {

		HttpURLConnection httpURLConnection = FileUtils
				.getHttpURLConnection(down_url);
		InputStream inputStream = httpURLConnection.getInputStream();// ��ȡ������
		OutputStream outputStream = new FileOutputStream(file, false);// �ļ������򸲸ǵ�

		byte buffer[] = new byte[1024];
		int readsize = 0;
		int downloadCount = 0;// �Ѿ����غõĴ�С
		int updateCount = 0;// �Ѿ��ϴ����ļ���С
		int down_step = 5;// ��ʾstep
		int totalSize = httpURLConnection.getContentLength();// �ļ��ܴ�С
		while ((readsize = inputStream.read(buffer)) != -1) {
			outputStream.write(buffer, 0, readsize);
			downloadCount += readsize;// ʱʱ��ȡ���ص��Ĵ�С
			/**
			 * ÿ������5%
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
