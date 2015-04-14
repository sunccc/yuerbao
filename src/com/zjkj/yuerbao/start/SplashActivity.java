package com.zjkj.yuerbao.start;

import java.io.File;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.zjkj.yuerbao.R;
import com.zjkj.yuerbao.common.SharedPreferencesHelper;
import com.zjkj.yuerbao.common.preference.AppData;
import com.zjkj.yuerbao.common.util.FileUtils;
import com.zjkj.yuerbao.common.util.NetworkState;
import com.zjkj.yuerbao.common.util.YuerbaoProperties;
import com.zjkj.yuerbao.wxapi.WXEntryActivity;

/**
 * 
 * @author Gxy
 * 
 * @TODO ����Ӧ�õĽ���
 *
 */
public class SplashActivity extends Activity {

	private boolean close = false;
	private Handler handler;
	private DelayedThread dt;
	private Intent intent;
	private ImageView backGround;
	private YuerbaoProperties yp;
	private SharedPreferencesHelper sp;
	private DownSplashImageThread downSplashImageThread;
	private NetworkState netWork;
	private String splashFilePath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		intent = new Intent();
		yp = YuerbaoProperties.getInstance();
		sp = new SharedPreferencesHelper(this, AppData.PREFS_NAME);
		netWork = new NetworkState(getApplicationContext());
		backGround = (ImageView) findViewById(R.id.background);

		if (!netWork.isWifiNetwork()) {
			Toast.makeText(getApplicationContext(), "���Ի���,������������WIFI����.",
					Toast.LENGTH_LONG).show();
		}

		// ��������Ӧ��logͼƬ
		checkSplashImage();

		handler = new Handler();
		// ��һ�����߳����������ʱ����
		dt = new DelayedThread();
		handler.postDelayed(dt, 5000);

	}

	/**
	 * @TODO ��������Ӧ��logͼƬ
	 */
	private void checkSplashImage() {
		InputStream in = null;
		File file = null;
		try {
			File sdCard = Environment.getExternalStorageDirectory();
			file = new File(sdCard + yp.getSplashFile());

			// sdcard��û��ͼƬ�����ñ���ͼƬ
			if (!file.isFile()) {
				backGround.setImageDrawable(getResources().getDrawable(
						R.drawable.splash));

				// sdcard��û��ͼƬ, ���ѷ����������ص�ͼƬ����Ϊ��������
			} else {
				Drawable drawable = Drawable.createFromPath(sdCard
						+ yp.getSplashFile());
				if (!(drawable == null)) {
					backGround.setImageDrawable(drawable);
				} else {
					backGround.setImageDrawable(getResources().getDrawable(
							R.drawable.splash));
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}

	/**
	 * @param v
	 *            ������
	 * 
	 * @TODO �����ʾ����Ĳ���
	 */
	public void openMainActivity(View v) {
		close = true;
		setClass();
		startActivity(intent);
		super.finish();
	}

	/**
	 * 
	 * @TODO ������תҳ��(��¼״̬��δ��¼״̬)
	 * 
	 * @return void
	 */
	public void setClass() {
		String className = sp.getValue(AppData.ACTIVITY_ClASS);
		String loginType = sp.getValue("LoginType");
		if (className != null && loginType != null) {
			intent.setClassName(getApplicationContext(), className);
		} else {
			intent.setClass(getApplicationContext(), WXEntryActivity.class);
		}

	}

	/**
	 * @TODO �������������ͼƬ�����̣߳�ִ��ҳ����ת
	 */
	@Override
	public void finish() {
		// TODO Auto-generated method stub

		setClass();

		// ���������߳�
		// netWork.isNetworkConnected()
		if (netWork.isWifiNetwork()) { // ���Ի���

			downSplashImageThread = new DownSplashImageThread();
			downSplashImageThread.start();

			// �Ƴ����߳�
			handler.removeCallbacks(dt);
			handler.removeCallbacks(downSplashImageThread);
		}
		if (!close) {
			startActivity(intent);
			super.finish();
		}
	}

	/**
	 * 
	 * @TODO ������ȡ������������Ӧ��logͼƬ���߳�
	 *
	 */
	class DownSplashImageThread extends Thread {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				FileUtils.downloadSplashFile();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	class DelayedThread extends Thread {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			finish();
		}
	}
}
