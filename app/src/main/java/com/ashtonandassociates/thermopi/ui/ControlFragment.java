package com.ashtonandassociates.thermopi.ui;

import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.ashtonandassociates.thermopi.R;
import com.ashtonandassociates.thermopi.api.ApiService;
import com.ashtonandassociates.thermopi.api.ServiceGenerator;
import com.ashtonandassociates.thermopi.api.response.ControlResponse;
import com.ashtonandassociates.thermopi.api.response.CurrentResponse;
import com.ashtonandassociates.thermopi.api.shared.ApiTemperature;
import com.ashtonandassociates.thermopi.util.AppStateManager;
import com.ashtonandassociates.thermopi.util.NumberUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ControlFragment extends Fragment
	implements RadioGroup.OnCheckedChangeListener, View.OnClickListener, Callback<ControlResponse> {

	public static final String TAG = ControlFragment.class.getSimpleName();

	public final String COMMAND_TIME = "CMD TIME";
	public final String COMMAND_TEMP = "CMD TEMP";

	protected RadioGroup mRadioGroup;
	protected View mTemperatureGroup;
	protected View mTimeGroup;
	protected Button mTimeButton;
	protected Button mTemperatureButton;
	protected EditText mEditTextTemperature;
	protected EditText mEditTextTime;

	protected ApiService service;

	private void refreshValues() {
		service.getCurrent(new Callback<CurrentResponse>() {
			@Override
			public void success(CurrentResponse currentResponse, Response response) {
				Log.d(TAG, currentResponse.toString());
				for(CurrentResponse.Current current : currentResponse.data) {
					if(current.description.equals("drinnen")) {
						Double temp = new Double(current.value.toString());
						mEditTextTemperature.setHint(NumberUtil.formatTemperature(temp) + "*");
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
		View view = inflater.inflate(R.layout.fragment_control, null);
		service = ServiceGenerator.createService(ApiService.class, getResources());
		mRadioGroup = (RadioGroup)view.findViewById(R.id.control_radio_group);
		mRadioGroup.setOnCheckedChangeListener(this);

		mTemperatureButton = (Button)view.findViewById(R.id.control_button_temperature);
		mTemperatureButton.setOnClickListener(this);
		mTimeButton = (Button)view.findViewById(R.id.control_button_time);
		mTimeButton.setOnClickListener(this);

		mTemperatureGroup = view.findViewById(R.id.control_temperature_layout_group);
		mTemperatureGroup.setVisibility(View.GONE);
		mTimeGroup = view.findViewById(R.id.control_time_layout_group);
		mTimeGroup.setVisibility(View.VISIBLE);

		mEditTextTemperature = (EditText)view.findViewById(R.id.control_edittext_temperature);
		mEditTextTime = (EditText)view.findViewById(R.id.control_edittext_time);

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
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		Log.v(TAG, "" + checkedId);
		switch(checkedId) {
			case R.id.control_radio_temperature:
				mTimeGroup.setVisibility(View.GONE);
				mTemperatureGroup.setVisibility(View.VISIBLE);
				refreshValues();
				break;

			case R.id.control_radio_time:
				mTimeGroup.setVisibility(View.VISIBLE);
				mTemperatureGroup.setVisibility(View.GONE);
				break;
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
				.setMessage(getActivity().getString(R.string.control_alert_dialog_missing_value_message))
				.setNeutralButton(getActivity().getString(R.string.control_alert_dialog_dismiss), null);
		switch(id) {
			case R.id.control_button_temperature:
				Log.i(TAG, "temperature button");
				if(mEditTextTemperature.getText().length() == 0) {
					builder.show();
					break;
				}

				ApiTemperature temp = new ApiTemperature(new Double(mEditTextTemperature.getText().toString()), ApiTemperature.CONST_DEFAULT_SCALE);
				String tempString = temp.getTemperature(ApiTemperature.CONST_API_SCALE).toString();
				service.sendCommand(COMMAND_TEMP, tempString, this.getApiHashString(COMMAND_TEMP, tempString), this);
				break;
			case R.id.control_button_time:
				Log.i(TAG, "time button");
				if(mEditTextTime.getText().length() == 0) {
					builder.show();
					break;
				}
				Integer inputMinutes = new Integer(mEditTextTime.getText().toString());
				Integer minutes = inputMinutes * 60;
				service.sendCommand(COMMAND_TIME, minutes.toString(), this.getApiHashString(COMMAND_TIME, minutes.toString()), this);
				break;
		}
	}

	@Override
	public void success(ControlResponse response, Response response2) {
		Log.d(TAG, response.toString());
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		if(response.error != null) {
			builder.setMessage(response.error.text);
		} else {
			builder.setMessage(getActivity().getString(R.string.control_alert_dialog_server_ok_message) + "\n" + response.result);
		}
		builder.setNeutralButton(getActivity().getString(R.string.control_alert_dialog_dismiss), null);
		builder.show();
	}

	@Override
	public void failure(RetrofitError error) {
		Log.e(TAG, error.toString());
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
				.setMessage(getActivity().getString(R.string.control_alert_dialog_server_error_message))
				.setNeutralButton(getActivity().getString(R.string.control_alert_dialog_dismiss), null);
		builder.show();
	}

	private String getApiHashString(String command, String param) {
		String retVal = null;
		AppStateManager manager = AppStateManager.getInstance();
		String sharedSecret = manager.getApiSharedSecret();
		String nonce = manager.getApiNonce();
		if(nonce == null) {
			return null;
		}
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			String hashMe = command.concat(param.concat(sharedSecret.concat(nonce)));
//			Log.v(TAG, "hashme" + hashMe);

			byte[] bytes = md.digest(hashMe.getBytes());
			StringBuilder sb = new StringBuilder(2 * bytes.length);
			for (byte b : bytes) {
				sb.append("0123456789abcdef".charAt((b & 0xF0) >> 4));
				sb.append("0123456789abcdef".charAt((b & 0x0F)));
			}

			retVal = sb.toString();
		} catch(NoSuchAlgorithmException nsae) {
			Log.e(TAG, nsae.toString());
		}
		return retVal;
	}

}