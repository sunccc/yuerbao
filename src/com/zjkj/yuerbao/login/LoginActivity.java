package com.zjkj.yuerbao.login;

import android.annotation.SuppressLint;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.zjkj.yuerbao.R;
import com.zjkj.yuerbao.common.SharedPreferencesHelper;
import com.zjkj.yuerbao.common.preference.WeiChatData;
import com.zjkj.yuerbao.common.util.FileUtils;

public class LoginActivity extends Activity {

	private SharedPreferencesHelper sp;
	private static final int DOWN_OK = 1;// �������
	private ImageView headImage;
	private Bitmap bmp = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_wxlogin);
		sp = new SharedPreferencesHelper(this, WeiChatData.PREFS_NAME);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//headImage = (ImageView) findViewById(R.id.headImage);

		//����΢���û�ͷ�������߳�
		createHeadimgDownLoadThread();

	}
	//����΢���û�ͷ�������߳�
	public void createHeadimgDownLoadThread() {
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
					headImage.setImageBitmap(bmp);
					break;
				}

			}

		};

		final Message message = new Message();

		//����
		new Thread(new Runnable() {
			@SuppressLint("NewApi")
			@Override
			public void run() {

				try {
					bmp = FileUtils.getHttpBitmap(sp.getValue("headimgurl"));
					if (bmp.getByteCount() > 0) {
						// ���سɹ�
						message.what = DOWN_OK;
						handler.sendMessage(message);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}).start();
	}

}
