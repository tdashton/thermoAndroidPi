package ashtonandassociates.com.thermopi.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import ashtonandassociates.com.thermopi.R;
import ashtonandassociates.com.thermopi.util.AssetManagerUtil;
import ashtonandassociates.com.thermopi.util.Constants;

public class OverviewFragment extends Fragment {

	protected TextView mTextView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_overview, null);
		mTextView = (TextView) view.findViewById(R.id.sensor_1);

		// Instantiate the RequestQueue.
		RequestQueue queue = Volley.newRequestQueue(this.getActivity());
		// do asset management
		AssetManagerUtil am = new AssetManagerUtil(getResources(), R.raw.config);

		String url = am.getProperty(Constants.CONST_URL_BASE).concat(am.getProperty(Constants.CONST_URL_PATH).concat("?current=true"));

		JsonObjectRequest jsObjRequest = new JsonObjectRequest
				(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						mTextView.setText("Response: " + response.toString());
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						mTextView.setText("That didn't work!");
					}
				});

		queue.add(jsObjRequest);

		return view;
	}
}
