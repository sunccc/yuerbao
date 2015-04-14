package com.zjkj.yuerbao.common.util.qq.listener;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.zjkj.yuerbao.common.SharedPreferencesHelper;
import com.zjkj.yuerbao.common.preference.AppData;
import com.zjkj.yuerbao.common.util.qq.util.Util;

public class QQBaseUiListener implements IUiListener {
	private Activity mContext;
	private String mScope;
	private Tencent mTencent;
	private SharedPreferencesHelper sp;

	public QQBaseUiListener(Activity mContext) {
		super();
		this.mContext = mContext;
	}

	public QQBaseUiListener(Activity mContext, Tencent mTencent) {
		super();
		this.mContext = mContext;
		this.mTencent = mTencent;
	}

	@Override
	public void onComplete(Object response) {
		Toast.makeText(mContext, "正在跳转请稍等！", Toast.LENGTH_LONG).show();		
		sp = new SharedPreferencesHelper(mContext, AppData.PREFS_NAME);
		JSONObject json = (JSONObject) response;
		updateUserInfo();
	}

	@Override
	public void onError(UiError e) {
		showResult("onError:", "code:" + e.errorCode + ", msg:"
				+ e.errorMessage + ", detail:" + e.errorDetail);
	}

	private void showResult(String string, String string2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCancel() {
		showResult("onCancel", "");
	}

	Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				JSONObject response = (JSONObject) msg.obj;
				if (response.has("nickname")) {

					// 用户昵称
					try {
						sp.putValue("nickname", response.get("nickname"));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			} else if (msg.what == 1) {
				Bitmap bitmap = (Bitmap) msg.obj;
				// 用户信息
				// mUserLogo.setImageBitmap(bitmap);
				// mUserLogo.setVisibility(android.view.View.VISIBLE);
			}
		}

	};

	private void updateUserInfo() {
		sp.putValue("LoginType", "QQUser");
		if (mTencent != null && mTencent.isSessionValid()) {
			IUiListener listener = new IUiListener() {

				@Override
				public void onError(UiError e) {

				}

				@Override
				public void onComplete(final Object response) {
					Message msg = new Message();
					msg.obj = response;
					msg.what = 0;
					mHandler.sendMessage(msg);
					new Thread() {

						@Override
						public void run() {
							JSONObject json = (JSONObject) response;
							if (json.has("figureurl")) {
								Bitmap bitmap = null;
								try {
									bitmap = Util.getbitmap(json
											.getString("figureurl_qq_2"));

									System.out.println("回调到qq用户信息:"
											+ response.toString());
								} catch (JSONException e) {

								}
								Message msg = new Message();
								msg.obj = bitmap;
								msg.what = 1;
								mHandler.sendMessage(msg);
							}
						}

					}.start();
				}

				@Override
				public void onCancel() {

				}
			};
			UserInfo mInfo = new UserInfo(mContext.getApplicationContext(),
					mTencent.getQQToken());
			mInfo.getUserInfo(listener);

		} else {
			// mUserInfo.setText("");
			// mUserInfo.setVisibility(android.view.View.GONE);
			// mUserLogo.setVisibility(android.view.View.GONE);
		}
	}
}
