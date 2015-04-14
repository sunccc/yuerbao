package com.zjkj.yuerbao.common.preference;

/**
 * 
 * @author Gxy
 * 
 *         WeiBo��������(Ӧ���µ�shared_prefsĿ¼�ļ������ļ��µČ���)
 * 
 */
public interface WeiBoData {
	// �����xml�ļ���
	public static final String PREFS_NAME = "WeiBoData";
	// ��Ӧ����΢�ſ���ƽ̨�����APP_KEY
	public static final String APP_KEY = "1623533867";
	/**
	 * ��ǰ DEMO Ӧ�õĻص�ҳ��������Ӧ�ÿ���ʹ���Լ��Ļص�ҳ��
	 * ����ʹ��Ĭ�ϻص�ҳ��https://api.weibo.com/oauth2/default.html
	 */
	public static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";

	/**
	 * WeiboSDKDemo Ӧ�ö�Ӧ��Ȩ�ޣ�������������һ�㲻��Ҫ��ô�࣬��ֱ�����óɿռ��ɡ� ������鿴 Demo �ж�Ӧ��ע�͡�
	 */
	public static final String SCOPE = "email,direct_messages_read,direct_messages_write,"
			+ "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
			+ "follow_app_official_microblog," + "invitation_write";

	public static final String USER_SHOW = "https://api.weibo.com/2/users/show.json";

}
