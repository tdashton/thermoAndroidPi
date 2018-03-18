package com.ashtonandassociates.thermopi.connectivity;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by tdashton on 28.02.18.
 */
public class BroadcastReceiverManager {

    public static String TAG = BroadcastReceiverManager.class.getSimpleName();

    protected static BroadcastReceiverManager receiver;

    protected List<ConnectionReceiverInterface> receivers;
    protected HashMap<ConnectionReceiverInterface, Integer> receiversActionMasks;

    public static BroadcastReceiverManager getInstance() {
        if (BroadcastReceiverManager.receiver == null) {
            BroadcastReceiverManager.receiver = new BroadcastReceiverManager();
        }
        return BroadcastReceiverManager.receiver;
    }

    protected BroadcastReceiverManager() {
        this.receivers = new ArrayList<>();
        this.receiversActionMasks = new HashMap<>();
    }

    public void addReceiver(ConnectionReceiverInterface receiver, Integer actionMask) {
        if (!this.receivers.contains(receiver)) {
            this.receivers.add(receiver);
            this.receiversActionMasks.put(receiver, actionMask);
        }
    }

    public void removeReceiver(ConnectionReceiverInterface receiver) {
        if (this.receivers.contains(receiver)) {
            this.receivers.remove(receiver);
            this.receiversActionMasks.remove(receiver);
        }
    }

    public void notifyReceivers(Context context, Intent intent, Integer action) {
        for (ConnectionReceiverInterface receiver : this.receivers) {
            int mask = this.receiversActionMasks.get(receiver).intValue();
            if ((mask & action) == action) {
                receiver.notificationReceived(context, intent);
            }
        }
    }
}
