package com.ashtonandassociates.thermopi;

import android.app.FragmentManager;
import android.content.Intent;
import android.media.audiofx.BassBoost;
import android.support.v4.widget.DrawerLayout;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
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
import com.ashtonandassociates.thermopi.api.response.ControlReadResponse;
import com.ashtonandassociates.thermopi.api.response.NonceResponse;
import com.ashtonandassociates.thermopi.api.response.CurrentResponse;
import com.ashtonandassociates.thermopi.interfaces.ApiInterface;
import com.ashtonandassociates.thermopi.ui.ControlFragment;
import com.ashtonandassociates.thermopi.ui.GraphFragment;
import com.ashtonandassociates.thermopi.ui.OverviewFragment;
import com.ashtonandassociates.thermopi.util.AppStateManager;
import com.ashtonandassociates.thermopi.util.AssetManagerUtil;
import com.ashtonandassociates.thermopi.util.Constants;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends ActionBarActivity
	implements ApiInterface {

	private static final String TAG = MainActivity.class.getSimpleName();

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private String[] mDrawerItems;

	private Fragment mMainFragment;
	private Fragment mGraphFragment;
	private Fragment mControlFragment;

	private ApiService service;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		service = ServiceGenerator.createService(ApiService.class, getResources());
		// enable ActionBar app icon to behave as action to toggle nav drawer
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);

		mMainFragment = getFragmentManager().findFragmentByTag("mMainFragment");
		if(mMainFragment == null) {
			mMainFragment = new OverviewFragment();
		}
		mGraphFragment = getFragmentManager().findFragmentByTag("mGraphFragment");
		if(mGraphFragment == null) {
			mGraphFragment = new GraphFragment();
		}
		mControlFragment = getFragmentManager().findFragmentByTag("mControlFragment");
		if(mControlFragment == null) {
			mControlFragment = new ControlFragment();
		}

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.replace(R.id.content_frame, mMainFragment, "mMainFragment")
					.add(R.id.content_frame, mGraphFragment, "mGraphFragment")
					.add(R.id.content_frame, mControlFragment, "mControlFragment")
					.hide(mGraphFragment)
					.hide(mControlFragment)
					.commit();
		}

		Log.v(TAG, mMainFragment.toString());
		Log.v(TAG, mGraphFragment.toString());
		Log.v(TAG, mControlFragment.toString());

		mDrawerItems = getResources().getStringArray(R.array.drawer_menu_items);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// Set the adapter for the list view
		mDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item, mDrawerItems));
		mDrawerList.setSelection(1);
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

		mDrawerLayout.setDrawerListener(mDrawerToggle);

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
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
		FragmentManager fragmentManager = getFragmentManager();

		switch(position) {
			case 0:
			default:
				fragmentManager.beginTransaction()
						.show(mMainFragment)
						.hide(mGraphFragment)
						.hide(mControlFragment)
						.commit();
				break;

			case 1:
				fragmentManager.beginTransaction()
						.hide(mMainFragment)
						.show(mGraphFragment)
						.hide(mControlFragment)
						.commit();
				break;

			case 2:
				getApiNonce();
				fragmentManager.beginTransaction()
						.hide(mMainFragment)
						.hide(mGraphFragment)
						.show(mControlFragment)
						.commit();
				break;

			case 3:
				Intent intent = new Intent(this, SettingsActivity.class);
				startActivity(intent);
				mDrawerList.clearChoices();
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
			Log.i("id", Integer.toString(id));
			return true;
		} else if (id == R.id.action_refresh) {
			refreshCurrentValues();
			if(this.mControlFragment.isHidden() == false) {
				refreshControlValues();
			}
		} else if (id == R.id.action_generate_nonce) {
			getApiNonce();
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

	protected void getApiNonce() {
		service.getApiNonce(new Callback<NonceResponse>() {
			@Override
			public void success(NonceResponse apiNonceResponse, Response response) {
				AppStateManager manager = AppStateManager.getInstance();
				AssetManagerUtil util = AssetManagerUtil.getInstance(getResources(), R.raw.config);
				manager.setApiNonce(apiNonceResponse.nonce);
				manager.setApiSharedSecret(util.getProperty(Constants.CONST_SERVER_SHARED_SECRET));
				//Log.v(TAG, "hashme: " + apiNonceResponse.nonce);
//				notifyApiListeners(apiNonceResponse);
			}

			@Override
			public void failure(RetrofitError error) {
				Log.e(TAG, error.toString());
			}
		});
	}

	@Override
	public void refreshCurrentValues() {
		service.getCurrent(new Callback<CurrentResponse>() {
			@Override
			public void success(CurrentResponse currentResponse, Response response) {
				notifyApiListeners(currentResponse);
			}

			@Override
			public void failure(RetrofitError error) {
				Log.d(TAG, error.toString());
			}
		});
	}

	@Override
	public void refreshControlValues() {
		service.readCommandValue(new Callback<ControlReadResponse>() {
			@Override
			public void success(ControlReadResponse controlReadResponse, Response response) {
				notifyApiListeners(controlReadResponse);
			}

			@Override
			public void failure(RetrofitError error) {
				Log.e(TAG, error.toString());
			}
		});
	}

	private void notifyApiListeners(Object responseClass) {
		Fragment[] fragments = {mMainFragment, mControlFragment, mGraphFragment};
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
}
