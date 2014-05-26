package com.ilyoan.mytrainresv;

import java.util.ArrayList;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
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

	MyTrainResv.OnNotification myTrainNoti = new MyTrainResv.OnNotification() {
		@Override
		public void onTrainList(MyTrainResv myTrain) {
			ArrayList<Train> trainList = myTrain.getTrainList();
			MainActivity.this.trainListFragment.setTrainList(trainList);
			onItemSelected(ViewListFragment.VIEW_TRAIN_LIST);
		}
	};
}

