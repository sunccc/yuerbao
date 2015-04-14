package com.zjkj.yuerbao.start.bind;

import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;

import com.zjkj.yuerbao.R;
import com.zjkj.yuerbao.common.SharedPreferencesHelper;
import com.zjkj.yuerbao.common.preference.AppData;
import com.zjkj.yuerbao.common.widget.ClearEditText;
import com.zjkj.yuerbao.main.MainActivity;
import com.zjkj.yuerbao.start.BindStatus;

/**
 * 
 * @author Gxy
 * 
 * @TODO 接受信息时间绑定界面
 *
 */
public class HopeTime extends Activity {

	private TimePicker htTime;
	private EditText htTimeText;
	private SharedPreferencesHelper sp;
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hope_time);
		sp = new SharedPreferencesHelper(this, AppData.PREFS_NAME);
		intent = new Intent();

		htTime = (TimePicker) findViewById(R.id.htTimePicker);
		htTimeText = (ClearEditText) findViewById(R.id.timeEditText);
		htTimeText.requestFocus();
		htTimeText.setInputType(InputType.TYPE_NULL);

		htTime.setOnTimeChangedListener(new HtTimeChangedListener());

	}

	public void clearDateEditText(View v) {
		htTimeText.setText("");
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		intent.setClass(this, BindStatus.class);

		startActivityForResult(intent, 0);
		// startActivity(intent);
		finish();
		return super.onKeyDown(keyCode, event);
	}

	public void saveHopeTime(View v) {
		if (htTimeText.getText().length() == 0) {
			Toast.makeText(getApplicationContext(), "您还有没有绑定接受信息的时间哦!",
					Toast.LENGTH_LONG).show();
		} else {

			intent.setClass(HopeTime.this, MainActivity.class);
			sp.putValue(AppData.BIND_MSG_TIME, htTimeText.getText()
					.toString());
			startActivity(intent);
			finish();
		}
	}

	class HtTimeChangedListener implements OnTimeChangedListener {

		@Override
		public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
			// TODO Auto-generated method stub

			try {
				Method method = view.getClass().getDeclaredMethod("setCurrentMinute",
						Integer.class);

				method.setAccessible(true);
				method.invoke(new TimePicker(getApplicationContext()), new Integer(0));
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			htTimeText.setText(hourOfDay +":00");

		}

	}
}
