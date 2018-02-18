package com.ashtonandassociates.thermopi.util;

import java.util.Date;

public class AppStateManager {

	private static AppStateManager instance = null;

	private AppStateManager() {}

	public static AppStateManager getInstance() {
		if(AppStateManager.instance == null) {
			AppStateManager.instance = new AppStateManager();
			AppStateManager.instance.apiNonceExpires = new Date(0);
		}
		return instance;
	}

	private String apiNonce = null;
	private Date apiNonceExpires = null;
	private String apiSharedSecret = null;

	public void setApiNonce(String param, Date expires) {
		this.apiNonceExpires = expires;
		this.apiNonce = param;
	}

	public void setApiNonce(String param) {
		long now = new Date().getTime();
		this.setApiNonce(param, new Date(now + 10 * 60 * 1000));
	}

	public String getApiNonce() {
		return this.apiNonce;
	}

	public boolean hasApiNonce() {
		boolean expired = this.apiNonceExpires.before(new Date());
		return this.apiNonce != null && !expired;
	}

	public String getApiSharedSecret() {
		return this.apiSharedSecret;
	}

	public void setApiSharedSecret(String param) {
		this.apiSharedSecret = param;
	}
}
