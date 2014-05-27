package com.ilyoan.mytrainresv.core;

import java.util.ArrayList;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

public class MyTrainResv {
	private static final String TAG = "MyTrainResv";

	private static MyTrainResv instance = null;
	private static Activity activity = null;

	private final MyHttp http = new MyHttp();
	private ArrayList<Train> trains = new ArrayList<Train>();

	private OnNotification noti = null;

	private MyTrainResv() { }

	public static MyTrainResv getInstance() {
		if (instance == null) {
			instance = new MyTrainResv();
		}
		return instance;
	}

	public static void setActivity(Activity activity) {
		MyTrainResv.activity = activity;
	}

	public void login(String id, String pw) {
		Log.d(TAG, "MyTrainResv.login(" + id + ")");
		this.http.login(id, pw);
	}

	public void searchTrain(String stationFrom,
			String stationTo,
			String date,
			String timeFrom,
			String timeTo,
			boolean firstClass,
			boolean ktxOnly) {
		Log.d(TAG, "MyTrainResv.searchTrain()");
		Station station = Station.getInstance();
		this.http.searchTrain(
				station.getCode(stationFrom),
				station.getCode(stationTo),
				date,
				timeFrom,
				ktxOnly);
	}

	public void resvTrain(ArrayList<Train> trainList) {
		Log.d(TAG, "MyTrainResv.resvTrain()");
		for (Train train : trainList) {
			this.http.resv(train);
		}
	}

	public void initTrainList() {
		this.trains = new ArrayList<Train>();
	}

	public void addTrain(Train train) {
		this.trains.add(train);
	}

	public final ArrayList<Train> getTrainList() {
		return this.trains;
	}

	public static void showToast(String message) {
		Toast toast = Toast.makeText(
				MyTrainResv.activity.getApplicationContext(),
				message,
				Toast.LENGTH_LONG);
		toast.show();
	}

	public interface OnNotification {
		public void onTrainList(MyTrainResv myTrain);
	}

	public void setNotification(OnNotification noti) {
		this.noti = noti;
	}

	public void onTrainList() {
		if (this.noti != null) {
			this.noti.onTrainList(this);
		}
	}
}
