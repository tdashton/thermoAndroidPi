package ashtonandassociates.com.thermopi.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ashtonandassociates.com.thermopi.R;
import ashtonandassociates.com.thermopi.util.AssetManagerUtil;
import ashtonandassociates.com.thermopi.util.Constants;

public class OverviewFragment extends Fragment {

	protected TextView mSensor1Label;
	protected TextView mSensor1Value;
	protected TextView mSensor2Label;
	protected TextView mSensor2Value;
	protected TextView mSensorDate;

	private void refreshValues() {
		// Instantiate the RequestQueue.
		RequestQueue queue = Volley.newRequestQueue(this.getActivity());
		// do asset management
		AssetManagerUtil am = AssetManagerUtil.getInstance(getResources(), R.raw.config);
		setHasOptionsMenu(true);
		String url = am.getProperty(Constants.CONST_URL_BASE).concat(am.getProperty(Constants.CONST_URL_PATH).concat("?current=true"));

		JsonObjectRequest jsObjRequest = new JsonObjectRequest
				(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						JSONArray responseData;
						try {
							responseData = response.getJSONArray("data");
							mSensorDate.setText(((JSONObject)responseData.get(0)).get("datetime").toString());
							mSensor1Label.setText(((JSONObject)responseData.get(0)).get("description").toString());
							mSensor1Value.setText(((JSONObject)responseData.get(0)).get("value").toString());
							mSensor2Label.setText(((JSONObject)responseData.get(1)).get("description").toString());
							mSensor2Value.setText(((JSONObject)responseData.get(1)).get("value").toString());
						} catch (JSONException je) {
							Log.e(this.getClass().getSimpleName(), je.getMessage());
						}

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						mSensor1Label.setText("That didn't work!");
					}
				});

		queue.add(jsObjRequest);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_overview, null);
		mSensorDate = (TextView) view.findViewById(R.id.sensor_date);
		mSensor1Label = (TextView) view.findViewById(R.id.sensor_1_label);
		mSensor1Value = (TextView) view.findViewById(R.id.sensor_1_value);
		mSensor2Label = (TextView) view.findViewById(R.id.sensor_2_label);
		mSensor2Value = (TextView) view.findViewById(R.id.sensor_2_value);
		if(savedInstanceState == null) {
			refreshValues();
		}

		return view;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("dateString", mSensorDate.getText().toString());
		outState.putString("valueOne", mSensor1Label.getText().toString());
		outState.putString("valuetwo", mSensor2Label.getText().toString());
	}

	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		super.onViewStateRestored(savedInstanceState);
		if(savedInstanceState != null) {
			mSensorDate.setText(savedInstanceState.getString("dateString"));
			mSensor1Value.setText(savedInstanceState.getString("valueOne"));
			mSensor2Value.setText(savedInstanceState.getString("valueTwo"));
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			// Inflate the menu; this adds items to the action bar if it is present.
		getActivity().getMenuInflater().inflate(R.menu.menu_graph, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.i("i", item.toString());
		refreshValues();
		return super.onOptionsItemSelected(item);
	}
}
