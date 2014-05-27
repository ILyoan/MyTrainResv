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

	private OnNotification noti = null;

	private TrainSearchOption lastSearchOption = null;

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
		this.lastSearchOption = new TrainSearchOption(
				station.getCode(stationFrom),
				station.getCode(stationTo),
				date,
				timeFrom,
				timeTo,
				firstClass,
				ktxOnly);

		this.http.setOnSearchTrainCallback(this.onSearchTrainCallback);
		this.http.searchTrain(
				station.getCode(stationFrom),
				station.getCode(stationTo),
				date,
				timeFrom,
				ktxOnly);
	}

	private final MyHttp.OnSearchTrainCallback onSearchTrainCallback = new MyHttp.OnSearchTrainCallback() {
		@Override
		public void onResult(ArrayList<Train> trainList, String error) {
			if (error != null) {
				showToast(error);
			} else if (MyTrainResv.this.noti != null) {
				MyTrainResv.this.noti.onTrainList(trainList);
			}
		}

	};

	public void resvTrain(ArrayList<Train> trainList) {
		if (this.lastSearchOption == null) {
			return;
		}

		Log.d(TAG, "MyTrainResv.resvTrain()");

		this.http.setOnSearchTrainCallback(this.onSearchTrainForResvCallback);
		this.http.searchTrain(
				this.lastSearchOption.stationFrom,
				this.lastSearchOption.stationTo,
				this.lastSearchOption.date,
				this.lastSearchOption.timeFrom,
				this.lastSearchOption.ktxOnly);
	}

	private final MyHttp.OnSearchTrainCallback onSearchTrainForResvCallback = new MyHttp.OnSearchTrainCallback() {
		@Override
		public void onResult(ArrayList<Train> trainList, String error) {
			if (error != null) {
				showToast(error);
			} else {
				showToast("do resv");
			}
		}

	};

	public static void showToast(String message) {
		Toast toast = Toast.makeText(
				MyTrainResv.activity.getApplicationContext(),
				message,
				Toast.LENGTH_LONG);
		toast.show();
	}

	public interface OnNotification {
		public void onTrainList(ArrayList<Train> trainList);
	}

	public void setNotification(OnNotification noti) {
		this.noti = noti;
	}

	public class TrainSearchOption {
		public TrainSearchOption(
				String stationFrom,
				String stationTo,
				String date,
				String timeFrom,
				String timeTo,
				boolean firstClass,
				boolean ktxOnly
				) {
			this.stationFrom = stationFrom;
			this.stationTo = stationTo;
			this.date = date;
			this.timeFrom = timeFrom;
			this.timeTo = timeTo;
			this.firstClass = firstClass;
			this.ktxOnly = ktxOnly;
		}
		public String stationFrom;
		public String stationTo;
		public String date;
		public String timeFrom;
		public String timeTo;
		public boolean firstClass;
		public boolean ktxOnly;
	}
}
