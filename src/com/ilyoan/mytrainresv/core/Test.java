package com.ilyoan.mytrainresv.core;

import android.util.Log;

public class Test {
	private static final String TAG = "MyTrainResv";
	
	public void login(String id, String password) {
		MyTrainResv myTrainResv = MyTrainResv.getInstance();
		myTrainResv.login(id, password);
	}
}
