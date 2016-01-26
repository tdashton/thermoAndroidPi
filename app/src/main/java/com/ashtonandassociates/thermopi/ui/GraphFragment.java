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
import com.ashtonandassociates.thermopi.util.FragmentVisibilitySaver;

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
		AssetManagerUtil am = AssetManagerUtil.getInstance(getResources(), R.raw.config);
		mUrl = am.getProperty(
				Constants.CONST_URL_BASE).concat(
							am.getProperty(Constants.CONST_URL_PATH_WEBVIEW));
		loadUrl();

		return view;
	}

	public void loadUrl() {
		mWebView.loadUrl(mUrl);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean("isHidden", isHidden());
	}
}
