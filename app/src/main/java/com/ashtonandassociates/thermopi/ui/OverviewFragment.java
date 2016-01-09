package com.ashtonandassociates.thermopi.ui;

import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ashtonandassociates.thermopi.R;
import com.ashtonandassociates.thermopi.api.ApiService;
import com.ashtonandassociates.thermopi.api.ServiceGenerator;
import com.ashtonandassociates.thermopi.api.response.CurrentResponse;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class OverviewFragment extends Fragment {

	public static final String TAG = OverviewFragment.class.getSimpleName();

	protected TextView mSensor1Label;
	protected TextView mSensor1Value;
	protected TextView mSensor2Label;
	protected TextView mSensor2Value;
	protected TextView mSensorDate;

	protected ApiService service;

	private void refreshValues() {
		service = ServiceGenerator.createService(ApiService.class, getResources());
		service.getCurrent(new Callback<CurrentResponse>() {
			@Override
			public void success(CurrentResponse currentResponse, Response response) {
				Log.d(TAG, currentResponse.toString());
				if(currentResponse.data.size() != 0) {
					if (currentResponse.data.get(0) != null) {
						mSensorDate.setText(currentResponse.data.get(0).datetime);
						mSensor1Label.setText(currentResponse.data.get(0).description);
						mSensor1Value.setText(currentResponse.data.get(0).value);
					}

					if (currentResponse.data.get(1) != null) {
						mSensor2Label.setText(currentResponse.data.get(1).description);
						mSensor2Value.setText(currentResponse.data.get(1).value);
					}
				}
			}

			@Override
			public void failure(RetrofitError error) {
				Log.d(TAG, error.toString());
			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_overview, null);
		mSensorDate = (TextView) view.findViewById(R.id.sensor_date);
		mSensor1Label = (TextView) view.findViewById(R.id.sensor_1_label);
		mSensor1Value = (TextView) view.findViewById(R.id.sensor_1_value);
		mSensor2Label = (TextView) view.findViewById(R.id.sensor_2_label);
		mSensor2Value = (TextView) view.findViewById(R.id.sensor_2_value);
		if(savedInstanceState != null) {
			boolean hidden = savedInstanceState.getBoolean("fragHidden");
			if(hidden) {
				getFragmentManager().beginTransaction().hide(this).commit();
			}
		}
		refreshValues();
		return view;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean("fragHidden", isHidden());
		outState.putString("dateString", mSensorDate.getText().toString());
		outState.putString("valueOne", mSensor1Label.getText().toString());
		outState.putString("valuetwo", mSensor2Label.getText().toString());
	}
}
