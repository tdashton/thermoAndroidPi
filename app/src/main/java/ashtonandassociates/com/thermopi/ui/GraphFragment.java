package ashtonandassociates.com.thermopi.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import ashtonandassociates.com.thermopi.R;
import ashtonandassociates.com.thermopi.util.AssetManagerUtil;
import ashtonandassociates.com.thermopi.util.Constants;

public class GraphFragment extends Fragment {

	protected TextView mTextView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_graph, null);
		mTextView = (TextView) view.findViewById(R.id.textview);

		// Instantiate the RequestQueue.
		RequestQueue queue = Volley.newRequestQueue(this.getActivity());
		// do asset management
		AssetManagerUtil am = AssetManagerUtil.getInstance(getResources(), R.raw.config);

		String url = am.getProperty(Constants.CONST_URL_BASE).concat(am.getProperty(Constants.CONST_URL_PATH));

		// Request a string response from the provided URL.
		StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						// Display the first 500 characters of the response string.
						mTextView.setText("Response is: " + response.substring(0, 500));
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				mTextView.setText("That didn't work!");
			}
		});
		// Add the request to the RequestQueue.
		queue.add(stringRequest);

		return view;
	}
}
