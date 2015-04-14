package com.zjkj.yuerbao.common.util;

import java.io.IOException;
import java.util.Properties;

import android.content.Context;

/**
 * 
 * @author Gxy
 *
 * @TODO 用来获取资源文件(启动图片，apk下载地址和保存在硬盘上的路径...)
 */
public final class YuerbaoProperties {

	private Properties pro;

	private YuerbaoProperties() {
		pro = new Properties();
		try {
			
//			pro.load(context.getAssets().open("yuerbao.properties"));
			pro.load(getClass().getResourceAsStream("/assets/yuerbao.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static YuerbaoProperties getInstance() {

		return new YuerbaoProperties();
	}

	public String getApkDir() {
		return pro.getProperty("apkDir");
	}

	public String getSplashDir() {
		return pro.getProperty("splashDir");
	}

	public String getSplashFile() {
		return pro.getProperty("splashFile");
	}

	public String getLoadSplashFileUrl() {
		return pro.getProperty("loadSplashFileUrl");
	}

	public String getLoadVersionInfoXmlUrl() {
		return pro.getProperty("loadVersionInfoXmlUrl");
	}

}
