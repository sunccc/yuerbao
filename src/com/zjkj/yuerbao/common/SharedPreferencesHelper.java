package com.zjkj.yuerbao.common;

import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 
 * @author Gxy
 * 
 * @TODO 与应用有关的属性设置类
 *
 */
public class SharedPreferencesHelper {

	private SharedPreferences sp;
	private SharedPreferences.Editor editor;
	private Context context;

	public SharedPreferencesHelper(Context c, String name) {
		context = c;
		// 在应用目录下生成share_prefs/name.xml文件
		sp = context.getSharedPreferences(name, 0);
		editor = sp.edit();
	}

	public void putValue(String key, Object value) {

		if (sp.contains(key)) {
			editor.remove(key);
		}

		if (value instanceof Integer) {
			editor.putInt(key, (Integer) value);
		} else if (value instanceof Boolean) {
			editor.putBoolean(key, (Boolean) value);
		} else if (value instanceof String) {
			editor.putString(key, (String) value);
		}
		editor.commit();
	}

	public SharedPreferences getSp() {
		return sp;
	}

	public void setSp(SharedPreferences sp) {
		this.sp = sp;
	}

	public SharedPreferences.Editor getEditor() {
		return editor;
	}

	public void setEditor(SharedPreferences.Editor editor) {
		this.editor = editor;
	}

	public String getValue(String key) {
		return sp.getString(key, null);
	}

	public boolean getBoolean(String key) {
		return sp.getBoolean(key, false);
	}

	public String getValue(String key, String defValue) {
		return sp.getString(key, defValue);
	}
}
