package com.ashtonandassociates.thermopi.connectivity;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tdashton on 28.02.18.
 */
public class BroadcastReceiverManager {

    public static String TAG = BroadcastReceiverManager.class.getSimpleName();

    protected static BroadcastReceiverManager receiver;

    protected List<ConnectionReceiverInterface> receivers;

    public static BroadcastReceiverManager getInstance() {
        if (BroadcastReceiverManager.receiver == null) {
            BroadcastReceiverManager.receiver = new BroadcastReceiverManager();
        }
        return BroadcastReceiverManager.receiver;
    }

    protected BroadcastReceiverManager() {
        this.receivers = new ArrayList<>();
    }

    public void addReceiver(ConnectionReceiverInterface receiver) {
        if (!this.receivers.contains(receiver)) {
            this.receivers.add(receiver);
        }
    }

    public void notifyReceivers(Context context, Intent intent) {
        for (ConnectionReceiverInterface receiver : this.receivers) {
            receiver.notificationReceived(context, intent);
        }
    }
}
