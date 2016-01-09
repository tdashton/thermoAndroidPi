package com.ashtonandassociates.thermopi.api;

import android.content.res.Resources;

import com.ashtonandassociates.thermopi.R;
import com.ashtonandassociates.thermopi.util.AssetManagerUtil;
import com.ashtonandassociates.thermopi.util.Constants;
import com.squareup.okhttp.OkHttpClient;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

public class ServiceGenerator {

//	public static  String API_BASE_URL = "http://your.api-base.url";

	private static RestAdapter.Builder builder = new RestAdapter.Builder();

	public static <S> S createService(Class<S> serviceClass, Resources res) {
		AssetManagerUtil am = AssetManagerUtil.getInstance(res, R.raw.config);
		String url = am.getProperty(Constants.CONST_URL_BASE).concat(am.getProperty(Constants.CONST_URL_PATH));
		builder.setEndpoint(url)
				.setClient(new OkClient(new OkHttpClient()))
				.setLogLevel(RestAdapter.LogLevel.BASIC);
		RestAdapter adapter = builder.build();
		return adapter.create(serviceClass);
	}
}
