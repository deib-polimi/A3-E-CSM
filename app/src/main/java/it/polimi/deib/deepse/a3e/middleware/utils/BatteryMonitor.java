package it.polimi.deib.deepse.a3e.middleware.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import it.polimi.deib.deepse.a3e.middleware.core.A3E;

/**
 * Created by giovanniquattrocchi on 03/11/17.
 */

public class BatteryMonitor extends BroadcastReceiver {

    public BatteryMonitor(Context context){
//        context.registerReceiver(this, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        float batteryPct = level / (float)scale;

        A3ELog.append("*Battery Level*", "value: "+batteryPct);

    }
}
