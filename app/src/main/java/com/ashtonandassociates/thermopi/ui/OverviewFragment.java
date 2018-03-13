package com.ashtonandassociates.thermopi.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ashtonandassociates.thermopi.R;
import com.ashtonandassociates.thermopi.api.ApiListenerInterface;
import com.ashtonandassociates.thermopi.api.ApiListenerService;
import com.ashtonandassociates.thermopi.api.response.CurrentResponse;
import com.ashtonandassociates.thermopi.api.annotation.*;
import com.ashtonandassociates.thermopi.api.ApiInterface;
import com.ashtonandassociates.thermopi.util.Constants;
import com.ashtonandassociates.thermopi.util.FragmentVisibilitySaver;
import com.ashtonandassociates.thermopi.util.NumberUtil;

import java.text.DateFormat;
import java.util.Date;

public class OverviewFragment extends Fragment implements ApiListenerInterface {

	public static final String TAG = OverviewFragment.class.getSimpleName();
	private final FragmentVisibilitySaver visibilitySaver = new FragmentVisibilitySaver();

	private boolean mInitialized = false;

	protected TextView mSensorDate;
	protected LinearLayout mSensorDataContainer;

	@ApiListener(CurrentResponse.class)
	@SuppressWarnings("unused")
	public void onApiServiceResponse(CurrentResponse currentResponse) {
		Log.d(TAG, currentResponse.toString());
		if (mInitialized == false) {
			return;
		}

		mSensorDataContainer.removeAllViews();
		for(int i = 0; i < currentResponse.data.size(); i++) {
			View container = getActivity().getLayoutInflater().inflate(R.layout.fragment_overview_sensor, null);
			TextView mSensorLabel = (TextView) container.findViewById(R.id.sensor_x_label);
			TextView mSensorValue = (TextView) container.findViewById(R.id.sensor_x_value);
			DateFormat df = DateFormat.getDateTimeInstance();
			mSensorDate.setText(
					df.format(new Date(currentResponse.data.get(i).datetime))
			);
			mSensorLabel.setText(currentResponse.data.get(i).description);
			Double temp = Double.valueOf(currentResponse.data.get(i).value);
			mSensorValue.setText(NumberUtil.formatTemperature(temp));

			mSensorDataContainer.addView(container);
		}
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ApiListenerService.getInstance().registerListener(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_overview, null);
		visibilitySaver.restoreVisibilityState(getFragmentManager(), this, savedInstanceState);

		mSensorDataContainer = (LinearLayout) view.findViewById(R.id.overview_sensor_container);
		mSensorDate = (TextView) view.findViewById(R.id.sensor_date);

		SharedPreferences sharedPrefs = getActivity().getSharedPreferences(Constants.CONST_SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
		TextView locationName = (TextView) view.findViewById(R.id.overview_location_name);
		locationName.setText(sharedPrefs.getString(Constants.CONST_LOCATION_NAME, getString(R.string.settings_location_name)));

		mInitialized = true;

		return view;
	}

	@Override
	public void onStop() {
		super.onStop();
		ApiListenerService.getInstance().unregisterListener(this);
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		if(hidden == false) {
			((ApiInterface)getActivity()).refreshCurrentValues();
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean("isHidden", isHidden());
	}
}
