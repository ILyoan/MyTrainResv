package com.ilyoan.mytrainresv;

import com.ilyoan.mytrainresv.R;
import com.ilyoan.mytrainresv.core.MyTrainResv;
import com.ilyoan.mytrainresv.core.Test;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
	private static final String PREF = "MyTrainResvPref";
	private static final String PREF_ID = "ID";
	private static final String PREF_PW = "PW";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Initialize widgets from preferences.
        fromPreference();
        
        // Initialize MyTrainResv
        MyTrainResv.setActivity(this);
        
        // Set test button click handler.
        findViewById(R.id.button_test).setOnClickListener(onTestClick);
    }
    
    private void fromPreference() {
    	// preferences
        SharedPreferences pref = getSharedPreferences(PREF, Activity.MODE_PRIVATE);
        String prefId = pref.getString(PREF_ID, "");
        String prefPw = pref.getString(PREF_PW, "");
        
        // user id
        EditText editTextId = (EditText)findViewById(R.id.editText_id);
        editTextId.setText(prefId);
        
        // password
        EditText editTextPw = (EditText)findViewById(R.id.editText_pw);
        editTextPw.setText(prefPw);
    }
    
    private void toPreference() {
    	// preferences
        SharedPreferences pref = getSharedPreferences(PREF, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        
        // user id
        editor.putString(PREF_ID, getId());        
        // password
        editor.putString(PREF_PW, getPassword());
        
        // commit
        editor.commit();
    }
    
    private String getId() {
    	EditText editTextId = (EditText)findViewById(R.id.editText_id);
    	return editTextId.getText().toString();
    }
    
    private String getPassword() {
    	EditText editTextPw = (EditText)findViewById(R.id.editText_pw);
    	return editTextPw.getText().toString();
    }
    
    Button.OnClickListener onTestClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			toPreference();
			
			String id = getId();
			String password = getPassword();
			
			Test test = new Test();
			test.login(id, password);
		}
    };
}
