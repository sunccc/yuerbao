package com.zjkj.yuerbao.common.util;

import java.io.InputStream;


import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

import com.zjkj.yuerbao.common.upgrade.VersionInfo;

public class VersionXmlPullPaser {

	public static VersionInfo readXML(InputStream inStream) throws Exception {

		VersionInfo versionInfo = null;

		XmlPullParser parser = Xml.newPullParser(); // ��android.util.Xml����һ��XmlPullParserʵ��
		parser.setInput(inStream, "UTF-8"); // ���������� ��ָ�����뷽ʽ

		int eventType = parser.getEventType();// �¼�����

		while (eventType != XmlPullParser.END_DOCUMENT) {// ��������
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:// ��ȡ��xml����������
				break;
			case XmlPullParser.START_TAG:// ��ʼ��ǩ
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
			case XmlPullParser.END_TAG:// ������ǩ
				break;
			}
			eventType = parser.next();
		}
		
		return versionInfo;
	}
}
