package com.ilyoan.mytrainresv;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ViewListFragment extends ListFragment {
	public static final String VIEW_LOGIN = "Login";
	public static final String VIEW_TRAIN = "Train";

	private Callback callback = dummyCallback;
	private final ArrayList<String> viewList = new ArrayList<String>();

	public interface Callback {
		public void onItemSelected(String id);
	}

	private static Callback dummyCallback = new Callback() {
		@Override
		public void onItemSelected(String id) {
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.viewList.add(VIEW_LOGIN);
		this.viewList.add(VIEW_TRAIN);

		setListAdapter(new ArrayAdapter<String>(
				getActivity(),
				android.R.layout.simple_list_item_activated_1,
				android.R.id.text1,
				this.viewList));
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (!(activity instanceof Callback)) {
			throw new IllegalStateException("Activity must implement fragment's callbacks.");
		}
		this.callback = (Callback)activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		this.callback = dummyCallback;
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		super.onListItemClick(listView, view, position, id);
		this.callback.onItemSelected(this.viewList.get(position));
	}
}
