package com.ilyoan.mytrainresv.core;

import java.util.ArrayList;
import java.util.HashSet;

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

	// Last used searching Param for train list.
	private TrainSearchParam lastSearchParam = null;

	private TrainResvWorker resvWorker = null;

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

		// Update last used search Param.
		// This Param will be used on resvTrain() function.
		Station station = Station.getInstance();
		this.lastSearchParam = new TrainSearchParam(
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
		if (this.lastSearchParam == null) {
			return;
		}

		Log.d(TAG, "MyTrainResv.resvTrain()");

		if (this.resvWorker != null) {
			this.resvWorker.stopWorking();
		}
		this.resvWorker = new TrainResvWorker(trainList, this.lastSearchParam);
		this.resvWorker.startWorking();
	}

	class TrainResvWorker {
		// The list of train that is wanted to be reserved.
		private HashSet<String> wishTrainNo = null;
		// train search parameter
		private TrainSearchParam searchParam = null;

		private boolean isRunning = false;

		public TrainResvWorker(ArrayList<Train> wishTrainList, TrainSearchParam searchParam) {
			// Set parameters to search and reserve train.
			this.wishTrainNo = new HashSet<String>();
			for (Train train : wishTrainList) {
				this.wishTrainNo.add(train.no);
			}
			this.searchParam = searchParam;
		}

		public void startWorking() {
			this.isRunning = true;
			doSearch();
		}

		public void stopWorking() {
			this.isRunning = false;
		}

		private void doSearch() {
			if (this.isRunning) {
				Log.d(TAG, "Search train for reservation");

				// Register train list handler.
				MyTrainResv.this.http.setOnSearchTrainCallback(
						this.onSearchTrainForResvCallback);
				// Search trains.
				MyTrainResv.this.http.searchTrain(
						this.searchParam.stationFrom,
						this.searchParam.stationTo,
						this.searchParam.date,
						this.searchParam.timeFrom,
						this.searchParam.ktxOnly);
			}
		}

		// Train list are obtained, see if there is a train available.
		private final MyHttp.OnSearchTrainCallback onSearchTrainForResvCallback = new MyHttp.OnSearchTrainCallback() {
			@Override
			public void onResult(ArrayList<Train> trainList, String error) {
				boolean toBeContinued = true;
				if (error != null) {
					// If there was an error show toast message.
					showToast(error);
					toBeContinued = false;
				} else {
					// Iterate over the train list.
					for (Train train: trainList) {
						// We got a train available.
						if (train.hasNormal || train.hasSpecial && MyTrainResv.this.lastSearchParam.firstClass) {
							// And the train is one of wanted.
							if (TrainResvWorker.this.wishTrainNo.contains(train.no)) {
								// Try reservation
								doResv(train);
								toBeContinued = false;
							}
						}
					}
				}
				if (toBeContinued) {
					Log.d(TAG, "Search train to be continued!");
					doSearch();
				}
			}
		};

		private void doResv(Train train) {
			Log.i(TAG, "Try reserve train");
			Log.i(TAG, train.toString());
		}
	}


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

	// Train searching Parameter.
	public class TrainSearchParam {
		public TrainSearchParam(
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
