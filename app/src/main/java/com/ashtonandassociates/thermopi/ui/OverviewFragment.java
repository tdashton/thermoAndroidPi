package com.ashtonandassociates.thermopi.ui;

import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ashtonandassociates.thermopi.R;
import com.ashtonandassociates.thermopi.api.ApiService;
import com.ashtonandassociates.thermopi.api.response.CurrentResponse;
import com.ashtonandassociates.thermopi.api.annotation.*;
import com.ashtonandassociates.thermopi.interfaces.ApiInterface;
import com.ashtonandassociates.thermopi.util.FragmentVisibilitySaver;

public class OverviewFragment extends Fragment {

	public static final String TAG = OverviewFragment.class.getSimpleName();
	private final FragmentVisibilitySaver visibilitySaver = new FragmentVisibilitySaver();

	private boolean mInitialized = false;

	protected TextView mSensor1Label;
	protected TextView mSensor1Value;
	protected TextView mSensor2Label;
	protected TextView mSensor2Value;
	protected TextView mSensorDate;

	protected ApiService service;

	@ApiListener(CurrentResponse.class)
	@SuppressWarnings("unused")
	public void onApiServiceResponse(CurrentResponse currentResponse) {
		Log.d(TAG, currentResponse.toString());
		if(mInitialized == false) {
			return;
		}
		if(currentResponse.data.size() != 0) {
			if (currentResponse.data.get(0) != null) {
				if (currentResponse.data.get(0) != null) {
					mSensorDate.setText(currentResponse.data.get(0).datetime);
					mSensor1Label.setText(currentResponse.data.get(0).description);
					mSensor1Value.setText(currentResponse.data.get(0).value);
				}
			}

			if (currentResponse.data.get(1) != null) {
				if (currentResponse.data.get(1) != null) {
					mSensor2Label.setText(currentResponse.data.get(1).description);
					mSensor2Value.setText(currentResponse.data.get(1).value);
				}
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_overview, null);
		visibilitySaver.restoreVisibilityState(getFragmentManager(), this, savedInstanceState);
		((ApiInterface)getActivity()).refreshValues();

		mSensorDate = (TextView) view.findViewById(R.id.sensor_date);
		mSensor1Label = (TextView) view.findViewById(R.id.sensor_1_label);
		mSensor1Value = (TextView) view.findViewById(R.id.sensor_1_value);
		mSensor2Label = (TextView) view.findViewById(R.id.sensor_2_label);
		mSensor2Value = (TextView) view.findViewById(R.id.sensor_2_value);

		mInitialized = true;

		return view;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean("isHidden", isHidden());
	}
}
