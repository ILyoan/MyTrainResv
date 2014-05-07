package com.ilyoan.mytrainresv.core;

public class MyTrainResv {
	private MyTrainResv instance = null;
	
	private MyTrainResv() { }
	
	public MyTrainResv getInstance() {
		if (instance == null) {
			instance = new MyTrainResv();
		}
		return instance;
	}
}
