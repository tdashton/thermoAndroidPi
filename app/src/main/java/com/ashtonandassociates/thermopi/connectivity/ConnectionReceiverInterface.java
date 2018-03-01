package com.ashtonandassociates.thermopi.connectivity;

import android.content.Context;
import android.content.Intent;

/**
 * Created by tdashton on 28.02.18.
 */

public interface ConnectionReceiverInterface {
    void notificationReceived(Context context, Intent intent);
}
