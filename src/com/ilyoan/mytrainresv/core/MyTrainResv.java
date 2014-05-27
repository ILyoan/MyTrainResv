package com.ilyoan.mytrainresv.core;

import java.util.ArrayList;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

public class MyTrainResv {
	private static final String TAG = "MyTrainResv";

	// Singleton object.
	private static MyTrainResv instance = null;

	// Activity.
	private static Activity activity = null;

	// MyHttp object.
	private final MyHttp http = new MyHttp();

	// Notification handler.
	private OnNotification noti = null;

	// Last used searching option for train list.
	private TrainSearchOption lastSearchOption = null;

	// Prevent creating an object of this class as this is singleton.
	private MyTrainResv() { }

	// Get the singleton object.
	public static MyTrainResv getInstance() {
		if (instance == null) {
			instance = new MyTrainResv();
		}
		return instance;
	}

	// Set activity object.
	public static void setActivity(Activity activity) {
		MyTrainResv.activity = activity;
	}

	// Login to server.
	public void login(String id, String pw) {
		Log.d(TAG, "MyTrainResv.login(" + id + ")");
		this.http.login(id, pw);
	}

	// Search train list.
	public void searchTrain(String stationFrom,
			String stationTo,
			String date,
			String timeFrom,
			String timeTo,
			boolean firstClass,
			boolean ktxOnly) {
		Log.d(TAG, "MyTrainResv.searchTrain()");

		// Update last used search option.
		// This option will be used on resvTrain() function.
		Station station = Station.getInstance();
		this.lastSearchOption = new TrainSearchOption(
				station.getCode(stationFrom),
				station.getCode(stationTo),
				date,
				timeFrom,
				timeTo,
				firstClass,
				ktxOnly);

		// Set handler.
		this.http.setOnSearchTrainCallback(this.onSearchTrainCallback);
		// Do search.
		this.http.searchTrain(
				station.getCode(stationFrom),
				station.getCode(stationTo),
				date,
				timeFrom,
				ktxOnly);
	}

	// Train list result handler.
	private final MyHttp.OnSearchTrainCallback onSearchTrainCallback = new MyHttp.OnSearchTrainCallback() {
		@Override
		public void onResult(ArrayList<Train> trainList, String error) {
			if (error != null) {
				// If there was an error, show toast message.
				showToast(error);
			} else if (MyTrainResv.this.noti != null) {
				// Give notification via noti handler.
				MyTrainResv.this.noti.onTrainList(trainList);
			}
		}

	};

	// Reserve train
	public void resvTrain(ArrayList<Train> trainList) {
		// Trains should have been search at least once.
		if (this.lastSearchOption == null) {
			return;
		}

		Log.d(TAG, "MyTrainResv.resvTrain()");

		// Register train list handler.
		this.http.setOnSearchTrainCallback(this.onSearchTrainForResvCallback);
		// Search trains.
		this.http.searchTrain(
				this.lastSearchOption.stationFrom,
				this.lastSearchOption.stationTo,
				this.lastSearchOption.date,
				this.lastSearchOption.timeFrom,
				this.lastSearchOption.ktxOnly);
	}

	// Train list are obtained, see if there is a train available.
	private final MyHttp.OnSearchTrainCallback onSearchTrainForResvCallback = new MyHttp.OnSearchTrainCallback() {
		@Override
		public void onResult(ArrayList<Train> trainList, String error) {
			if (error != null) {
				// If there was an error show toast message.
				showToast(error);
			} else {
				// Do reservation work.
				showToast("do resv");
			}
		}

	};

	// Show toast message.
	public static void showToast(String message) {
		Toast toast = Toast.makeText(
				MyTrainResv.activity.getApplicationContext(),
				message,
				Toast.LENGTH_LONG);
		toast.show();
	}

	// Interface for notifying to the main activity or wrapper system.
	public interface OnNotification {
		public void onTrainList(ArrayList<Train> trainList);
	}

	// Register notification.
	public void setNotification(OnNotification noti) {
		this.noti = noti;
	}

	// Train searcing option.
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
