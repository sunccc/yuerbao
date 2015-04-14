package com.zjkj.yuerbao.common.preference;

/**
 * 
 * @author Gxy
 * 
 *         WeiBo优先数据(应用下的shared_prefs目录文件名及文件下的屬性)
 * 
 */
public interface WeiBoData {
	// 保存的xml文件名
	public static final String PREFS_NAME = "WeiBoData";
	// 本应用在微信开发平台申请的APP_KEY
	public static final String APP_KEY = "1623533867";
	/**
	 * 当前 DEMO 应用的回调页，第三方应用可以使用自己的回调页。
	 * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
	 */
	public static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";

	/**
	 * WeiboSDKDemo 应用对应的权限，第三方开发者一般不需要这么多，可直接设置成空即可。 详情请查看 Demo 中对应的注释。
	 */
	public static final String SCOPE = "email,direct_messages_read,direct_messages_write,"
			+ "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
			+ "follow_app_official_microblog," + "invitation_write";

	public static final String USER_SHOW = "https://api.weibo.com/2/users/show.json";

}
