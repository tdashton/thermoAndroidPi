package com.ashtonandassociates.thermopi.connectivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by tdashton on 28.02.18.
 */

public class ConnectionReceiver extends BroadcastReceiver {

    public static String TAG = ConnectionReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        BroadcastReceiverManager.getInstance().notifyReceivers(context, intent);
    }
}
