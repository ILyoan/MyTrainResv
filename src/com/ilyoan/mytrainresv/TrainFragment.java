package com.ilyoan.mytrainresv;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.ilyoan.mytrainresv.core.Station;

public class TrainFragment extends Fragment {
	private static final String PREF = "MyTrainResvPref";
	private static final String PREF_STATION_FROM = "StationFrom";
	private static final String PREF_STATION_TO = "StationTo";
	private static final String PREF_DATE = "DAte";
	private static final String PREF_TIME_FROM = "TimeFrom";
	private static final String PREF_TIME_TO = "TimeTo";
	private static final String PREF_FIRST_CLASS = "FirstClass";
	private static final String PREF_KTX_ONLY = "KtxOnly";

	private View view = null;

	public String getStationFrom() {
		return (String)((Spinner)this.view.findViewById(R.id.spinner_station_from))
				.getSelectedItem();
	}

	public String getStationTo() {
		return (String)((Spinner)this.view.findViewById(R.id.spinner_station_to))
				.getSelectedItem();
	}

	public String getDate() {
		return ((String)((Spinner)this.view.findViewById(R.id.spinner_date))
				.getSelectedItem())
				.replaceAll("-", "");
	}

	public String getTimeFrom() {
		return ((String)((Spinner)this.view.findViewById(R.id.spinner_time_from))
				.getSelectedItem())
				.replaceAll(":", "") + "00";
	}

	public String getTimeTo() {
		return ((String)((Spinner)this.view.findViewById(R.id.spinner_time_to))
				.getSelectedItem())
				.replaceAll(":", "") + "00";
	}

	public boolean getFirstClass() {
		return ((CheckBox)this.view.findViewById(R.id.checkBox_first_class)).isChecked();
	}

	public boolean getKtxOnly() {
		return ((CheckBox)this.view.findViewById(R.id.checkBox_ktx_only)).isChecked();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.view = inflater.inflate(R.layout.layout_train, container, false);
		initWidgets();
		return this.view;
	}

	private void initWidgets() {
		initStationSpinner((Spinner)this.view.findViewById(R.id.spinner_station_from));
		initStationSpinner((Spinner)this.view.findViewById(R.id.spinner_station_to));
		initDateSpinner((Spinner)this.view.findViewById(R.id.spinner_date));
		initTimeSpinner((Spinner)this.view.findViewById(R.id.spinner_time_from));
		initTimeSpinner((Spinner)this.view.findViewById(R.id.spinner_time_to));

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
				getActivity(), android.R.layout.simple_spinner_item, data);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}

	public void fromPreference() {
		// preferences
		SharedPreferences pref = getActivity().getSharedPreferences(PREF, Context.MODE_PRIVATE);
		int prefStationFrom = pref.getInt(PREF_STATION_FROM, 0);
		int prefStationTo = pref.getInt(PREF_STATION_TO, 0);
		int prefDate = pref.getInt(PREF_DATE, 0);
		int prefTimeFrom = pref.getInt(PREF_TIME_FROM, 0);
		int prefTimeTo = pref.getInt(PREF_TIME_TO, 0);
		boolean prefFirstClass = pref.getBoolean(PREF_FIRST_CLASS, false);
		boolean prefKtxOnly = pref.getBoolean(PREF_KTX_ONLY, true);

		// station from
		Spinner stationFrom = (Spinner)this.view.findViewById(R.id.spinner_station_from);
		stationFrom.setSelection(prefStationFrom);
		// station to
		Spinner stationTo = (Spinner)this.view.findViewById(R.id.spinner_station_to);
		stationTo.setSelection(prefStationTo);
		// date
		Spinner date = (Spinner)this.view.findViewById(R.id.spinner_date);
		date.setSelection(prefDate);
		// time from
		Spinner timeFrom = (Spinner)this.view.findViewById(R.id.spinner_time_from);
		timeFrom.setSelection(prefTimeFrom);
		// time to
		Spinner timeTo = (Spinner)this.view.findViewById(R.id.spinner_time_to);
		timeTo.setSelection(prefTimeTo);
		// first class
		CheckBox firstClass = (CheckBox)this.view.findViewById(R.id.checkBox_first_class);
		firstClass.setChecked(prefFirstClass);
		// ktx only
		CheckBox ktxOnly = (CheckBox)this.view.findViewById(R.id.checkBox_ktx_only);
		ktxOnly.setChecked(prefKtxOnly);
	}

	public void toPreference() {
		// preferences
		SharedPreferences pref = getActivity().getSharedPreferences(PREF, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();

		// station from
		Spinner stationFrom = (Spinner)this.view.findViewById(R.id.spinner_station_from);
		editor.putInt(PREF_STATION_FROM, stationFrom.getSelectedItemPosition());
		// station to
		Spinner stationTo = (Spinner)this.view.findViewById(R.id.spinner_station_to);
		editor.putInt(PREF_STATION_TO, stationTo.getSelectedItemPosition());
		// date
		Spinner date = (Spinner)this.view.findViewById(R.id.spinner_date);
		editor.putInt(PREF_DATE, date.getSelectedItemPosition());
		// time from
		Spinner timeFrom = (Spinner)this.view.findViewById(R.id.spinner_time_from);
		editor.putInt(PREF_TIME_FROM, timeFrom.getSelectedItemPosition());
		// time to
		Spinner timeTo = (Spinner)this.view.findViewById(R.id.spinner_time_to);
		editor.putInt(PREF_TIME_TO, timeTo.getSelectedItemPosition());
		// first class
		CheckBox firstClass = (CheckBox)this.view.findViewById(R.id.checkBox_first_class);
		editor.putBoolean(PREF_FIRST_CLASS, firstClass.isChecked());
		// ktx only
		CheckBox ktxOnly = (CheckBox)this.view.findViewById(R.id.checkBox_ktx_only);
		editor.putBoolean(PREF_KTX_ONLY, ktxOnly.isChecked());

		// commit
		editor.commit();
	}
}
