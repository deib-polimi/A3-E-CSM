package it.polimi.deib.deepse.a3e.client;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import it.polimi.deib.deepse.a3e.middleware.core.A3E;
import it.polimi.deib.deepse.a3e.middleware.core.A3EFunction;

/**
 * Created by giovanniquattrocchi on 15/11/17.
 */

public class A3ETestService extends Service {

    private A3ETest test;
    PowerManager.WakeLock wakeLock;

    public void startTest(Test test){
        PowerManager mgr = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakeLock");
        wakeLock.acquire();
        test.start();
    }

    private final IBinder mBinder = new LocalBinder();
    public class LocalBinder extends Binder {
        A3ETestService getService() {
            // Return this instance of LocalService so clients can call public methods
            return A3ETestService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        wakeLock.release();
    }
}
