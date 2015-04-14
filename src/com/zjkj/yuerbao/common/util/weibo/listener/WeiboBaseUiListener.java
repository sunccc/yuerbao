package com.zjkj.yuerbao.common.util.weibo.listener;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;
import com.zjkj.yuerbao.common.SharedPreferencesHelper;
import com.zjkj.yuerbao.common.preference.AppData;
import com.zjkj.yuerbao.common.preference.WeiBoData;
import com.zjkj.yuerbao.common.util.FileUtils;

public class WeiboBaseUiListener implements WeiboAuthListener{
	private Oauth2AccessToken mAccessToken;
	private Activity mContext;
	private SharedPreferencesHelper sp;

	public WeiboBaseUiListener(Activity mContext) {
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
	}

	@SuppressLint("NewApi")
	@Override
	public void onComplete(Bundle values) {
		// 从 Bundle 中解析 Token
		Toast.makeText(mContext, "正在跳转请稍等！", Toast.LENGTH_LONG).show();
		sp = new SharedPreferencesHelper(mContext, AppData.PREFS_NAME);
		mAccessToken = Oauth2AccessToken.parseAccessToken(values);
		if (mAccessToken.isSessionValid()) {
			getUserInfo();
			// 保存 Token 到 SharedPreferences
			// AccessTokenKeeper.writeAccessToken(WeiboBaseUiListener.this,
			// mAccessToken);
		} else {
			// 当您注册的应用程序签名不正确时，就会收到 Code，请确保签名正确
			String code = values.getString("code", "");
		}
	}

	@Override
	public void onCancel() {
	}

	@Override
	public void onWeiboException(WeiboException e) {
	}

	public void getUserInfo() {
		// TODO Auto-generated method stub
		new Thread() {
			@Override
			public void run() {
				String userShowUrl = WeiBoData.USER_SHOW;
				String url = userShowUrl + "?access_token="
						+ mAccessToken.getToken() + "&uid="
						+ mAccessToken.getUid();
				System.out.println(url);
				JSONObject wBUserInfo = FileUtils.inputStreamToJson(url);
				// profile_image_url
				// screen_name

				System.out.println("回调到微博用户信息:" + wBUserInfo.toString());
				sp.putValue("LoginType", "WeiBoUser");
				try {
					sp.putValue("nickname", wBUserInfo.getString("screen_name"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}.start();
	}
}
