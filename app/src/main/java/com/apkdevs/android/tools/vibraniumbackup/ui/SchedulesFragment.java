package com.apkdevs.android.tools.vibraniumbackup.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apkdevs.android.tools.vibraniumbackup.R;

public class SchedulesFragment extends Fragment {

		public SchedulesFragment() {}

		public static SchedulesFragment newInstance() {
				SchedulesFragment fragment = new SchedulesFragment();
				return fragment;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
				View rootView = inflater.inflate(R.layout.frag_schd, container, false);
				return rootView;
		}
}
