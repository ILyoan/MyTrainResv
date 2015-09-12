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
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;

import com.ilyoan.mytrainresv.core.MyTrainResv;


public class LoginFragment extends Fragment {
    private static final String PREFERENCE = "MyTrainResvPref";
    private static final String PREFERENCE_ID = "ID";
    private static final String PREFERENCE_PW = "PW";

    private EditText mEditTextId = null;
    private EditText mEditTextPw = null;

    private View mView = null;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_login, container, false);

        mEditTextId = (EditText)mView.findViewById(R.id.editText_id);
        mEditTextPw = (EditText)mView.findViewById(R.id.editText_pw);

        fromPreference();

        mView.findViewById(R.id.button_login_ok).setOnClickListener(onLoginOk);

        return mView;
    }

    public void fromPreference() {
        SharedPreferences pref = getActivity().getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);

        mEditTextId.setText(pref.getString(PREFERENCE_ID, ""));
        mEditTextPw.setText(pref.getString(PREFERENCE_PW, ""));
    }

    public void toPreference() {
        SharedPreferences pref = getActivity().getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString(PREFERENCE_ID, mEditTextId.getText().toString());
        editor.putString(PREFERENCE_PW, mEditTextPw.getText().toString());

        editor.commit();
    }


    Button.OnClickListener onLoginOk = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            toPreference();

            MyTrainResv myTrainResv = MyTrainResv.getInstance();
            myTrainResv.login(
                    LoginFragment.this.mEditTextId.getText().toString(),
                    LoginFragment.this.mEditTextPw.getText().toString()
            );
        }
    };

}
