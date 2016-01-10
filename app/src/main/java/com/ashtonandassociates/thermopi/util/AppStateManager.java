package com.ashtonandassociates.thermopi.util;

import android.content.res.AssetManager;

/**
 * Created by theKernel on 10.01.2016.
 */
public class AppStateManager {

	private static AppStateManager instance = null;

	private AppStateManager() {}

	public static AppStateManager getInstance() {
		if(AppStateManager.instance == null) {
			AppStateManager.instance = new AppStateManager();
		}
		return instance;
	}

	private String apiNonce = null;
	private String apiSharedSecret = null;

	public void setApiNonce(String param) {
		this.apiNonce = param;
	}

	public String getApiNonce() {
		return this.apiNonce;
	}

	public String getApiSharedSecret() {
		return this.apiSharedSecret;
	}

	public void setApiSharedSecret(String param) {
		this.apiSharedSecret = param;
	}

}
