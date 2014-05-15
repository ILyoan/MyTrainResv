package com.ilyoan.mytrainresv;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class LoginFragment extends Fragment {
	private static final String PREF = "MyTrainResvPref";
	private static final String PREF_ID = "ID";
	private static final String PREF_PW = "PW";

	private View view = null;

	public String getLoginId() {
		return ((EditText)this.view.findViewById(R.id.editText_id)).getText().toString();
	}

	public String getLoginPwd() {
		return ((EditText)this.view.findViewById(R.id.editText_pw)).getText().toString();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.view = inflater.inflate(R.layout.layout_login, container, false);
		initWidgets();
		return this.view;
	}

	private void initWidgets() {
		// Set widget value according to the preferences.
		fromPreference();
	}

	public void fromPreference() {
		// preferences
		SharedPreferences pref = getActivity().getSharedPreferences(PREF, Context.MODE_PRIVATE);
		String prefId = pref.getString(PREF_ID, "");
		String prefPw = pref.getString(PREF_PW, "");

		// user id
		EditText editTextId = (EditText)this.view.findViewById(R.id.editText_id);
		editTextId.setText(prefId);
		// password
		EditText editTextPw = (EditText)this.view.findViewById(R.id.editText_pw);
		editTextPw.setText(prefPw);
	}

	public void toPreference() {
		// preferences
		SharedPreferences pref = getActivity().getSharedPreferences(PREF, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();

		// user id
		editor.putString(PREF_ID, getLoginId());
		// password
		editor.putString(PREF_PW, getLoginPwd());
	}
}
