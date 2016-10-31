package com.ashtonandassociates.thermopi.interfaces;

import com.ashtonandassociates.thermopi.api.ApiService;

public interface ApiInterface {

	ApiService getApiService();

	void getApiNonce();

	void refreshCurrentValues();

	void refreshControlValues();

}
