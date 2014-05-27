package com.ilyoan.mytrainresv.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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

		// Stop the thread
		this.trainResvThread.stopThread();
		// Start with new parameter
		this.trainResvThread.startThread(trainList, this.lastSearchOption);


	}

	// Train reservation thread.
	TrainResvThread trainResvThread = new TrainResvThread();
	class TrainResvThread extends Thread {
		// The list of train that is wanted to be reserved.
		private HashSet<String> wishTrainNo = null;
		// train search option
		private TrainSearchOption searchOption = null;
		//
		private boolean running = false;
		private final Lock searchLoopLock = new ReentrantLock(true);


		public void startThread(
				final ArrayList<Train> wishList,
				final TrainSearchOption searchOption) {
			// Set parameters to search and reserve train.
			this.wishTrainNo = new HashSet<String>();
			for (Train train : wishList) {
				this.wishTrainNo.add(train.no);
			}
			this.searchOption = searchOption;

			// Set this thread is running.
			this.running = true;

			// Start thread.
			start();
		}

		public void stopThread() {
			// To halt thread loop.
			this.running = false;
			// If the thread is running than wait until it dies.
			if (isAlive()) {
				try {
					join();
				} catch (InterruptedException e) {
					showToast(e.getMessage());
				}
			}
		}

		@Override
		public void run() {
			while (this.running) {
				this.searchLoopLock.lock();
				Log.d(TAG, "Resv thread running");

				// Register train list handler.
				MyTrainResv.this.http.setOnSearchTrainCallback(
						this.onSearchTrainForResvCallback);
				// Search trains.
				MyTrainResv.this.http.searchTrain(
						this.searchOption.stationFrom,
						this.searchOption.stationTo,
						this.searchOption.date,
						this.searchOption.timeFrom,
						this.searchOption.ktxOnly);
			}
		}

		// Train list are obtained, see if there is a train available.
		private final MyHttp.OnSearchTrainCallback onSearchTrainForResvCallback = new MyHttp.OnSearchTrainCallback() {
			@Override
			public void onResult(ArrayList<Train> trainList, String error) {
				if (error != null) {
					// If there was an error show toast message.
					showToast(error);
				} else {
					// Iterate over the train list.
					for (Train train: trainList) {
						// We got a train available.
						if (train.hasNormal || train.hasSpecial && MyTrainResv.this.lastSearchOption.firstClass) {
							// And the train is one of wanted.
							if (TrainResvThread.this.wishTrainNo.contains(train.no)) {
								// Try reservation
								TryResvTrain(train);
							}
						}
					}
				}
				Log.d(TAG, "Resv thread notify");
				TrainResvThread.this.searchLoopLock.unlock();
			}
		};

		public void TryResvTrain(Train train) {
			Log.d(TAG, "MyTrainResv.TryResvTrain()");
			Log.i(TAG, "Try resv train - " + train.toString());

			//MyTrainResv.this.http.resv(train);
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

	// Train searching option.
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
