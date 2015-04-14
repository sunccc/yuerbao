package com.zjkj.yuerbao.login;

import java.util.Calendar;

import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;

import com.zjkj.yuerbao.R;
import com.zjkj.yuerbao.main.MainActivity;

public class RegisterActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		
		final RadioButton r1=(RadioButton)findViewById(R.id.ycq_radiobtn);
		final RadioButton r2=(RadioButton)findViewById(R.id.bbsr_radiobtn);
		r1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				
				if(isChecked)
				{
					r2.setChecked(false);
				}else{
					r2.setChecked(true);
				}
				
			}
		});
		
		r2.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				
				if(isChecked)
				{
					r1.setChecked(false);
				}else{
					r1.setChecked(true);
				}
				
			}
		});
		
		
		final EditText rq = (EditText) findViewById(R.id.rq);
		InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);       
	     imm.hideSoftInputFromWindow(rq.getWindowToken(), 0);
		final Calendar cd = Calendar.getInstance();
		Date date = new Date();
		cd.setTime(date);

		rq.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub
				DatePickerDialog dpd = new DatePickerDialog(
						RegisterActivity.this, new OnDateSetListener() {
							public void onDateSet(DatePicker view, int year,
									int monthOfYear, int dayOfMonth) {
								rq.setText(year + "-" + monthOfYear
										+ "-" + dayOfMonth);
							}
						}, cd.get(Calendar.YEAR), cd.get(Calendar.MONTH), cd
								.get(Calendar.DAY_OF_MONTH));

				dpd.show();
			}
		});
		rq.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				DatePickerDialog dpd = new DatePickerDialog(
						RegisterActivity.this, new OnDateSetListener() {
							public void onDateSet(DatePicker view, int year,
									int monthOfYear, int dayOfMonth) {
								rq.setText(year + "-" + monthOfYear
										+ "-" + dayOfMonth);
							}
						}, cd.get(Calendar.YEAR), cd.get(Calendar.MONTH), cd
								.get(Calendar.DAY_OF_MONTH));

				dpd.show();
			}
		});
		
		Button ok = (Button) findViewById(R.id.ok);
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
}