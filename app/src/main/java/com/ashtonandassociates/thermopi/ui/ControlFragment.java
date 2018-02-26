package com.ashtonandassociates.thermopi.ui;

import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ashtonandassociates.thermopi.R;
import com.ashtonandassociates.thermopi.SettingsActivity;
import com.ashtonandassociates.thermopi.api.annotation.ApiListener;
import com.ashtonandassociates.thermopi.api.response.ControlCommandResponse;
import com.ashtonandassociates.thermopi.api.response.ControlLogsResponse;
import com.ashtonandassociates.thermopi.api.response.ControlReadResponse;
import com.ashtonandassociates.thermopi.api.response.CurrentResponse;
import com.ashtonandassociates.thermopi.api.shared.ApiTemperature;
import com.ashtonandassociates.thermopi.api.ApiInterface;
import com.ashtonandassociates.thermopi.persistence.HideRecentLogTask;
import com.ashtonandassociates.thermopi.persistence.entity.RecentLog;
import com.ashtonandassociates.thermopi.ui.lifecycle.MainViewModel;
import com.ashtonandassociates.thermopi.ui.list.RecentLogAdapter;
import com.ashtonandassociates.thermopi.ui.list.element.ControlRecentItem;
import com.ashtonandassociates.thermopi.util.AppStateManager;
import com.ashtonandassociates.thermopi.util.Constants;
import com.ashtonandassociates.thermopi.util.FragmentVisibilitySaver;
import com.ashtonandassociates.thermopi.util.HashUtil;
import com.ashtonandassociates.thermopi.util.NumberUtil;
import com.google.common.collect.Lists;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ControlFragment extends Fragment
	implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, RadioGroup.OnCheckedChangeListener, View.OnClickListener, Callback<ControlCommandResponse>, Observer<List<RecentLog>> {

	private static final String TAG = ControlFragment.class.getSimpleName();
	private final FragmentVisibilitySaver visibilitySaver = new FragmentVisibilitySaver();

	public static final String COMMAND_STATUS = "CMD RUNNING";
	public static final String COMMAND_TIME = "CMD TIME";
	public static final String COMMAND_TEMP = "CMD TEMP";

	public final String CONSTANT_API_TEMP_INSIDE = "drinnen";

	private boolean mInitialized = false;

	protected float mMinimumTemperature = (float) SettingsActivity.TEMPERATURE_DEFAULT_MIN;
	protected float mMaximumTemperature = (float) SettingsActivity.TEMPERATURE_DEFAULT_MAX;

	public SharedPreferences sharedPrefs;
	AppStateManager manager = AppStateManager.getInstance();

	protected RadioGroup mRadioGroup;
	protected View mTemperatureGroup;
	protected View mTimeGroup;
	protected Button mTimeButton;
	protected Button mTemperatureButton;
	protected SeekBar mSeekBarTemperature;
	protected TextView mEditTextTemperature;
	protected EditText mEditTextTime;
	protected TextView mTemperatureCurrentTextView;
	protected TextView mTemperatureStatusTextView;
	protected ListView mListViewControlRecent;
	protected RecentLogAdapter mListViewRecentAdapter;
	/** Map<String, List<RecentLog> mListRecent; Eigentlich dies... */
	protected Map<String, List> mListRecent;
	protected RecentLog mLogToUpdate;
	protected int mLogToUpdatePosition;
	protected MainViewModel mMainViewModel;

	protected SeekBar.OnSeekBarChangeListener mSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

		private synchronized void getNonceSynchronized() {
			if (manager.getApiNonce() == null) {
				((ApiInterface) getActivity()).getApiNonce();
			} else {
				Log.v(TAG, "already had a nonce");
			}
		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			Log.v(TAG, String.format("onProgressChanged progress:%d fromUser:%s", progress, Boolean.valueOf(fromUser)).toString());
			mEditTextTemperature.setText(
					NumberUtil.formatTemperature(Double.valueOf(ControlFragment.this.percentToTemperature(progress)))
			);
			this.getNonceSynchronized();
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			Log.v(TAG, "onStartTrackingTouch");
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			Log.v(TAG, "onStopTrackingTouch");
		}

		protected AppStateManager manager = AppStateManager.getInstance();

	};

	@ApiListener(ControlLogsResponse.class)
	@SuppressWarnings("unused")
	public void onApiServiceResponse(ControlLogsResponse response) {
		Log.i(TAG, response.getClass().getSimpleName());
		this.mMainViewModel
				.getLogs(ControlFragment.COMMAND_TIME, true)
				.observe(this, this);
		this.mMainViewModel
				.getLogs(ControlFragment.COMMAND_TEMP, true)
				.observe(this, this);
	}

	@ApiListener(ControlReadResponse.class)
	@SuppressWarnings("unused")
	public void onApiServiceResponse(ControlReadResponse response) {
		Log.d(TAG, response.toString());
		if(mInitialized == false) {
			return;
		}
		for(ControlReadResponse.Result result : response.result) {
			if(result.type.equals(COMMAND_TEMP)) {
				Integer temp = new Integer(result.param);
				ApiTemperature apiTemp = new ApiTemperature(temp, ApiTemperature.CONST_API_SCALE);
				mSeekBarTemperature.setProgress(
					ControlFragment.this.temperatureToPercent(
						apiTemp.getTemperatureDouble(ApiTemperature.CONST_DEFAULT_SCALE).floatValue()
					)
				);
				mEditTextTemperature.setText(
						apiTemp.toString()
				);
				break;
			}
		}
	}

	@ApiListener(ControlReadResponse.class)
	@SuppressWarnings("unused")
	public void onApiControlReadResponse(ControlReadResponse controlReadResponse) {
		Log.d(TAG, controlReadResponse.toString());
		if(mInitialized == false) {
			return;
		}
		for(ControlReadResponse.Result result : controlReadResponse.result) {
			if(result.type.equals(COMMAND_STATUS)) {
				String status = getString(R.string.control_status_running_false);
				if("1".equals(result.param)) {
					status = getString(R.string.control_status_running_true);
				}
				mTemperatureStatusTextView.setText(String.format(
						getString(R.string.control_set_temperature_status),
						status
				));
			}
		}
	}

	@ApiListener(CurrentResponse.class)
	@SuppressWarnings("unused")
	public void onApiServiceResponse(CurrentResponse currentResponse) {
		Log.d(TAG, currentResponse.toString());
		if(mInitialized == false) {
			return;
		}
		for(CurrentResponse.Current current : currentResponse.data) {
			if(current.description.equals(CONSTANT_API_TEMP_INSIDE)) {
				Double temp = new Double(current.value);
				mTemperatureCurrentTextView.setText(String.format(
						getString(R.string.control_set_temperature_current),
						NumberUtil.formatTemperature(temp)
				));
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		this.toggleManualInput(
				(this.mRadioGroup.getCheckedRadioButtonId() == R.id.control_radio_time) ? ControlFragment.COMMAND_TIME : ControlFragment.COMMAND_TEMP
		);
		((ApiInterface)getActivity()).getApiNonce();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sharedPrefs = getActivity().getSharedPreferences(Constants.CONST_SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
		this.mMinimumTemperature = sharedPrefs.getFloat(Constants.CONST_CONTROL_TEMPERATURE_MINIMUM, SettingsActivity.TEMPERATURE_DEFAULT_MIN);
		this.mMaximumTemperature = sharedPrefs.getFloat(Constants.CONST_CONTROL_TEMPERATURE_MAXIMUM, SettingsActivity.TEMPERATURE_DEFAULT_MAX);
		this.mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
		this.mListRecent = new HashMap<>();
		this.mMainViewModel
				.getLogs(ControlFragment.COMMAND_TIME)
				.observe(this, this);
		this.mMainViewModel
				.getLogs(ControlFragment.COMMAND_TEMP)
				.observe(this, this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_control, null);
		visibilitySaver.restoreVisibilityState(getFragmentManager(), this, savedInstanceState);

		mRadioGroup = (RadioGroup)view.findViewById(R.id.control_radio_group);
		mRadioGroup.setOnCheckedChangeListener(this);

		mTemperatureCurrentTextView = (TextView)view.findViewById(R.id.control_set_temperature_current);
		mTemperatureCurrentTextView.setText(String.format(getString(R.string.control_set_temperature_current), "-"));

		mTemperatureStatusTextView = (TextView)view.findViewById(R.id.control_set_temperature_status);
		mTemperatureStatusTextView.setText(String.format(getString(R.string.control_set_temperature_status), "-"));

		mTemperatureButton = (Button)view.findViewById(R.id.control_button_temperature);
		mTemperatureButton.setOnClickListener(this);
		mTimeButton = (Button)view.findViewById(R.id.control_button_time);
		mTimeButton.setOnClickListener(this);

		mTemperatureGroup = view.findViewById(R.id.control_temperature_layout_group);
		mTemperatureGroup.setVisibility(View.GONE);

		mTimeGroup = view.findViewById(R.id.control_time_layout_group);

		mSeekBarTemperature = (SeekBar)view.findViewById(R.id.control_seekbar_temperature);
		mEditTextTemperature = (TextView)view.findViewById(R.id.label_temperature_degrees);
		mSeekBarTemperature.setOnSeekBarChangeListener(this.mSeekBarChangeListener);
		mEditTextTime = (EditText)view.findViewById(R.id.control_edittext_time);

		mListViewControlRecent = (ListView)view.findViewById(R.id.control_list_recent_control_values);

		int checked = sharedPrefs.getInt("controlMode", R.id.control_radio_time);
		mRadioGroup.check(checked);

		SharedPreferences sharedPrefs = getActivity().getSharedPreferences(Constants.CONST_SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
		TextView locationName = (TextView) view.findViewById(R.id.control_location_name);
		locationName.setText(sharedPrefs.getString(Constants.CONST_LOCATION_NAME, getString(R.string.settings_location_name)));

		mInitialized = true;

		return view;
	}

	@Override
	public void onChanged(@Nullable List<RecentLog> recentLogs) {
		if (recentLogs.size() == 0) {
			return;
		}
		mListRecent.put(recentLogs.get(0).type, Lists.transform(recentLogs, ControlRecentItem.transformFromRecentLog()));
		boolean updateUi = false;
		switch (recentLogs.get(0).type) {
			case ControlFragment.COMMAND_TIME:
				if (this.mRadioGroup.getCheckedRadioButtonId() == R.id.control_radio_time) {
					updateUi = true;
				}
				break;

			case ControlFragment.COMMAND_TEMP:
				if (this.mRadioGroup.getCheckedRadioButtonId() == R.id.control_radio_temperature) {
					updateUi = true;
				}
				break;
		}

		if (updateUi) {
			mListViewRecentAdapter = new RecentLogAdapter(getActivity(), android.R.layout.simple_list_item_1);
			mListViewControlRecent.setAdapter(mListViewRecentAdapter);
			mListViewControlRecent.setOnItemClickListener(this);
			mListViewControlRecent.setOnItemLongClickListener(this);
			mListViewRecentAdapter.clear();
			mListViewRecentAdapter.addAll(mListRecent.get(recentLogs.get(0).type));
			mListViewRecentAdapter.notifyDataSetChanged();
		}
//		for(RecentLog log : recentLogs) {
//			Log.v(TAG, "onChanged:" + log.type + " / " + log.param);
//		}
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		if(hidden == false) {
			((ApiInterface)getActivity()).refreshControlValues();
			((ApiInterface)getActivity()).refreshCurrentValues();
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean("isHidden", isHidden());
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
//		Log.v(TAG, "onCheckChanged" + checkedId);
		mListViewRecentAdapter = new RecentLogAdapter(getActivity(), android.R.layout.simple_list_item_1);
		mListViewControlRecent.setAdapter(mListViewRecentAdapter);
		mListViewControlRecent.setOnItemClickListener(this);
		mListViewRecentAdapter.clear();

		switch(checkedId) {
			case R.id.control_radio_temperature:
				this.toggleManualInput(ControlFragment.COMMAND_TEMP);
				if (mListRecent.get(ControlFragment.COMMAND_TEMP) != null) {
					mListViewRecentAdapter.addAll(mListRecent.get(ControlFragment.COMMAND_TEMP));
				}
				break;

			case R.id.control_radio_time:
				this.toggleManualInput(ControlFragment.COMMAND_TIME);
				if (mListRecent.get(ControlFragment.COMMAND_TIME) != null) {
					mListViewRecentAdapter.addAll(mListRecent.get(ControlFragment.COMMAND_TIME));
				}
				break;
		}

		mListViewRecentAdapter.notifyDataSetChanged();
		SharedPreferences.Editor editor = sharedPrefs.edit();
		editor.putInt("controlMode", checkedId);
		editor.commit();
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

				ApiTemperature temp = new ApiTemperature(
						Double.valueOf(this.percentToTemperature(mSeekBarTemperature.getProgress())),
						ApiTemperature.CONST_DEFAULT_SCALE);
				Double tempDouble = temp.getTemperatureDouble(
						ApiTemperature.CONST_API_SCALE);
				String tempString = Integer.toString(tempDouble.intValue());
				((ApiInterface)getActivity()).getApiService().sendCommand(COMMAND_TEMP, tempString, this.getApiHashString(COMMAND_TEMP, tempString), this);
				break;
			case R.id.control_button_time:
				Log.i(TAG, "time button");
				if(mEditTextTime.getText().length() == 0) {
					builder.show();
					break;
				}
				Integer inputMinutes = Integer.parseInt(mEditTextTime.getText().toString());
				Integer minutes = inputMinutes * 60;
				((ApiInterface)getActivity()).getApiService().sendCommand(COMMAND_TIME, minutes.toString(), this.getApiHashString(COMMAND_TIME, minutes.toString()), this);
				break;
		}
	}

	/** Method to receive click events from the ListView */
	@Override
	public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
		String type = (ControlFragment.this.mRadioGroup.getCheckedRadioButtonId() == R.id.control_radio_time ? ControlFragment.COMMAND_TIME : ControlFragment.COMMAND_TEMP);
		ControlRecentItem clickedRecentLog = (ControlRecentItem)ControlFragment.this.mListRecent.get(type).get(i);
		mLogToUpdate = new RecentLog(clickedRecentLog);
		mLogToUpdatePosition = i;

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
				.setMessage(getActivity().getString(R.string.control_hide_alert_dialog_title))
				.setCancelable(true)
				.setNegativeButton(getResources().getString(R.string.control_hide_alert_dialog_negative), null)
				.setPositiveButton(getResources().getString(R.string.control_hide_alert_dialog_positive), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						new HideRecentLogTask(getActivity().getApplication()).execute(ControlFragment.this.mLogToUpdate);
						ControlFragment.this.mListRecent.get(mLogToUpdate.type).remove(ControlFragment.this.mLogToUpdatePosition);
						ControlRecentItem item = ControlFragment.this.mListViewRecentAdapter.getItem(ControlFragment.this.mLogToUpdatePosition);
						ControlFragment.this.mListViewRecentAdapter.remove(
								item
						);
						ControlFragment.this.mListViewRecentAdapter.notifyDataSetChanged();
						dialogInterface.dismiss();
					}
				});

		builder.show();

		return true;
	}

	/** Method to receive click events from the ListView */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//		Log.v(ControlFragment.TAG, view.toString());
//		Log.v(ControlFragment.TAG, Integer.toString(position));
//		Log.v(ControlFragment.TAG, Long.toString(id));

		ControlRecentItem item = ((ControlRecentItem)mListViewControlRecent.getAdapter().getItem(position));
		Integer intParam = Integer.valueOf(item.getValue());
		String stringParam;
		Log.d(ControlFragment.TAG, intParam.toString());

		switch (item.getType()) {
			case ControlFragment.COMMAND_TEMP:
				ApiTemperature temp = new ApiTemperature(Integer.valueOf(item.getValue()), ApiTemperature.CONST_API_SCALE);
				Double tempDouble = temp.getTemperatureDouble(ApiTemperature.CONST_API_SCALE);
				stringParam = Integer.toString(tempDouble.intValue());

				break;

			case ControlFragment.COMMAND_TIME:
				Integer inputMinutes = Integer.valueOf(item.getValue());
//				Integer minutes = inputMinutes * 60;
				stringParam = inputMinutes.toString();

				break;

			default:
				throw new IllegalArgumentException();
		}

		Log.d(ControlFragment.TAG, String.format(
				"set %s to %s",
				((ControlRecentItem)mListViewControlRecent.getAdapter().getItem(position)).getType(),
				stringParam)
		);

		((ApiInterface)getActivity()).getApiService().sendCommand(
				item.getType(),
				stringParam,
				this.getApiHashString(item.getType(), stringParam),
				this);
	}

	public void toggleManualInput(String type) {
		if (sharedPrefs.getBoolean(Constants.CONST_SHOW_CONTROL_MANUAL_INPUT, true)) {
			switch (type) {
				case ControlFragment.COMMAND_TEMP:
					mTimeGroup.setVisibility(View.GONE);
					mTemperatureGroup.setVisibility(View.VISIBLE);

					break;

				case ControlFragment.COMMAND_TIME:
					mTimeGroup.setVisibility(View.VISIBLE);
					mTemperatureGroup.setVisibility(View.GONE);

					break;
			}
		} else {
			mTimeGroup.setVisibility(View.GONE);
			mTemperatureGroup.setVisibility(View.GONE);
		}
	}

	@Override
	public void success(ControlCommandResponse response, Response response2) {
		Log.d(TAG, response.toString());
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		if(response.error != null) {
			builder.setMessage(response.error.text);
		} else {
			builder.setMessage(getActivity().getString(R.string.control_alert_dialog_server_ok_message) + "\n" + response.result);
			mEditTextTime.setText(null);
			((ApiInterface)getActivity()).refreshControlValues();
		}
		builder.setNeutralButton(getActivity().getString(R.string.control_alert_dialog_dismiss), null);
		builder.show();
		this.manager.setApiNonce(null);
	}

	@Override
	public void failure(RetrofitError error) {
		Log.e(TAG, error.toString());
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
				.setTitle(getActivity().getString(R.string.control_alert_dialog_server_error_title))
				.setMessage(String.format(getActivity().getString(R.string.control_alert_dialog_server_error_message), error.getKind()))
				.setNeutralButton(getActivity().getString(R.string.control_alert_dialog_dismiss), null);
		builder.show();
	}

	private String getApiHashString(String command, String param) {

		String sharedSecret = this.manager.getApiSharedSecret();
		String nonce = this.manager.getApiNonce();
		if(nonce == null) {
			return null;
		}

		String hashMe = command.concat(param.concat(sharedSecret.concat(nonce)));
		return HashUtil.getInstance().getMessageDigestHash(hashMe);
	}

	private int temperatureToPercent(float temperature) {
		float percent = ((temperature - this.mMinimumTemperature) / (this.mMaximumTemperature - this.mMinimumTemperature)) * 100;
		Log.d(TAG, "temperatureToPercent: " + Float.valueOf(percent).toString());
		return (int)percent;
	}

	private float percentToTemperature(int percent) {
		float temperature = this.mMinimumTemperature + (this.mMaximumTemperature - this.mMinimumTemperature) * (float)(percent / 100.0);
		Log.d(TAG, "percentToTemperature: " + Float.valueOf(this.mMinimumTemperature + temperature).toString());
		return temperature;
	}
}
