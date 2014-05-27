package com.ilyoan.mytrainresv;

import com.ilyoan.mytrainresv.core.MyTrainResv;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class LoginFragment extends Fragment {
	private static final String PREF = "MyTrainResvPref";
	private static final String PREF_ID = "ID";
	private static final String PREF_PW = "PW";

	private EditText editTextId = null;
	private EditText editTextPw = null;

	private View view = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.view = inflater.inflate(R.layout.layout_login, container, false);
		initWidgets();
		return this.view;
	}

	private void initWidgets() {
		this.editTextId = (EditText)this.view.findViewById(R.id.editText_id);
		this.editTextPw = (EditText)this.view.findViewById(R.id.editText_pw);

		// Set widget value according to the preferences.
		fromPreference();

		// ok button
		this.view.findViewById(R.id.button_login_ok).setOnClickListener(this.onLoginOk);
	}

	public void fromPreference() {
		// preferences
		SharedPreferences pref = getActivity().getSharedPreferences(PREF, Context.MODE_PRIVATE);
		// user id
		this.editTextId.setText(pref.getString(PREF_ID, ""));
		// password
		this.editTextPw.setText(pref.getString(PREF_PW, ""));
	}

	public void toPreference() {
		// preferences
		SharedPreferences pref = getActivity().getSharedPreferences(PREF, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();

		// user id
		editor.putString(PREF_ID, this.editTextId.getText().toString());
		// password
		editor.putString(PREF_PW, this.editTextPw.getText().toString());

		// commit
		editor.commit();
	}

	Button.OnClickListener onLoginOk = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			toPreference();

			MyTrainResv myTrainResv = MyTrainResv.getInstance();
			myTrainResv.login(
					LoginFragment.this.editTextId.getText().toString(),
					LoginFragment.this.editTextPw.getText().toString());
		}
	};
}
