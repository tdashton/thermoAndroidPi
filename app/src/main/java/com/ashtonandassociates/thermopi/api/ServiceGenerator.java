package com.ashtonandassociates.thermopi.api;

import android.content.SharedPreferences;

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

	private static RestAdapter.Builder builder = new RestAdapter.Builder();
	public static CookieManager cookieManager = new CookieManager();

	public static <S> S createService(Class<S> serviceClass, SharedPreferences sharedPrefs) {
		String url = sharedPrefs.getString(Constants.CONST_URL_BASE, "http://localhost/");

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
