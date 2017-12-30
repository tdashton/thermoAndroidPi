package com.ashtonandassociates.thermopi.api;

import com.ashtonandassociates.thermopi.api.response.ControlLogsResponse;
import com.ashtonandassociates.thermopi.api.response.ControlReadResponse;
import com.ashtonandassociates.thermopi.api.response.NonceResponse;
import com.ashtonandassociates.thermopi.api.response.ControlCommandResponse;
import com.ashtonandassociates.thermopi.api.response.CurrentResponse;
import com.ashtonandassociates.thermopi.api.response.HistoryResponse;


import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

public interface ApiService {

	@GET("/control/nonce")
	void getApiNonce(Callback<NonceResponse> cb);

	@GET("/logs/history/json/{timePeriod}")
	void getLogsHistory(@Path("timePeriod") String timePeriod, Callback<HistoryResponse> cb);

	@GET("/logs/history/json/current")
	void getCurrent(Callback<CurrentResponse> cb);

	@FormUrlEncoded
	@POST("/control/command")
	void sendCommand(@Field("cmd") String command, @Field("param") String param, @Field("signature") String signature, Callback<ControlCommandResponse> cb);

	@GET("/control/read")
	void readCommandValue(Callback<ControlReadResponse> cb);

	@GET("/control/logs/")
	void readControlLogs(Callback<ControlLogsResponse> cb);
}
