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
 * @TODO 预产期绑定界面
 */
public class YuchanDate extends Activity {

	private DatePicker ycDate;
	private EditText ycDateText;
	private TextView btnSave;
	private SharedPreferencesHelper sp;
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_yuchan_date);
		sp = new SharedPreferencesHelper(this, AppData.PREFS_NAME);
		intent = new Intent();

		// 获取组件
		ycDate = (DatePicker) findViewById(R.id.bbDate);
		ycDateText = (ClearEditText) findViewById(R.id.dateEditText);
		btnSave = (TextView) findViewById(R.id.btnSave);
		// 点击日期编辑框不弹出软键盘
		ycDateText.setInputType(InputType.TYPE_NULL);
		setDisabledTextViews(ycDate);

		btnSave.setOnClickListener(new SaveOnClickLitener());// 设置监听
		ycDate.init(ycDate.getYear(), ycDate.getMonth(),
				ycDate.getDayOfMonth(), new YunchanDateChangedListener());
	}

	/**
	 * @param v
	 *            保存组件
	 * 
	 * @TODO 保存预产日期,然后跳转到下一个界面
	 */
	public void saveHyData(View v) {

		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		Date chooseDate = null;

		if (ycDateText.getText().length() == 0) {
			Toast.makeText(getApplicationContext(), "您还有没有绑定预产期哦!",
					Toast.LENGTH_LONG).show();
		} else {

			try {
				chooseDate = sdf.parse(ycDateText.getText().toString());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (now.getTime() > chooseDate.getTime()
					|| now.getTime() == chooseDate.getTime()) {

				Toast.makeText(this, "预产期已过，请重新填写", Toast.LENGTH_LONG).show();
				clearDateEditText(ycDate);

			} else {

				intent.setClass(YuchanDate.this, MainActivity.class);
				sp.putValue(AppData.BIND_STATUS, "YC");
				sp.putValue(AppData.BIND_DATE, ycDateText.getText()
						.toString());
				finish();
				startActivity(intent);
			}
		}
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
	 * 
	 * @TODO 保存日期
	 */
	class SaveOnClickLitener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			saveHyData(v);

		}
	}

	/**
	 *
	 * @TODO 日期控件监听类（同步输入框中的数据）
	 */
	class YunchanDateChangedListener implements OnDateChangedListener {

		@Override
		public void onDateChanged(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// TODO Auto-generated method stub
			ycDateText.setText(year + "/" + (monthOfYear + 1) + "/"
					+ dayOfMonth);
		}
	}

	/**
	 * 
	 * @param v
	 *            清除图标ImageView
	 * 
	 * @TODO 清除日期
	 */
	public void clearDateEditText(View v) {
		ycDateText.setText("");
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
