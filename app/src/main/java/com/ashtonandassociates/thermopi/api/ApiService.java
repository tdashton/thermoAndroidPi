package com.ashtonandassociates.thermopi.api;

import com.ashtonandassociates.thermopi.api.response.CurrentResponse;
import com.ashtonandassociates.thermopi.api.response.HistoryResponse;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

public interface ApiService {

	@GET("/logs/history/json/{timePeriod}")
	void getLogsHistory(@Path("timePeriod") String timePeriod, Callback<HistoryResponse> cb);

	@GET("/logs/history/json/current")
	void getCurrent(Callback<CurrentResponse> cb);

	@POST("/control/command/")
	void sendCommand(@Field("cmd") String command, @Field("param") String param);
}
