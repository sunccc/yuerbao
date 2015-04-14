package com.zjkj.yuerbao.start.bind;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.TextView;
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
 * @TODO 备孕期绑定界面
 *
 */
public class BeiyunDate extends Activity {

	private DatePicker byDate;
	private EditText byDateText;
	private TextView btnSave;
	private SharedPreferencesHelper sp;
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_beiyun_date);
		sp = new SharedPreferencesHelper(this, AppData.PREFS_NAME);
		intent = new Intent();

		byDate = (DatePicker) findViewById(R.id.bbDate);
		byDateText = (ClearEditText) findViewById(R.id.dateEditText);
		btnSave = (TextView) findViewById(R.id.btnSave);
		byDateText.setInputType(InputType.TYPE_NULL);
		setDisabledTextViews(byDate);

		btnSave.setOnClickListener(new SaveOnClickLitener());
		byDate.init(byDate.getYear(), byDate.getMonth(),
				byDate.getDayOfMonth(), new BeiyunDateChangedListener());

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		intent.setClass(this, BindStatus.class);
		startActivity(intent);
		finish();
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * @param v
	 *            保存组件
	 * 
	 * @TODO 保存备孕日期,然后跳转到下一个界面
	 */
	public void saveHyData(View v) {
		if (byDateText.getText().length() == 0) {
			Toast.makeText(getApplicationContext(), "您还有没有绑定备孕期哦!",
					Toast.LENGTH_LONG).show();
		} else {
			Intent intent = new Intent();
			intent.setClass(BeiyunDate.this, MainActivity.class);
			sp.putValue(AppData.BIND_STATUS, "BY");
			sp.putValue(AppData.BIND_DATE, byDateText.getText()
					.toString());
			startActivity(intent);
			finish();
		}
	}

	class SaveOnClickLitener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			saveHyData(v);

		}
	}

	class BeiyunDateChangedListener implements OnDateChangedListener {

		@Override
		public void onDateChanged(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// TODO Auto-generated method stub
			byDateText.setText(year + "/" + (monthOfYear + 1) + "/"
					+ dayOfMonth);
		}

	}

	// 清除日期
	public void clearDateEditText(View v) {

		byDateText.setText("");
	}
	private void setDisabledTextViews(ViewGroup dp)

	{

		for (int x = 0, n = dp.getChildCount(); x < n; x++)

		{

			View v = dp.getChildAt(x);

			if (v instanceof TextView)

			{

				v.setEnabled(false);

			}

			else if (v instanceof ViewGroup)

			{

				setDisabledTextViews((ViewGroup) v);

			}

		}

	}
}
