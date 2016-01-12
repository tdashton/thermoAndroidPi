package com.ashtonandassociates.thermopi.ui;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.ashtonandassociates.thermopi.R;
import com.ashtonandassociates.thermopi.util.AssetManagerUtil;
import com.ashtonandassociates.thermopi.util.Constants;

public class GraphFragment extends Fragment {

//	protected WebView mWebView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_graph, null);
//		mWebView = (WebView) view.findViewById(R.id.graph_webview);
//		WebSettings webSettings = mWebView.getSettings();
//		webSettings.setJavaScriptEnabled(true);
//		AssetManagerUtil am = AssetManagerUtil.getInstance(getResources(), R.raw.config);
//		String url = am.getProperty(
//				Constants.CONST_URL_BASE).concat(
//					am.getProperty(Constants.CONST_URL_PATH).concat(
//							am.getProperty(Constants.CONST_URL_PATH_WEBVIEW)));
//		mWebView.loadUrl(url);
		return view;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putBoolean("fragHidden", isHidden());
		super.onSaveInstanceState(outState);
	}

	private void refreshValues() {
//		// Instantiate the RequestQueue.
//		RequestQueue queue = Volley.newRequestQueue(this.getActivity());
//		// do asset management
//		AssetManagerUtil am = AssetManagerUtil.getInstance(getResources(), R.raw.config);
//
//		String url = am.getProperty(Constants.CONST_URL_BASE).concat(am.getProperty(Constants.CONST_URL_PATH));
//
//		// Request a string response from the provided URL.
//		StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//				new Response.Listener<String>() {
//					@Override
//					public void onResponse(String response) {
//						// Display the first 500 characters of the response string.
//						mTextView.setText("Response is: " + response.substring(0, 500));
//					}
//				}, new Response.ErrorListener() {
//			@Override
//			public void onErrorResponse(VolleyError error) {
//				mTextView.setText("That didn't work!");
//			}
//		});
//		// Add the request to the RequestQueue.
//		queue.add(stringRequest);

	}
}
