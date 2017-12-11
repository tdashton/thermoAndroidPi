package com.ashtonandassociates.thermopi.messaging;

import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by tashton on 27.07.17.
 */
public class ThermoPiGcmListenerService extends GcmListenerService
{
	private static final String TAG = ThermoPiGcmListenerService.class.getSimpleName();

	@Override
	public void onMessageReceived(java.lang.String s, android.os.Bundle bundle) {
		Log.d(TAG, "onMessageReceived string:" + s);
		Log.d(TAG, "onMessageReceived bundle:" + bundle.toString());
	}
}
