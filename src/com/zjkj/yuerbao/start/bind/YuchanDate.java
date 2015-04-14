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
 * @TODO Ԥ���ڰ󶨽���
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

		// ��ȡ���
		ycDate = (DatePicker) findViewById(R.id.bbDate);
		ycDateText = (ClearEditText) findViewById(R.id.dateEditText);
		btnSave = (TextView) findViewById(R.id.btnSave);
		// ������ڱ༭�򲻵��������
		ycDateText.setInputType(InputType.TYPE_NULL);
		setDisabledTextViews(ycDate);

		btnSave.setOnClickListener(new SaveOnClickLitener());// ���ü���
		ycDate.init(ycDate.getYear(), ycDate.getMonth(),
				ycDate.getDayOfMonth(), new YunchanDateChangedListener());
	}

	/**
	 * @param v
	 *            �������
	 * 
	 * @TODO ����Ԥ������,Ȼ����ת����һ������
	 */
	public void saveHyData(View v) {

		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		Date chooseDate = null;

		if (ycDateText.getText().length() == 0) {
			Toast.makeText(getApplicationContext(), "������û�а�Ԥ����Ŷ!",
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

				Toast.makeText(this, "Ԥ�����ѹ�����������д", Toast.LENGTH_LONG).show();
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
	 * @TODO ��������
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
	 * @TODO ���ڿؼ������ࣨͬ��������е����ݣ�
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
	 *            ���ͼ��ImageView
	 * 
	 * @TODO �������
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
