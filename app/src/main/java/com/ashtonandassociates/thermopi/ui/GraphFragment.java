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

	protected WebView mWebView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_graph, null);
		mWebView = (WebView) view.findViewById(R.id.graph_webview);
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		AssetManagerUtil am = AssetManagerUtil.getInstance(getResources(), R.raw.config);
		String url = am.getProperty(
				Constants.CONST_URL_BASE).concat(
					am.getProperty(Constants.CONST_URL_PATH).concat(
							am.getProperty(Constants.CONST_URL_PATH_WEBVIEW)));
		mWebView.loadUrl(url);
		return view;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
}
