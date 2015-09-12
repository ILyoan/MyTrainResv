package com.ilyoan.mytrainresv.core;

import java.util.ArrayList;
import java.util.HashSet;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.ilyoan.mytrainresv.MainActivity;

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
				//station.getCode(stationFrom),
				//station.getCode(stationTo),
				stationFrom,
				stationTo,
				date,
				timeFrom,
				timeTo,
				firstClass,
				ktxOnly);

		// Do search.
		this.http.searchTrain(
				//station.getCode(stationFrom),
				//station.getCode(stationTo),
				stationFrom,
				stationTo,
				date,
				timeFrom,
				ktxOnly,
				this.onSearchTrainCallback);
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

		int tries = 0;

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
			this.tries = 0;
			doSearch();
		}

		public void stopWorking() {
			this.isRunning = false;
		}

		private void doSearch() {
			if (this.isRunning) {
				Log.d(TAG, "Search train for reservation");

				this.tries += 1;
				try {
					// Too many request can be detected and block by system maintainer ^^;
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				if (this.tries % 20 == 0) {
					showToast("에약시도중: " + this.tries);
				}
				displayNoti("예약중", "시도횟수: " + this.tries);

				// Search trains.
				MyTrainResv.this.http.searchTrain(
						this.searchParam.stationFrom,
						this.searchParam.stationTo,
						this.searchParam.date,
						this.searchParam.timeFrom,
						this.searchParam.ktxOnly,
						this.onSearchTrainCallback);
			}
		}

		// Train list are obtained, see if there is a train available.
		private final MyHttp.OnSearchTrainCallback onSearchTrainCallback = new MyHttp.OnSearchTrainCallback() {
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

			MyTrainResv.this.http.resv(train, this.onResvTrainCallback);
		}

		private final MyHttp.OnResvTrainCallback onResvTrainCallback = new MyHttp.OnResvTrainCallback() {
			@Override
			public void onResult(boolean result, String error) {
				Log.i(TAG, "Reservation result: " + result);
				if (result == true) {
					showToast("예약 완료");
					displayVibrateNoti("예약 완료", "예약 완료");
				} else {
					if (error != null) {
						Log.i(TAG, "Reservation error: " + error);
						showToast(error);
						displayVibrateNoti("예약 실패", error);
						doSearch();
					} else {
						Log.e(TAG, "Reservation error but error is null");
						showToast("알수 없는 문제로 예약 실패");
						displayVibrateNoti("예약 실패", "알수 없는 문제로 예약 실패");
					}
				}
			}
		};
	}


	// Show toast message.
	public static void showToast(String message) {
		Toast toast = Toast.makeText(
				MyTrainResv.activity.getApplicationContext(),
				message,
				Toast.LENGTH_LONG);
		toast.show();
	}

	// display notification
	public static void displayNoti(String title, String text) {
		MainActivity activity = (MainActivity)MyTrainResv.activity;
		activity.displayNoti(title, text);
	}

	// display notification
	public static void displayVibrateNoti(String title, String text) {
		MainActivity activity = (MainActivity)MyTrainResv.activity;
		activity.displayNoti(title, text, true);
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
