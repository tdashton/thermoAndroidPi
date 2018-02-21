package com.ashtonandassociates.thermopi;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ashtonandassociates.thermopi.api.annotation.ApiListener;
import com.ashtonandassociates.thermopi.api.ApiService;
import com.ashtonandassociates.thermopi.api.ServiceGenerator;
import com.ashtonandassociates.thermopi.api.response.ControlLogsResponse;
import com.ashtonandassociates.thermopi.api.response.ControlReadResponse;
import com.ashtonandassociates.thermopi.api.response.NonceResponse;
import com.ashtonandassociates.thermopi.api.response.CurrentResponse;
import com.ashtonandassociates.thermopi.api.ApiInterface;
import com.ashtonandassociates.thermopi.persistence.InsertRecentLogsTask;
import com.ashtonandassociates.thermopi.persistence.entity.RecentLog;
import com.ashtonandassociates.thermopi.ui.ControlFragment;
import com.ashtonandassociates.thermopi.ui.DebugFragment;
import com.ashtonandassociates.thermopi.ui.GraphFragment;
import com.ashtonandassociates.thermopi.ui.OverviewFragment;
import com.ashtonandassociates.thermopi.ui.drawer.DrawerAdapter;
import com.ashtonandassociates.thermopi.ui.drawer.DrawerItem;
import com.ashtonandassociates.thermopi.util.AppStateManager;
import com.ashtonandassociates.thermopi.util.Constants;
import com.google.common.collect.Lists;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity
	implements ApiInterface {

	private final String TAG = this.getClass().getSimpleName();

	protected static final int SETTINGS_ACTIVITY_REQUEST = 1;

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	public SharedPreferences sharedPrefs;

	private Fragment mMainFragment;
	private Fragment mGraphFragment;
	private Fragment mControlFragment;
	private Fragment mDebugFragment;

	private ApiService service;
	private Callback<NonceResponse> mNonceResponseCallback;
	private Callback<CurrentResponse> mCurrentResponseCallback;
	private Callback<ControlLogsResponse> mControlHistoryResponseCallback;
	private Callback<ControlReadResponse> mControlReadResponseCallback;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		sharedPrefs = getSharedPreferences(Constants.CONST_SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
		service = ServiceGenerator.createService(ApiService.class, sharedPrefs);
		// enable ActionBar app icon to behave as action to toggle nav drawer
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);

		mMainFragment = getSupportFragmentManager().findFragmentByTag("mMainFragment");
		if(mMainFragment == null) {
			mMainFragment = new OverviewFragment();
		}
		mGraphFragment = getSupportFragmentManager().findFragmentByTag("mGraphFragment");
		if(mGraphFragment == null) {
			mGraphFragment = new GraphFragment();
		}
		mControlFragment = getSupportFragmentManager().findFragmentByTag("mControlFragment");
		if(mControlFragment == null) {
			mControlFragment = new ControlFragment();
		}
		mDebugFragment = getSupportFragmentManager().findFragmentByTag("mDebugFragment");
		if(mDebugFragment == null) {
			mDebugFragment = new DebugFragment();
		}

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.content_frame, mMainFragment, "mMainFragment")
					.add(R.id.content_frame, mGraphFragment, "mGraphFragment")
					.add(R.id.content_frame, mControlFragment, "mControlFragment")
					.add(R.id.content_frame, mDebugFragment, "mDebugFragment")
					.hide(mGraphFragment)
					.hide(mControlFragment)
					.hide(mDebugFragment)
					.commit();
		}

