package com.ilyoan.mytrainresv.core;

public class MyTrainResv {
	private static MyTrainResv instance = null;
	
	private MyTrainResv() { }
	
	public static MyTrainResv getInstance() {
		if (instance == null) {
			instance = new MyTrainResv();
		}
		return instance;
	}
}
