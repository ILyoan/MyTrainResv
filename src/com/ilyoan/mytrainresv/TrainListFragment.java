package com.ilyoan.mytrainresv;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TrainListFragment extends Fragment {
	private View view = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.view = inflater.inflate(R.layout.layout_train_list, container, false);
		initWidgets();
		return this.view;
	}

	private void initWidgets() {
	}
}
