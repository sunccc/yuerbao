package com.zjkj.yuerbao.start.bind;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
 * @author Gxy
 *
 * @TODO 宝宝生日绑定界面
 */
public class BaobaoDate extends Activity {

	private DatePicker bbDate;
	private EditText bbDateText;
	private TextView btnSave;
	private SharedPreferencesHelper sp;
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_baobao_date);
		sp = new SharedPreferencesHelper(this, AppData.PREFS_NAME);
		intent = new Intent();

		bbDate = (DatePicker) findViewById(R.id.bbDate);
		bbDateText = (ClearEditText) findViewById(R.id.dateEditText);
		btnSave = (TextView) findViewById(R.id.btnSave);
		bbDateText.setInputType(InputType.TYPE_NULL);
		setDisabledTextViews(bbDate);

		btnSave.setOnClickListener(new SaveOnClickLitener());
		bbDate.init(bbDate.getYear(), bbDate.getMonth(),
				bbDate.getDayOfMonth(), new BaobaoDateChangedListener());
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		intent.setClass(this, BindStatus.class);
		startActivity(intent);
		finish();
		return super.onKeyDown(keyCode, event);
	}

	public void saveHyData(View v) {

		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		Date chooseDate = null;

		if (bbDateText.getText().length() == 0) {
			Toast.makeText(getApplicationContext(), "您还有没有绑定宝宝生日哦!",
					Toast.LENGTH_LONG).show();
		} else {
			try {
				chooseDate = sdf.parse(bbDateText.getText().toString());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (now.getTime() < chooseDate.getTime()) {

				Toast.makeText(this, "您的宝宝还未出生！", Toast.LENGTH_LONG).show();
				clearDateEditText(bbDate);

			} else {

				intent.setClass(BaobaoDate.this, MainActivity.class);
				sp.putValue(AppData.BIND_STATUS, "BB");
				sp.putValue(AppData.BIND_DATE, bbDateText.getText()
						.toString());
				startActivity(intent);
				finish();
			}
		}
	}

	// 保存按键监听类
	public class SaveOnClickLitener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			saveHyData(v);

		}
	}

	public class BaobaoDateChangedListener implements OnDateChangedListener {
		@Override
		public void onDateChanged(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// TODO Auto-generated method stub
			bbDateText.setText(year + "/" + (monthOfYear + 1) + "/"
					+ dayOfMonth);
		}
	}

	// 清除日期
	public void clearDateEditText(View v) {

		bbDateText.setText("");
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
