package com.puravida.savemy3g;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {
    public BootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Intent startServiceIntent = new Intent(context, CheckWifiService.class);
            startServiceIntent.putExtra("boot", true);
            context.startService(startServiceIntent);
        }
    }
}
