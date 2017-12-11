package com.ashtonandassociates.thermopi.messaging;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.ashtonandassociates.thermopi.R;
import com.ashtonandassociates.thermopi.util.AssetManager;
import com.ashtonandassociates.thermopi.util.Constants;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

public class RegistrationIntentService extends IntentService
{
	private static final String TAG = RegistrationIntentService.class.getSimpleName();

	public RegistrationIntentService() {
		super(RegistrationIntentService.TAG);
	}

	/**
	 * Creates an IntentService.  Invoked by your subclass's constructor.
	 *
	 * @param name Used to name the worker thread, important only for debugging.
	 */
	public RegistrationIntentService(String name) {
		super(name);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		String appId = AssetManager.getInstance(getResources(), R.raw.config).getProperty(Constants.CONST_CONFIG_GOOGLE_SERVICES_APP_ID);
		InstanceID instanceID = InstanceID.getInstance(this);
		String token = null;
		SharedPreferences sharedPrefs = null;

		try {
			token = instanceID.getToken(appId, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
			sharedPrefs = getSharedPreferences(Constants.CONST_SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
			sharedPrefs.edit().putString(Constants.CONST_GCM_TOKEN, token).commit();
			Log.w(TAG, "Token: " + token);
		} catch (IOException ioe) {
			Log.w(TAG, "could not get GCM token");
		}
	}
}
