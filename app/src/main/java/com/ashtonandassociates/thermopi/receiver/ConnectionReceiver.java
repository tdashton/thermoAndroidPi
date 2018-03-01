package com.ashtonandassociates.thermopi.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by tdashton on 28.02.18.
 */

public class ConnectionReceiver extends BroadcastReceiver {

    public static String TAG = ConnectionReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        BroadcastReceiverService.getInstance().notifyReceivers(context, intent);
    }
}
