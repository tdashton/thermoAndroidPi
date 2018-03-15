package com.ashtonandassociates.thermopi.connectivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by tdashton on 28.02.18.
 */

public class ConnectionReceiver extends BroadcastReceiver {

    public static Integer CONNECTION_ACTIVITY_IS_CONNECTED = 1;

    public static String TAG = ConnectionReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() == ConnectivityManager.CONNECTIVITY_ACTION) {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo =
                    connectivityManager.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                BroadcastReceiverManager.getInstance().notifyReceivers(context, intent, this.CONNECTION_ACTIVITY_IS_CONNECTED);
            }
        }
    }
}
