package com.ashtonandassociates.thermopi;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.ashtonandassociates.thermopi.util.AssetManager;
import com.ashtonandassociates.thermopi.util.Constants;

public class SettingsActivity extends ActionBarActivity {

	private final String TAG = this.getClass().getSimpleName();

	public static final float TEMPERATURE_DEFAULT_MIN = 0;
	public static final float TEMPERATURE_DEFAULT_MAX = 25;

	EditText mTextViewSharedSecret;
	EditText mTextViewLocationName;
	EditText mTextViewURL;
	EditText mTextViewControlTemperatureMinimum;
	EditText mTextViewControlTemperatureMaximum;
	TextView mTextViewGcmTokenAvailability;
	CheckBox mCheckBoxRememberTab;
	CheckBox mCheckboxServerDebugOutput;

	public SharedPreferences sharedPrefs;

	private void sideloadSharedPrefs() {
		sharedPrefs = getSharedPreferences(Constants.CONST_SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
		if(sharedPrefs.getBoolean(Constants.CONST_USE_SHARED_SETTINGS, false) == false) {
			Log.v(TAG, "sideloading preferences from raw data file");
			// only end up here if the shared settings are not already set.
			// the shared settings are read from the raw resource on the first launch
			AssetManager assetManager = AssetManager.getInstance(getResources(), R.raw.config);
			SharedPreferences.Editor editor = sharedPrefs.edit();
			editor.putString(Constants.CONST_URL_BASE, assetManager.getProperty(Constants.CONST_URL_BASE));
			editor.putString(Constants.CONST_SERVER_SHARED_SECRET, assetManager.getProperty(Constants.CONST_SERVER_SHARED_SECRET));
			editor.putString(Constants.CONST_URL_PATH_WEBVIEW, assetManager.getProperty(Constants.CONST_URL_PATH_WEBVIEW));
			editor.putFloat(Constants.CONST_CONTROL_TEMPERATURE_MINIMUM, SettingsActivity.TEMPERATURE_DEFAULT_MIN);
			editor.putFloat(Constants.CONST_CONTROL_TEMPERATURE_MAXIMUM, SettingsActivity.TEMPERATURE_DEFAULT_MAX);
			editor.putBoolean(Constants.CONST_USE_SHARED_SETTINGS, true);
			editor.commit();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);

		sideloadSharedPrefs();
		sharedPrefs = getSharedPreferences(Constants.CONST_SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);

		mTextViewSharedSecret = (EditText)findViewById(R.id.settings_shared_secret);
		mTextViewLocationName = (EditText)findViewById(R.id.settings_location_name);
		mTextViewURL = (EditText)findViewById(R.id.settings_url_base);
		mCheckBoxRememberTab = (CheckBox)findViewById(R.id.settings_restore_tab);
		mCheckboxServerDebugOutput = (CheckBox)findViewById(R.id.settings_server_debug_output);
		mTextViewControlTemperatureMinimum = (EditText)findViewById(R.id.settings_control_temperature_minimum);
		mTextViewControlTemperatureMaximum = (EditText)findViewById(R.id.settings_control_temperature_maximum);
		mTextViewGcmTokenAvailability = (TextView)findViewById(R.id.settings_gcm_token_availability);
		mTextViewSharedSecret.setText(sharedPrefs.getString(Constants.CONST_SERVER_SHARED_SECRET, null));
		mTextViewLocationName.setText(sharedPrefs.getString(Constants.CONST_LOCATION_NAME, getString(R.string.settings_location_name)));
		mTextViewURL.setText(sharedPrefs.getString(Constants.CONST_URL_BASE, null));
		mCheckBoxRememberTab.setChecked(sharedPrefs.getBoolean(Constants.CONST_REMEMBER_LAST_FRAGMENT, true));
		Log.d(TAG, Constants.CONST_CONTROL_TEMPERATURE_MINIMUM);
		Log.d(TAG, Float.valueOf(SettingsActivity.TEMPERATURE_DEFAULT_MIN).toString());
		mTextViewControlTemperatureMinimum.setText(
				Float.valueOf(sharedPrefs.getFloat(Constants.CONST_CONTROL_TEMPERATURE_MINIMUM, SettingsActivity.TEMPERATURE_DEFAULT_MIN)).toString()
		);
		mTextViewControlTemperatureMaximum.setText(
				Float.valueOf(sharedPrefs.getFloat(Constants.CONST_CONTROL_TEMPERATURE_MAXIMUM, SettingsActivity.TEMPERATURE_DEFAULT_MAX)).toString()
		);
		mCheckboxServerDebugOutput.setChecked(sharedPrefs.getBoolean(Constants.CONST_SERVER_DEBUG_OUTPUT, false));
		if (sharedPrefs.getString(Constants.CONST_GCM_TOKEN, null) != null) {
			mTextViewGcmTokenAvailability.setText(getString(R.string.settings_gcm_token_available));
		} else {
			mTextViewGcmTokenAvailability.setText(getString(R.string.settings_gcm_token_not_available));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_settings, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		Log.v(TAG, "id: " + Integer.toString(id));
		if(id == android.R.id.home) {
			finish();
		} else if (id == R.id.settings_action_save) {
			Log.v(TAG, "save and finish()");
			if(this.saveSettings() == true) {
				finish();
			}
		}
		return false;
	}

	private boolean saveSettings() {
		SharedPreferences.Editor editor = sharedPrefs.edit();
		editor.putString(Constants.CONST_LOCATION_NAME, mTextViewLocationName.getText().toString());
		editor.putString(Constants.CONST_URL_BASE, mTextViewURL.getText().toString());
		editor.putString(Constants.CONST_SERVER_SHARED_SECRET, mTextViewSharedSecret.getText().toString());
		editor.putString(Constants.CONST_LOCATION_NAME, mTextViewLocationName.getText().toString());
		editor.putBoolean(Constants.CONST_REMEMBER_LAST_FRAGMENT, mCheckBoxRememberTab.isChecked());
		editor.putBoolean(Constants.CONST_SERVER_DEBUG_OUTPUT, mCheckboxServerDebugOutput.isChecked());
		editor.putFloat(Constants.CONST_CONTROL_TEMPERATURE_MINIMUM, Float.valueOf(mTextViewControlTemperatureMinimum.getText().toString()));
		editor.putFloat(Constants.CONST_CONTROL_TEMPERATURE_MAXIMUM, Float.valueOf(mTextViewControlTemperatureMaximum.getText().toString()));
		editor.putBoolean(Constants.CONST_USE_SHARED_SETTINGS, true);
		editor.commit();
		return true;
	}
}
