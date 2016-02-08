package com.ashtonandassociates.thermopi;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;

import com.ashtonandassociates.thermopi.util.AssetManagerUtil;
import com.ashtonandassociates.thermopi.util.Constants;

public class SettingsActivity extends ActionBarActivity {

	private final String TAG = this.getClass().getSimpleName();

	EditText mTextViewSharedSecret;
	EditText mTextViewLocationName;
	EditText mTextViewURL;
	CheckBox mCheckBoxRememberTab;

	public SharedPreferences sharedPrefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);

		sharedPrefs = getPreferences(MODE_PRIVATE);

		mTextViewSharedSecret = (EditText)findViewById(R.id.settings_shared_secret);
		mTextViewLocationName = (EditText)findViewById(R.id.settings_location_name);
		mTextViewURL = (EditText)findViewById(R.id.settings_url_base);
		mCheckBoxRememberTab = (CheckBox)findViewById(R.id.settings_restore_tab);

		AssetManagerUtil util = AssetManagerUtil.getInstance(this.getResources(), R.raw.config);
		mTextViewSharedSecret.setText(util.getProperty(Constants.CONST_SERVER_SHARED_SECRET));
		mTextViewLocationName.setText(sharedPrefs.getString(Constants.CONST_LOCATION_NAME, getString(R.string.settings_location_name)));
		mTextViewURL.setText(util.getProperty(Constants.CONST_URL_BASE));
		mCheckBoxRememberTab.setChecked(sharedPrefs.getBoolean(Constants.CONST_REMEMBER_LAST_TAB, true));
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
		editor.putString(Constants.CONST_URL_BASE, mTextViewURL.getText().toString());
		editor.putString(Constants.CONST_SERVER_SHARED_SECRET, mTextViewSharedSecret.getText().toString());
		editor.putString(Constants.CONST_LOCATION_NAME, mTextViewLocationName.getText().toString());
		editor.putBoolean(Constants.CONST_REMEMBER_LAST_TAB, mCheckBoxRememberTab.isChecked());
		editor.commit();
		return true;
	}
}
