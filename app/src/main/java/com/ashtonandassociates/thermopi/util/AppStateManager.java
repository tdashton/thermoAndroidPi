package com.ashtonandassociates.thermopi.util;

import android.content.Context;

import com.ashtonandassociates.thermopi.ui.list.element.ControlRecentItem;

import java.util.ArrayList;
import java.util.List;

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

	public List<Class> getRecentControlValues(Context ctx, String type) {
		List ary = new ArrayList<ControlRecentItem>();
		ary.add(new ControlRecentItem(ctx, type, Integer.valueOf(20).toString()));
		ary.add(new ControlRecentItem(ctx, type, Integer.valueOf(10).toString()));
		ary.add(new ControlRecentItem(ctx, type, Integer.valueOf(15).toString()));
//		ary.add(new ControlRecentItem(ctx, type, Integer.valueOf(25));
//		ary.add(new ControlRecentItem(ctx, type, Integer.valueOf(5));

		return ary;
	}
}
