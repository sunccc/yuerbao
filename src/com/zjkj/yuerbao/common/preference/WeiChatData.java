package com.zjkj.yuerbao.common.preference;

import org.json.JSONObject;

/**
 * 
 * @author Gxy
 * 
 *         ΢����������(Ӧ���µ�shared_prefsĿ¼�ļ������ļ��µČ���)
 */
public interface WeiChatData {
	// �����xml�ļ���
	public static final String PREFS_NAME = "WeiChatUserInfo";
	// ��Ӧ����΢�ſ���ƽ̨�����APP_ID
	public static final String APP_ID = "wxccb6a66d04f25b02";
	// ��Ӧ����΢�ſ���ƽ̨���ͨ�����Ӧ����ԿAppSecret
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
