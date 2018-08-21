package com.example.androidpopupwindow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.app.Activity;

public class MainActivity extends Activity {
	
	String[] DayOfWeek = {"Sunday", "Monday", "Tuesday", 
			"Wednesday", "Thursday", "Friday", "Saturday"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final Button btnOpenPopup = (Button) findViewById(R.id.openpopup);
		btnOpenPopup.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				LayoutInflater layoutInflater = 
						(LayoutInflater)getBaseContext()
						.getSystemService(LAYOUT_INFLATER_SERVICE);
				View popupView = layoutInflater.inflate(R.layout.popup, null);
				final PopupWindow popupWindow = new PopupWindow(
						popupView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				
				Button btnDismiss = (Button)popupView.findViewById(R.id.dismiss);
				
				Spinner popupSpinner = (Spinner)popupView.findViewById(R.id.popupspinner);
				
				ArrayAdapter<String> adapter = 
						new ArrayAdapter<String>(MainActivity.this, 
								android.R.layout.simple_spinner_item, DayOfWeek);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				popupSpinner.setAdapter(adapter);
				
				btnDismiss.setOnClickListener(new Button.OnClickListener(){

					@Override
					public void onClick(View v) {
						popupWindow.dismiss();
					}});
				
				popupWindow.showAsDropDown(btnOpenPopup, 50, -30);
				
				popupView.setOnTouchListener(new OnTouchListener() {
					int orgX, orgY;
					int offsetX, offsetY;

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:
							orgX = (int) event.getX();
							orgY = (int) event.getY();
							break;
						case MotionEvent.ACTION_MOVE:
							offsetX = (int)event.getRawX() - orgX;
							offsetY = (int)event.getRawY() - orgY;
							popupWindow.update(offsetX, offsetY, -1, -1, true);
							break;
						}
						return true;
					}});
			}

		});
	}

}
