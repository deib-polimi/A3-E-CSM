package it.polimi.deib.deepse.a3e.middleware.core;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import it.polimi.deib.deepse.a3e.middleware.domains.Domain;
import it.polimi.deib.deepse.a3e.middleware.utils.BatteryMonitor;

/**
 * Created by giovanniquattrocchi on 30/10/17.
 */

public class A3EFacade implements A3E {

    private A3EManager manager;
    private BroadcastReceiver batteryReceiver;
    private Context context;

    public A3EFacade(Context context){
        this.context = context;
        manager = new A3EManager(context);
        batteryReceiver = new BatteryMonitor();
        context.registerReceiver(batteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

    }

    @Override
    public void registerFunction(A3EFunction function) {
        manager.registerFunction(function);
    }

    @Override
    public <T> void executeFunction(Activity activity, A3EFunction<T> function, T payload, A3EFunction.Callback callback) {
        function.setCurrentContext(activity);
        Domain domain = manager.getSelectedDomain(function);
        domain.executeFunction(activity, function, payload, callback);
    }

    @Override
    public void quit() {
        context.unregisterReceiver(batteryReceiver);
        manager = null;
    }


}
