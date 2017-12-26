package com.ashtonandassociates.thermopi.util;

import com.ashtonandassociates.thermopi.ui.ControlFragment;
import com.ashtonandassociates.thermopi.ui.controlvalue.Item;

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

	public List<Class> getRecentControlValues() {
		List ary = new ArrayList<Item>();
		ary.add(new Item(ControlFragment.COMMAND_TIME, "10"));
		ary.add(new Item(ControlFragment.COMMAND_TIME, "11"));
		ary.add(new Item(ControlFragment.COMMAND_TIME, "13"));

		return ary;
	}
}
