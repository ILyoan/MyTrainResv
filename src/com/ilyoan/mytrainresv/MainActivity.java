package com.ilyoan.mytrainresv;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.ilyoan.mytrainresv.core.MyTrainResv;
import com.ilyoan.mytrainresv.core.Station;
import com.ilyoan.mytrainresv.core.Test;

public class MainActivity extends Activity {
	private static final String PREF = "MyTrainResvPref";
	private static final String PREF_ID = "ID";
	private static final String PREF_PW = "PW";
	private static final String PREF_STATION_FROM = "StationFrom";
	private static final String PREF_STATION_TO = "StationTo";
	private static final String PREF_DATE = "DAte";
	private static final String PREF_TIME_FROM = "TimeFrom";
	private static final String PREF_TIME_TO = "TimeTo";
	private static final String PREF_FIRST_CLASS = "FirstClass";
	private static final String PREF_KTX_ONLY = "KtxOnly";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Initialize widgets.
		initWidgets();

		// Initialize MyTrainResv
		MyTrainResv.setActivity(this);

		// Set test button click handler.
		findViewById(R.id.button_test).setOnClickListener(onTestClick);
	}

	private void initWidgets() {
		initStationSpinner((Spinner)findViewById(R.id.spinner_station_from));
		initStationSpinner((Spinner)findViewById(R.id.spinner_station_to));
		initDateSpinner((Spinner)findViewById(R.id.spinner_date));
		initTimeSpinner((Spinner)findViewById(R.id.spinner_time_from));
		initTimeSpinner((Spinner)findViewById(R.id.spinner_time_to));

		// Set widget value according to the preferences.
		fromPreference();
	}

	private void initStationSpinner(Spinner spinner) {
		initSpinner(spinner, Station.getInstance().names());
	}

	private void initDateSpinner(Spinner spinner) {
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		ArrayList<String> dates = new ArrayList<String>();
		for (int i = 0; i < 30; ++i) {
			dates.add(dateFormat.format(date));
			date = new Date(date.getTime() + 1000 * 24 * 60 * 60);
		}
		initSpinner(spinner, dates);
	}


	private void initTimeSpinner(Spinner spinner) {
		ArrayList<String> times = new ArrayList<String>();
		for (int h = 0; h < 24; ++h) {
			times.add(String.format("%02d:00", h));
			times.add(String.format("%02d:30", h));
		}
		initSpinner(spinner, times);
	}

	private void initSpinner(Spinner spinner, ArrayList<String> data) {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item, data);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}

	private void fromPreference() {
		// preferences
		SharedPreferences pref = getSharedPreferences(PREF, Activity.MODE_PRIVATE);
		String prefId = pref.getString(PREF_ID, "");
		String prefPw = pref.getString(PREF_PW, "");
		int prefStationFrom = pref.getInt(PREF_STATION_FROM, 0);
		int prefStationTo = pref.getInt(PREF_STATION_TO, 0);
		int prefDate = pref.getInt(PREF_DATE, 0);
		int prefTimeFrom = pref.getInt(PREF_TIME_FROM, 0);
		int prefTimeTo = pref.getInt(PREF_TIME_TO, 0);
		boolean prefFirstClass = pref.getBoolean(PREF_FIRST_CLASS, false);
		boolean prefKtxOnly = pref.getBoolean(PREF_KTX_ONLY, true);

		// user id
		EditText editTextId = (EditText)findViewById(R.id.editText_id);
		editTextId.setText(prefId);
		// password
		EditText editTextPw = (EditText)findViewById(R.id.editText_pw);
		editTextPw.setText(prefPw);
		// station from
		Spinner stationFrom = (Spinner)findViewById(R.id.spinner_station_from);
		stationFrom.setSelection(prefStationFrom);
		// station to
		Spinner stationTo = (Spinner)findViewById(R.id.spinner_station_to);
		stationTo.setSelection(prefStationTo);
		// date
		Spinner date = (Spinner)findViewById(R.id.spinner_date);
		date.setSelection(prefDate);
		// time from
		Spinner timeFrom = (Spinner)findViewById(R.id.spinner_time_from);
		timeFrom.setSelection(prefTimeFrom);
		// time to
		Spinner timeTo = (Spinner)findViewById(R.id.spinner_time_to);
		timeTo.setSelection(prefTimeTo);
		// first class
		CheckBox firstClass = (CheckBox)findViewById(R.id.checkBox_first_class);
		firstClass.setChecked(prefFirstClass);
		// ktx only
		CheckBox ktxOnly = (CheckBox)findViewById(R.id.checkBox_ktx_only);
		ktxOnly.setChecked(prefKtxOnly);
	}

	private void toPreference() {
		// preferences
		SharedPreferences pref = getSharedPreferences(PREF, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();

		// user id
		editor.putString(PREF_ID, getId());
		// password
		editor.putString(PREF_PW, getPassword());
		// station from
		Spinner stationFrom = (Spinner)findViewById(R.id.spinner_station_from);
		editor.putInt(PREF_STATION_FROM, stationFrom.getSelectedItemPosition());
		// station to
		Spinner stationTo = (Spinner)findViewById(R.id.spinner_station_to);
		editor.putInt(PREF_STATION_TO, stationTo.getSelectedItemPosition());
		// date
		Spinner date = (Spinner)findViewById(R.id.spinner_date);
		editor.putInt(PREF_DATE, date.getSelectedItemPosition());
		// time from
		Spinner timeFrom = (Spinner)findViewById(R.id.spinner_time_from);
		editor.putInt(PREF_TIME_FROM, timeFrom.getSelectedItemPosition());
		// time to
		Spinner timeTo = (Spinner)findViewById(R.id.spinner_time_to);
		editor.putInt(PREF_TIME_TO, timeTo.getSelectedItemPosition());
		// first class
		CheckBox firstClass = (CheckBox)findViewById(R.id.checkBox_first_class);
		editor.putBoolean(PREF_FIRST_CLASS, firstClass.isChecked());
		// ktx only
		CheckBox ktxOnly = (CheckBox)findViewById(R.id.checkBox_ktx_only);
		editor.putBoolean(PREF_KTX_ONLY, ktxOnly.isChecked());

		// commit
		editor.commit();
	}

	private String getId() {
		return ((EditText)findViewById(R.id.editText_id)).getText().toString();
	}

	private String getPassword() {
		return ((EditText)findViewById(R.id.editText_pw)).getText().toString();
	}

	private String getStationFrom() {
		return (String)((Spinner)findViewById(R.id.spinner_station_from)).getSelectedItem();
	}

	private String getStationTo() {
		return (String)((Spinner)findViewById(R.id.spinner_station_to)).getSelectedItem();
	}

	private String getDate() {
		return ((String)((Spinner)findViewById(R.id.spinner_date)).getSelectedItem())
				.replaceAll("-", "");
	}

	private String getTimeFrom() {
		return ((String)((Spinner)findViewById(R.id.spinner_time_from)).getSelectedItem())
				.replaceAll(":", "") + "00";
	}

	private String getTimeTo() {
		return ((String)((Spinner)findViewById(R.id.spinner_time_to)).getSelectedItem())
				.replaceAll(":", "") + "00";
	}

	private boolean getFirstClass() {
		return ((CheckBox)findViewById(R.id.checkBox_first_class)).isChecked();
	}

	private boolean getKtxOnly() {
		return ((CheckBox)findViewById(R.id.checkBox_ktx_only)).isChecked();
	}

	Button.OnClickListener onTestClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			toPreference();

			Test test = new Test();
			test.login(getId(), getPassword());
			test.searchTrain(getStationFrom(),
					getStationTo(),
					getDate(),
					getTimeFrom(),
					getTimeTo(),
					getFirstClass(),
					getKtxOnly());
		}
	};
}

