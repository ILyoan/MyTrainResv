package com.ilyoan.mytrainresv;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.ilyoan.mytrainresv.core.MyTrainResv;
import com.ilyoan.mytrainresv.core.Station;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class SearchFragment extends Fragment {
    private static final String PREFERENCE = "MyTrainResvPref";
    private static final String PREFERENCE_STATION_FROM = "StationFrom";
    private static final String PREFERENCE_STATION_TO = "StationTo";
    private static final String PREFERENCE_DATE = "DAte";
    private static final String PREFERENCE_TIME_FROM = "TimeFrom";
    private static final String PREFERENCE_TIME_TO = "TimeTo";
    private static final String PREFERENCE_FIRST_CLASS = "FirstClass";
    private static final String PREFERENCE_KTX_ONLY = "KtxOnly";

    private View view = null;

    EditText stationFrom = null;
    EditText stationTo = null;
    Spinner date = null;
    Spinner timeFrom = null;
    Spinner timeTo = null;
    CheckBox firstClass = null;
    CheckBox ktxOnly = null;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_search, container, false);

        this.stationFrom = (EditText)this.view.findViewById(R.id.editText_station_from);
        this.stationTo = (EditText)this.view.findViewById(R.id.editText_station_to);
        this.date = (Spinner)this.view.findViewById(R.id.spinner_date);
        this.timeFrom = (Spinner)this.view.findViewById(R.id.spinner_time_from);
        this.timeTo = (Spinner)this.view.findViewById(R.id.spinner_time_to);
        this.firstClass = (CheckBox)this.view.findViewById(R.id.checkBox_first_class);
        this.ktxOnly = (CheckBox)this.view.findViewById(R.id.checkBox_ktx_only);

        initDateSpinner(this.date);
        initTimeSpinner(this.timeFrom);
        initTimeSpinner(this.timeTo);

        // Set widget value according to the preferences.
        fromPreference();

        // ok button
        this.view.findViewById(R.id.button_train_ok).setOnClickListener(this.onSearchOk);

        return this.view;
    }


    private void initDateSpinner(Spinner spinner) {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd(E)");
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
        SharedPreferences pref = getActivity().getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);

        // station from
        this.stationFrom.setText(pref.getString(PREFERENCE_STATION_FROM, "서울"));
        // station to
        this.stationTo.setText(pref.getString(PREFERENCE_STATION_TO, "부산"));
        // date
        this.date.setSelection(pref.getInt(PREFERENCE_DATE, 0));
        // time from
        this.timeFrom.setSelection(pref.getInt(PREFERENCE_TIME_FROM, 0));
        // time to
        this.timeTo.setSelection(pref.getInt(PREFERENCE_TIME_TO, 0));
        // first class
        this.firstClass.setChecked(pref.getBoolean(PREFERENCE_FIRST_CLASS, false));
        // ktx only
        this.ktxOnly.setChecked(pref.getBoolean(PREFERENCE_KTX_ONLY, true));
    }

    public void toPreference() {
        // preferences
        SharedPreferences pref = getActivity().getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        // station from
        editor.putString(PREFERENCE_STATION_FROM, getStationFrom());
        // station to
        editor.putString(PREFERENCE_STATION_TO, getStationTo());
        // date
        editor.putInt(PREFERENCE_DATE, this.date.getSelectedItemPosition());
        // time from
        editor.putInt(PREFERENCE_TIME_FROM, this.timeFrom.getSelectedItemPosition());
        // time to
        editor.putInt(PREFERENCE_TIME_TO, this.timeTo.getSelectedItemPosition());
        // first class
        editor.putBoolean(PREFERENCE_FIRST_CLASS, this.firstClass.isChecked());
        // ktx only
        editor.putBoolean(PREFERENCE_KTX_ONLY, this.ktxOnly.isChecked());

        // commit
        editor.commit();
    }

    Button.OnClickListener onSearchOk = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            toPreference();

            MyTrainResv myTrainResv = MyTrainResv.getInstance();
            myTrainResv.searchTrain(
                    getStationFrom(),
                    getStationTo(),
                    getDate(),
                    getTimeFrom(),
                    getTimeTo(),
                    getFirstClass(),
                    getKtxOnly());
        }
    };

    public String getStationFrom() {
        return (String)this.stationFrom.getText().toString();
    }

    public String getStationTo() {
        return (String)this.stationTo.getText().toString();
    }

    public String getDate() {
        return ((String)this.date.getSelectedItem()).replaceAll("-", "").substring(0, 8);
    }

    public String getTimeFrom() {
        return ((String)this.timeFrom.getSelectedItem()).replaceAll(":", "") + "00";
    }

    public String getTimeTo() {
        return ((String)this.timeTo.getSelectedItem()).replaceAll(":", "") + "00";
    }

    public boolean getFirstClass() {
        return this.firstClass.isChecked();
    }

    public boolean getKtxOnly() {
        return this.ktxOnly.isChecked();
    }

}
