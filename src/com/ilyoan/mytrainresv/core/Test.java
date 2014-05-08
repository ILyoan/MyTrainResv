package com.ilyoan.mytrainresv.core;

public class Test {
	public void login(String id, String password) {
		MyTrainResv myTrainResv = MyTrainResv.getInstance();
		myTrainResv.login(id, password);
	}
	
	public void searchTrain(String stationFrom,
							String stationTo,
							String date,
							String timeFrom,
							String timeTo,
							boolean firstClass,
							boolean ktxOnly) {
		MyTrainResv myTrainResv = MyTrainResv.getInstance();
		myTrainResv.searchTrain(stationFrom, stationTo, date, timeFrom, timeTo, firstClass, ktxOnly);
	}
}
