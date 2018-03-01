package com.ashtonandassociates.thermopi.receiver;

import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by tdashton on 28.02.18.
 */
public class BroadcastReceiverService {

    public static String TAG = BroadcastReceiverService.class.getSimpleName();

    protected static BroadcastReceiverService receiver;

    protected List<ConnectionReceiverInterface> receivers;

    public static BroadcastReceiverService getInstance() {
        if (BroadcastReceiverService.receiver == null) {
            BroadcastReceiverService.receiver = new BroadcastReceiverService();
        }
        return BroadcastReceiverService.receiver;
    }

    protected BroadcastReceiverService() {
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
