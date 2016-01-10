package com.ashtonandassociates.thermopi;

import android.app.FragmentManager;
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

import com.ashtonandassociates.thermopi.ui.ControlFragment;
import com.ashtonandassociates.thermopi.ui.GraphFragment;
import com.ashtonandassociates.thermopi.ui.OverviewFragment;

public class MainActivity extends ActionBarActivity {

	final private String TAG = getClass().getSimpleName();

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private String[] mDrawerItems;

	private Fragment mMainFragment;
	private Fragment mGraphFragment;
	private Fragment mControlFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			mMainFragment = new OverviewFragment();
			mGraphFragment = new GraphFragment();
			mControlFragment = new ControlFragment();
			getFragmentManager().beginTransaction()
				.add(R.id.container, mMainFragment, "mMainFragment")
				.add(R.id.container, mGraphFragment, "mGraphFragment")
				.add(R.id.container, mControlFragment, "mControlFragment")
				.hide(mGraphFragment)
				.hide(mControlFragment)
				.commit();
		} else {
			mMainFragment = getFragmentManager().findFragmentByTag("mMainFragment");
			mGraphFragment = getFragmentManager().findFragmentByTag("mGraphFragment");
			mControlFragment = getFragmentManager().findFragmentByTag("mControlFragment");
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

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	protected class DrawerItemClickListener implements ListView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			Log.v(TAG, "arg1: " + arg1.toString());
			Log.v(TAG, "arg2: " + new Integer(arg2).toString());
			Log.v(TAG, "arg3: " + new Long(arg3).toString());
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
						.hide(mControlFragment)
						.hide(mGraphFragment)
						.commit();
				break;

			case 1:
				fragmentManager.beginTransaction()
						.show(mGraphFragment)
						.hide(mControlFragment)
						.hide(mMainFragment)
						.commit();
				break;

			case 2:
				fragmentManager.beginTransaction()
						.hide(mGraphFragment)
						.show(mControlFragment)
						.hide(mMainFragment)
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
			Log.i("id", new Integer(id).toString());
			return true;
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
}
