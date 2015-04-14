package com.zjkj.yuerbao.common.preference;

import org.json.JSONObject;

/**
 * 
 * @author Gxy
 * 
 *         微信优先数据(应用下的shared_prefs目录文件名及文件下的屬性)
 */
public interface WeiChatData {
	// 保存的xml文件名
	public static final String PREFS_NAME = "WeiChatUserInfo";
	// 本应用在微信开发平台申请的APP_ID
	public static final String APP_ID = "wxccb6a66d04f25b02";
	// 本应用在微信开发平台审核通过后的应用密钥AppSecret
	public static final String SECRET = "63182e7dafba64b4d806f06789a3da61";
	public static final String GRANT_TYPE = "authorization_code";
	public static final String TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
			+ APP_ID
			+ "&secret="
			+ SECRET
			+ "&grant_type=authorization_code&code=";;
	public static final String REFRESH_TOKENURL = "https: // api.weixin.qq.com/sns/oauth2/refresh_token?appid="
			+ APP_ID + "&grant_type=refresh_token&refresh_token=";;
	public static final String TOKEN_VALIDURL = "https://api.weixin.qq.com/sns/auth?";
	public static final String WEICHAT_USERINFOURL = "https://api.weixin.qq.com/sns/userinfo?";

}
