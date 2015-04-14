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
 * @TODO 启动应用的界面
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
			Toast.makeText(getApplicationContext(), "测试环境,部分数据来自WIFI网络.",
					Toast.LENGTH_LONG).show();
		}

		// 设置启动应用log图片
		checkSplashImage();

		handler = new Handler();
		// 绑定一个子线程作界面的延时操作
		dt = new DelayedThread();
		handler.postDelayed(dt, 5000);

	}

	/**
	 * @TODO 设置启动应用log图片
	 */
	private void checkSplashImage() {
		InputStream in = null;
		File file = null;
		try {
			File sdCard = Environment.getExternalStorageDirectory();
			file = new File(sdCard + yp.getSplashFile());

			// sdcard中没有图片，设置备用图片
			if (!file.isFile()) {
				backGround.setImageDrawable(getResources().getDrawable(
						R.drawable.splash));

				// sdcard中没有图片, 将把服务器端下载的图片设置为启动背景
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
	 *            主界面
	 * 
	 * @TODO 点击显示界面的操作
	 */
	public void openMainActivity(View v) {
		close = true;
		setClass();
		startActivity(intent);
		super.finish();
	}

	/**
	 * 
	 * @TODO 设置跳转页面(登录状态与未登录状态)
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
	 * @TODO 开启启动界面的图片下载线程，执行页面跳转
	 */
	@Override
	public void finish() {
		// TODO Auto-generated method stub

		setClass();

		// 启动下载线程
		// netWork.isNetworkConnected()
		if (netWork.isWifiNetwork()) { // 测试环境

			downSplashImageThread = new DownSplashImageThread();
			downSplashImageThread.start();

			// 移除子线程
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
	 * @TODO 用来获取服务器端启动应用log图片的线程
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
