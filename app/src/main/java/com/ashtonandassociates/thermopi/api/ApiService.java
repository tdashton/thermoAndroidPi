package com.ashtonandassociates.thermopi.api;

import com.ashtonandassociates.thermopi.api.response.ApiNonceResponse;
import com.ashtonandassociates.thermopi.api.response.ControlResponse;
import com.ashtonandassociates.thermopi.api.response.CurrentResponse;
import com.ashtonandassociates.thermopi.api.response.HistoryResponse;


import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

public interface ApiService {

	@GET("/control/nonce/")
	void getApiNonce(Callback<ApiNonceResponse> cb);

	@GET("/logs/history/json/{timePeriod}")
	void getLogsHistory(@Path("timePeriod") String timePeriod, Callback<HistoryResponse> cb);

	@GET("/logs/history/json/current")
	void getCurrent(Callback<CurrentResponse> cb);

	@FormUrlEncoded
	@POST("/control/command/")
	void sendCommand(@Field("cmd") String command, @Field("param") String param, Callback<ControlResponse> cb);
}