//		Log.v(TAG, mMainFragment.toString());
//		Log.v(TAG, mGraphFragment.toString());
//		Log.v(TAG, mControlFragment.toString());

		ArrayList<DrawerItem> mDrawerItems = new ArrayList<>();
		mDrawerItems.add(new DrawerItem(getString(R.string.drawer_menu_item_current), R.drawable.ic_announcement_white_24dp));
		mDrawerItems.add(new DrawerItem(getString(R.string.drawer_menu_item_log), R.drawable.ic_assessment_white_24dp));
		mDrawerItems.add(new DrawerItem(getString(R.string.drawer_menu_item_control), R.drawable.ic_settings_power_white_24dp));
		if (sharedPrefs.getBoolean(Constants.CONST_SERVER_DEBUG_OUTPUT, false)) {
			mDrawerItems.add(new DrawerItem(getString(R.string.drawer_menu_item_debug), R.drawable.ic_bug_report_white_24dp));
		}

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// Set the adapter for the list view
		mDrawerList.setAdapter(new DrawerAdapter(this, R.layout.drawer_item, mDrawerItems));
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.app_name) {

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				getSupportActionBar().setTitle(R.string.drawer_open);
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}

			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				getSupportActionBar().setTitle(R.string.app_name);
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}
		};

		mDrawerLayout.addDrawerListener(mDrawerToggle);

		checkForSharedPreferences();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(service == null) {
			service = ServiceGenerator.createService(ApiService.class, sharedPrefs);
		}
		if(this.mControlFragment.isHidden() == false) {
			refreshControlValues();
		}
		refreshControlLogValues();
		refreshCurrentValues();
	}

	@Override
	protected void onStart() {
		super.onStart();
		if(sharedPrefs.getBoolean(Constants.CONST_REMEMBER_LAST_FRAGMENT, false)) {
			int lastFragment = sharedPrefs.getInt(Constants.CONST_LAST_FRAGMENT, 0);
			this.selectItem(lastFragment);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		SharedPreferences.Editor editor = sharedPrefs.edit();
		int active = 0;
		if(mGraphFragment.isVisible()) {
			active = 1;
		} else if (mControlFragment.isVisible()) {
			active = 2;
		}
		editor.putInt(Constants.CONST_LAST_FRAGMENT, active);
		editor.commit();
	}

	protected class DrawerItemClickListener implements ListView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			Log.v(TAG, "arg1: " + arg1.toString());
			Log.v(TAG, "arg2: " + Integer.toString(arg2));
			Log.v(TAG, "arg3: " + Long.toString(arg3));
			selectItem(arg2);
		}
	}

	/** Swaps fragments in the main content view */
	private void selectItem(int position) {
		// Create a new fragment and specify the planet to show based on position
		FragmentManager fragmentManager = getSupportFragmentManager();
		this.mDrawerList.setItemChecked(position, true);

		switch(position) {
			case 0:
			default:
				fragmentManager.beginTransaction()
						.show(mMainFragment)
						.hide(mGraphFragment)
						.hide(mControlFragment)
						.hide(mDebugFragment)
						.commit();
				break;

			case 1:
				fragmentManager.beginTransaction()
						.hide(mMainFragment)
						.show(mGraphFragment)
						.hide(mControlFragment)
						.hide(mDebugFragment)
						.commit();
				break;

			case 2:
				this.getApiNonce();
				fragmentManager.beginTransaction()
						.hide(mMainFragment)
						.hide(mGraphFragment)
						.show(mControlFragment)
						.hide(mDebugFragment)
						.commit();
				break;

			case 3:
				fragmentManager.beginTransaction()
						.hide(mMainFragment)
						.hide(mGraphFragment)
						.hide(mControlFragment)
						.show(mDebugFragment)
						.commit();
				break;
		}
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		if (mDrawerToggle.onOptionsItemSelected(item)) {
			Log.v(TAG, "id: " + Integer.toString(id));
			return true;
		} else if (id == R.id.action_refresh) {
			refreshCurrentValues();
			refreshControlLogValues();
			if(this.mControlFragment.isHidden() == false) {
				refreshControlValues();
			}
			if(this.mGraphFragment.isHidden() == false && this.mGraphFragment instanceof GraphFragment) {
				((GraphFragment) this.mGraphFragment).loadUrl();
			}
		} else if (id == R.id.action_generate_nonce) {
			getApiNonce();
		} else if (id == R.id.action_settings) {
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivityForResult(intent, this.SETTINGS_ACTIVITY_REQUEST);
		}
		return false;
	}

	@Override
	public void onBackPressed() {
		if(this.mDrawerLayout.isDrawerOpen(mDrawerList)) {
			mDrawerLayout.closeDrawer(mDrawerList);
			return;
		}
		super.onBackPressed();
	}

	public ApiService getApiService() {
		return this.service;
	}

	@Override
	public void getApiNonce() {
		if (AppStateManager.getInstance().hasApiNonce()) {
			return;
		}
		if(mNonceResponseCallback != null) {
			return;
		}
		mNonceResponseCallback = new Callback<NonceResponse>() {
			@Override
			public void success(NonceResponse apiNonceResponse, Response response) {
				AppStateManager manager = AppStateManager.getInstance();
				manager.setApiNonce(apiNonceResponse.nonce);
				manager.setApiSharedSecret(sharedPrefs.getString(Constants.CONST_SERVER_SHARED_SECRET, ""));
				Log.v(TAG, "got nonce: " + apiNonceResponse.nonce);
//				notifyApiListeners(apiNonceResponse);
				mNonceResponseCallback = null;
			}

			@Override
			public void failure(RetrofitError error) {
				Log.e(TAG, error.toString());
				mNonceResponseCallback = null;
			}
		};
		service.getApiNonce(mNonceResponseCallback);
	}

	@Override
	public void refreshControlLogValues()
	{
		if(mControlHistoryResponseCallback != null) {
			return;
		}
		mControlHistoryResponseCallback = new Callback<ControlLogsResponse>() {
			@Override
			public void success(ControlLogsResponse controlLogsResponse, Response response) {
				List<ControlLogsResponse.Result> result = controlLogsResponse.result;
				List<RecentLog> daos = Lists.transform(result, RecentLog.transform());

				RecentLog[] daosParams = new RecentLog[daos.size()];
				daosParams = daos.toArray(daosParams);

				AsyncTask insertTask = new InsertRecentLogsTask(getApplication(), MainActivity.this);
				insertTask.execute(daosParams);

				mControlHistoryResponseCallback = null;
			}

			@Override
			public void failure(RetrofitError error) {
				Log.d(TAG, error.toString());
				mControlHistoryResponseCallback = null;
			}
		};
		service.readControlLogs(this.mControlHistoryResponseCallback);
	}

	@Override
	public void refreshCurrentValues() {
		if(mCurrentResponseCallback != null) {
			return;
		}
		mCurrentResponseCallback = new Callback<CurrentResponse>() {
			@Override
			public void success(CurrentResponse currentResponse, Response response) {
				notifyApiListeners(currentResponse);
				mCurrentResponseCallback = null;
			}

			@Override
			public void failure(RetrofitError error) {
				Log.d(TAG, error.toString());
				mCurrentResponseCallback = null;
			}
		};
		service.getCurrent(mCurrentResponseCallback);
	}

	public void refreshControlValues() {
		if(mControlReadResponseCallback!= null) {
			return;
		}
		mControlReadResponseCallback = new Callback<ControlReadResponse>() {
			@Override
			public void success(ControlReadResponse controlReadResponse, Response response) {
				notifyApiListeners(controlReadResponse);
				mControlReadResponseCallback = null;
			}

			@Override
			public void failure(RetrofitError error) {
				Log.e(TAG, error.toString());
				mControlReadResponseCallback = null;
			}
		};
		service.readCommandValue(mControlReadResponseCallback);
	}

	public void notifyApiListeners(Object responseClass) {
		Fragment[] fragments = {mMainFragment, mControlFragment, mGraphFragment, mDebugFragment};
		for(Fragment frag : fragments) {
			if(frag == null) {
				return;
			}
			Class theClass = frag.getClass();
			try	{
				for(Method met : theClass.getMethods()){
					if(met.isAnnotationPresent(ApiListener.class)) {
						Class annotationResponseClass = met.getAnnotation(ApiListener.class).value();
						if(annotationResponseClass == responseClass.getClass()) {
							met.invoke(frag, responseClass);
						}
					}
				}
			} catch(IllegalAccessException iae) {
				Log.e(TAG, iae.toString());
			} catch(InvocationTargetException ite) {
				Log.e(TAG, "ite " + ite.toString());
			}
		}
	}

	private void checkForSharedPreferences() {
		sharedPrefs = getSharedPreferences(Constants.CONST_SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
		if (sharedPrefs.getBoolean(Constants.CONST_USE_SHARED_SETTINGS, false) == false) {
			Log.v(TAG, "no prefs found, showing the settings activity");
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivityForResult(intent, this.SETTINGS_ACTIVITY_REQUEST);
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == SETTINGS_ACTIVITY_REQUEST) {
			if (resultCode == RESULT_OK) {
				this.service = null;
				Log.v(TAG, "nulled ApiService, onResume should regenerate it");
			}
		}
	}
}
