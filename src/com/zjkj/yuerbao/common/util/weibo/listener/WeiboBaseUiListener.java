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
		// �� Bundle �н��� Token
		Toast.makeText(mContext, "������ת���Եȣ�", Toast.LENGTH_LONG).show();
		sp = new SharedPreferencesHelper(mContext, AppData.PREFS_NAME);
		mAccessToken = Oauth2AccessToken.parseAccessToken(values);
		if (mAccessToken.isSessionValid()) {
			getUserInfo();
			// ���� Token �� SharedPreferences
			// AccessTokenKeeper.writeAccessToken(WeiboBaseUiListener.this,
			// mAccessToken);
		} else {
			// ����ע���Ӧ�ó���ǩ������ȷʱ���ͻ��յ� Code����ȷ��ǩ����ȷ
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

				System.out.println("�ص���΢���û���Ϣ:" + wBUserInfo.toString());
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
