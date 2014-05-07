package com.ilyoan.mytrainresv;

import com.ilyoan.mytrainresv.R;
import com.ilyoan.mytrainresv.core.Test;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        findViewById(R.id.button_test).setOnClickListener(onTestClick);
    }
    
    Button.OnClickListener onTestClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Test test = new Test();
			test.test();
		}
    };
}
