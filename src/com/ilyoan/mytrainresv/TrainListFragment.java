package com.ilyoan.mytrainresv;

import java.util.ArrayList;

import com.ilyoan.mytrainresv.core.MyTrainResv;
import com.ilyoan.mytrainresv.core.Train;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

public class TrainListFragment extends Fragment {
	private View view = null;
	private ListView listView = null;
	private ArrayList<String> trainStringList = null;
	private ArrayList<Train> trainList = null;
	private TrainListAdapter adapter = null;
	private Button resvButton = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.view = inflater.inflate(R.layout.layout_train_list, container, false);
		initWidgets();
		return this.view;
	}

	private void initWidgets() {
		this.trainStringList = new ArrayList<String>();
		this.listView = (ListView)this.view.findViewById(R.id.listView_train);
		this.adapter = new TrainListAdapter(this.view.getContext(), this.trainStringList);
		this.listView.setAdapter(this.adapter);
		this.listView.setOnItemClickListener(this.mItemClickListener);
		this.resvButton = (Button)this.view.findViewById(R.id.button_resv);
		this.resvButton.setOnClickListener(this.onResvClick);
	}

	public void setTrainList(ArrayList<Train> trainList) {
		this.trainList = trainList;
		this.trainStringList = new ArrayList<String>();
		for (Train train : trainList) {
			this.trainStringList.add(train.toString());
		}
		this.adapter = new TrainListAdapter(this.view.getContext(), this.trainStringList);
		this.listView.setAdapter(this.adapter);
	}

	private final AdapterView.OnItemClickListener mItemClickListener = new
			AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) {
			TrainListFragment.this.adapter.setChecked(position);
			TrainListFragment.this.adapter.notifyDataSetChanged();
		}
	};

	Button.OnClickListener onResvClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			MyTrainResv myTrainResv = MyTrainResv.getInstance();
			ArrayList<Train> checkTrainList = new ArrayList<Train>();
			for (int i = 0; i < TrainListFragment.this.adapter.checkedList.length; ++i) {
				if (TrainListFragment.this.adapter.checkedList[i]) {
					checkTrainList.add(TrainListFragment.this.trainList.get(i));
				}
			}
			myTrainResv.resvTrain(checkTrainList);
		}
	};

	class TrainListAdapter extends BaseAdapter {
		private LayoutInflater inflater = null;
		private ArrayList<String> trainList = null;
		private boolean[] checkedList = null;

		public TrainListAdapter(Context context, ArrayList<String> list) {
			this.inflater = LayoutInflater.from(context);
			this.trainList = list;
			this.checkedList = new boolean[this.trainList.size()];
		}

		public void setChecked(int position) {
			this.checkedList[position] = !this.checkedList[position];
		}

		@Override
		public int getCount() {
			return this.trainList.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			TrainListRowViewInfo viewInfo = null;
			if (convertView == null) {
				viewInfo = new TrainListRowViewInfo();
				view = this.inflater.inflate(R.layout.layout_train_list_row, null);
				viewInfo.checkBox = (CheckBox)view.findViewById(R.id.checkBox_train);
				viewInfo.checkBox.setClickable(false);
				viewInfo.checkBox.setFocusable(false);
				viewInfo.checkBox.setText(this.trainList.get(position));
				view.setTag(viewInfo);
			} else {
				view = convertView;
				viewInfo = (TrainListRowViewInfo)view.getTag();
			}
			viewInfo.checkBox.setChecked(this.checkedList[position]);
			return view;
		}

		class TrainListRowViewInfo {
			private CheckBox checkBox = null;
		}
	}
}
