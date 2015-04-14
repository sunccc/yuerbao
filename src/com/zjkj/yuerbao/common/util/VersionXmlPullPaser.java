package com.zjkj.yuerbao.common.util;

import java.io.InputStream;


import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

import com.zjkj.yuerbao.common.upgrade.VersionInfo;

public class VersionXmlPullPaser {

	public static VersionInfo readXML(InputStream inStream) throws Exception {

		VersionInfo versionInfo = null;

		XmlPullParser parser = Xml.newPullParser(); // 由android.util.Xml创建一个XmlPullParser实例
		parser.setInput(inStream, "UTF-8"); // 设置输入流 并指明编码方式

		int eventType = parser.getEventType();// 事件类型

		while (eventType != XmlPullParser.END_DOCUMENT) {// 结束返回
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:// 读取到xml的声明返回
				break;
			case XmlPullParser.START_TAG:// 开始标签
				if (parser.getName().equals("update")) {
					versionInfo = new VersionInfo();
				} else if (parser.getName().equals("version")) {
					eventType = parser.next();
					versionInfo.setVersion(parser.getText());
				} else if (parser.getName().equals("versionCode")) {
					eventType = parser.next();
					versionInfo.setVersionCode(Integer.parseInt(parser
							.getText()));
				} else if (parser.getName().equals("updateTime")) {
					eventType = parser.next();
					versionInfo.setUpdateTime(parser.getText());
				} else if (parser.getName().equals("apkName")) {
					eventType = parser.next();
					versionInfo.setApkName(parser.getText());
				} else if (parser.getName().equals("downloadURL")) {
					eventType = parser.next();
					versionInfo.setDownloadURL(parser.getText());
				} else if (parser.getName().equals("displayMessage")) {
					eventType = parser.next();
					versionInfo.setDisplayMessage(parser.getText());
				}
				break;
			case XmlPullParser.END_TAG:// 结束标签
				break;
			}
			eventType = parser.next();
		}
		
		return versionInfo;
	}
}
