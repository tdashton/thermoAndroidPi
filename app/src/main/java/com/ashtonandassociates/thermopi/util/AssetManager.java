package com.ashtonandassociates.thermopi.util;

import android.content.res.Resources;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AssetManager {

	private static AssetManager instance = null;

	protected Resources resources = null;
	protected android.content.res.AssetManager assetManager = null;

	protected Properties mProperties;

	public static AssetManager getInstance(Resources resources, int rawConfigResource) {
		if(instance != null) {
			return instance;
		}
		instance = new AssetManager(resources, rawConfigResource);
		return instance;
	}

	protected AssetManager(Resources resource, int rawConfigResource) {
		this.resources = resource;
		this.assetManager = resource.getAssets();
		try {
			InputStream inputStream = resources.openRawResource(rawConfigResource);
			mProperties = new Properties();
			mProperties.load(inputStream);
			Log.d(this.getClass().getSimpleName(), "The properties are now loaded");
			Log.d(this.getClass().getSimpleName(), "properties: " + mProperties);
		} catch (IOException e) {
			Log.e(this.getClass().getSimpleName(), "Failed to open property file");
			e.printStackTrace();
		}
	}

	public String getProperty(String propertyName) {
		return mProperties.getProperty(propertyName);
	}
}