package com.ashtonandassociates.thermopi.api;

public interface ApiInterface {

	ApiService getApiService();

	void getApiNonce();

	void refreshCurrentValues();

	void refreshControlValues();

	void refreshControlLogValues();
}
