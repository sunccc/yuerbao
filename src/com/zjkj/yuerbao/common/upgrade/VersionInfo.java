package com.zjkj.yuerbao.common.upgrade;

/**
 * ����汾��Ϣ����
 * 
 * @author Royal
 * 
 */
public class VersionInfo {
	// �汾�����ַ���
	private String version;
	// �汾����ʱ��
	private String updateTime;
	// �°汾�������ص�ַ
	private String downloadURL;
	// ����������Ϣ
	private String displayMessage;
	// �汾��
	private int versionCode;
	// apk����
	private String apkName;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getDownloadURL() {
		return downloadURL;
	}

	public void setDownloadURL(String downloadURL) {
		this.downloadURL = downloadURL;
	}

	public String getDisplayMessage() {
		return displayMessage;
	}

	public void setDisplayMessage(String displayMessage) {
		this.displayMessage = displayMessage;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public String getApkName() {
		return apkName;
	}

	public void setApkName(String apkName) {
		this.apkName = apkName;
	}
}