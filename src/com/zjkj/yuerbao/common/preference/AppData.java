package com.zjkj.yuerbao.common.preference;

/**
 * 
 * @author Gxy
 * 
 *         放置偏好属性(应用下的shared_prefs目录文件名及文件下的屬性)
 * 
 */
public interface AppData {
	// 保存的xml文件名
	public static final String PREFS_NAME = "PreferenceData";
	// xml文件中保存的自定义标记(放置下一个跳转的界面类)
	public static final String ACTIVITY_ClASS = "class";
	// 状态绑定属性
	public static final String BIND_STATUS = "bindStatus";
	// 日期绑定属性
	public static final String BIND_DATE = "bindDate";
	// 接受信息时间绑定属性
	public static final String BIND_MSG_TIME = "bindGetMessageTime";
	// 是否有新版本
	public static final String IS_NEWVERSION = "isNewVersion";
	// 应用名称
	public static final String APP_NAME = "育儿宝";
	// 下载地址
	public static final String DOWNLOAD_URL = "down_url";

}
