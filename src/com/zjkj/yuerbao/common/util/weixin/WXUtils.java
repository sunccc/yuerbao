package com.zjkj.yuerbao.common.util.weixin;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zjkj.yuerbao.common.preference.WeiChatData;
import com.zjkj.yuerbao.common.util.FileUtils;

public class WXUtils {
	private static IWXAPI api;
	private static JSONObject TOKEN = null;

	public static JSONObject getTokenByCode(final String code) {

		
		return FileUtils.inputStreamToJson(WeiChatData.TOKEN_URL + code);
	}

	public static JSONObject refreshToken(String refresh_token) {

		return FileUtils.inputStreamToJson(WeiChatData.REFRESH_TOKENURL
				+ refresh_token);

	}

	public static boolean isTokenValid(String token, String openid) {

		// isTokenValidUrl += "access_token=" + token + "&openid=" + openid;
		String isTokenValidUrl = WeiChatData.TOKEN_VALIDURL + "access_token="
				+ token + "&openid=" + openid;

		JSONObject obj = FileUtils.inputStreamToJson(isTokenValidUrl);
		int flag = 1;
		try {
			flag = obj.getInt("errcode");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag == 1 ? false : true;
	}

	public static JSONObject getUserInfo(String token, String openid) {

		// getUserInfoUrl += "access_token=" + token + "&openid=" + openid;
		String getUserInfoUrl = WeiChatData.WEICHAT_USERINFOURL
				+ "access_token=" + token + "&openid=" + openid;
		return FileUtils.inputStreamToJson(getUserInfoUrl);
	}

	/**
	 * @param intent 
	 * @TODO ������Ȩ����
	 */
	public static void sendAuthReq() {
		SendAuth.Req req = new SendAuth.Req(); 
		req.scope = "snsapi_userinfo";
		req.state = "wechat_sdk_demo_test";
		api.sendReq(req);
	}

	/**
	 * ��Ӧ��ע�ᵽ΢��
	 */
	public static void regToWx(Activity activity) {
		// ��ȡIWXAPIʵ��
		api = WXAPIFactory.createWXAPI(activity.getApplicationContext(),
				WeiChatData.APP_ID, true);
		// ��APP_IDע�ᵽ΢��
		api.registerApp(WeiChatData.APP_ID);
		// �����յ��� intent���ݸ�IWXAPI
		api.handleIntent(activity.getIntent(), (IWXAPIEventHandler) activity);
	}
}
