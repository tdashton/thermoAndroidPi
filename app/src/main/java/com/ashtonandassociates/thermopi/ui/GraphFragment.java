package com.ashtonandassociates.thermopi.ui;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.ashtonandassociates.thermopi.R;
import com.ashtonandassociates.thermopi.util.AssetManager;
import com.ashtonandassociates.thermopi.util.Constants;
import com.ashtonandassociates.thermopi.util.FragmentVisibilitySaver;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class GraphFragment extends Fragment {

	private static final String TAG = GraphFragment.class.getSimpleName();
	private final FragmentVisibilitySaver visibilitySaver = new FragmentVisibilitySaver();

	protected WebView mWebView;
	protected String mUrl;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_graph, null);
		visibilitySaver.restoreVisibilityState(getFragmentManager(), this, savedInstanceState);
		mWebView = (WebView) view.findViewById(R.id.graph_webview);
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		loadUrl();

		return view;
	}

	public void loadUrl() {
		AssetManager am = AssetManager.getInstance(getResources(), R.raw.config);
		SharedPreferences sharedPrefs = getActivity().getSharedPreferences(Constants.CONST_SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
		String locationName = null;
		try {
			locationName = URLEncoder.encode(
					sharedPrefs.getString(Constants.CONST_LOCATION_NAME, getString(R.string.settings_location_name)),
					"UTF-8");
		} catch (UnsupportedEncodingException uee) {
			Log.e(TAG, uee.toString());
		}

		mUrl = am.getProperty(Constants.CONST_URL_BASE)
				.concat(am.getProperty(Constants.CONST_URL_PATH_WEBVIEW));
		if(locationName != null) {
			mUrl = mUrl.concat("?location_name=" + locationName);
		}

		mWebView.loadUrl(mUrl);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean("isHidden", isHidden());
	}
}
