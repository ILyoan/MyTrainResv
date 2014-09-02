package com.ilyoan.mytrainresv;

import java.util.ArrayList;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.MenuItem;

import com.ilyoan.mytrainresv.core.MyTrainResv;
import com.ilyoan.mytrainresv.core.Train;

public class MainActivity extends FragmentActivity implements ViewListFragment.Callback {
	ViewListFragment viewListFragment = new ViewListFragment();
	LoginFragment loginFragment = new LoginFragment();
	TrainFragment trainFragment = new TrainFragment();
	TrainListFragment trainListFragment = new TrainListFragment();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

		fragmentTransaction.add(R.id.main_activity, this.viewListFragment);
		fragmentTransaction.add(R.id.main_activity, this.loginFragment);
		fragmentTransaction.hide(this.loginFragment);
		fragmentTransaction.add(R.id.main_activity, this.trainFragment);
		fragmentTransaction.hide(this.trainFragment);
		fragmentTransaction.add(R.id.main_activity, this.trainListFragment);
		fragmentTransaction.hide(this.trainListFragment);
		fragmentTransaction.commit();

		// Initialize MyTrainResv
		MyTrainResv.setActivity(this);
		MyTrainResv.getInstance().setNotification(this.myTrainNoti);
	}

	@Override
	public void onItemSelected(String id) {
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		if (id == ViewListFragment.VIEW_LOGIN) {
			fragmentTransaction.hide(this.viewListFragment);
			fragmentTransaction.show(this.loginFragment);
			fragmentTransaction.hide(this.trainFragment);
			fragmentTransaction.hide(this.trainListFragment);
			fragmentTransaction.commit();
		} else if (id == ViewListFragment.VIEW_TRAIN) {
			fragmentTransaction.hide(this.viewListFragment);
			fragmentTransaction.hide(this.loginFragment);
			fragmentTransaction.show(this.trainFragment);
			fragmentTransaction.hide(this.trainListFragment);
			fragmentTransaction.commit();
		} else if (id == ViewListFragment.VIEW_TRAIN_LIST) {
			fragmentTransaction.hide(this.viewListFragment);
			fragmentTransaction.hide(this.loginFragment);
			fragmentTransaction.hide(this.trainFragment);
			fragmentTransaction.show(this.trainListFragment);
			fragmentTransaction.commit();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			FragmentManager fragmentManager = getFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.show(this.viewListFragment);
			fragmentTransaction.hide(this.loginFragment);
			fragmentTransaction.hide(this.trainFragment);
			fragmentTransaction.hide(this.trainListFragment);
			fragmentTransaction.commit();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void displayNoti(String title, String text) {
		displayNoti(title, text, false);
	}

	public void displayNoti(String title, String text, boolean vibrate) {
		// Notification bar
		int notificationId = 001;

		Intent viewIntent = new Intent(this, MainActivity.class);
		PendingIntent viewPendingIntent =
				PendingIntent.getActivity(this, 0, viewIntent, 0);

		NotificationCompat.Builder notificationBuilder =
				new NotificationCompat.Builder(this)
		.setSmallIcon(R.drawable.ic_launcher)
		.setContentTitle(title)
		.setContentText(text)
		.setContentIntent(viewPendingIntent);

		if (vibrate) {
			notificationBuilder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000, 1000, 3000 });
		}

		// Get an instance of the NotificationManager service
		NotificationManager nm =
				(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

		Log.d("MyTrainResv", "display notification: " + text);

		// Build the notification and issues it with notification manager.
		nm.notify(notificationId, notificationBuilder.build());
	}

	MyTrainResv.OnNotification myTrainNoti = new MyTrainResv.OnNotification() {
		@Override
		public void onTrainList(ArrayList<Train> trainList) {
			MainActivity.this.trainListFragment.setTrainList(trainList);
			onItemSelected(ViewListFragment.VIEW_TRAIN_LIST);
		}
	};
}

