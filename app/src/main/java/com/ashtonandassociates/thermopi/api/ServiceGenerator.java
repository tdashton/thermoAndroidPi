package com.ashtonandassociates.thermopi.api;

import android.content.res.Resources;

import com.ashtonandassociates.thermopi.R;
import com.ashtonandassociates.thermopi.util.AssetManagerUtil;
import com.ashtonandassociates.thermopi.util.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import java.net.CookieManager;
import java.net.CookiePolicy;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

public class ServiceGenerator {

//	public static  String API_BASE_URL = "http://your.api-base.url";

	private static RestAdapter.Builder builder = new RestAdapter.Builder();
	public static CookieManager cookieManager = new CookieManager();

	public static <S> S createService(Class<S> serviceClass, Resources res) {
		AssetManagerUtil am = AssetManagerUtil.getInstance(res, R.raw.config);
		String url = am.getProperty(Constants.CONST_URL_BASE);
		OkHttpClient httpClient = new OkHttpClient();

		cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
		httpClient.setCookieHandler(cookieManager);

		Gson gson = new GsonBuilder()
				.setDateFormat("yyyy-MM-dd' 'HH:mm:ss")
				.create();

		builder.setEndpoint(url)
				.setConverter(new GsonConverter(gson))
				.setClient(new OkClient(httpClient))
				.setLogLevel(RestAdapter.LogLevel.BASIC);
		RestAdapter adapter = builder.build();
		return adapter.create(serviceClass);
	}
}
