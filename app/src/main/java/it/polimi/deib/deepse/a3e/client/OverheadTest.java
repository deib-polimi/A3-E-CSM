package it.polimi.deib.deepse.a3e.client;


import android.app.Activity;
import android.os.Handler;
import android.os.HandlerThread;

import java.util.ArrayList;

import it.polimi.deib.deepse.a3e.middleware.core.A3E;
import it.polimi.deib.deepse.a3e.middleware.core.A3EFunction;
import it.polimi.deib.deepse.a3e.middleware.domains.LocalDomain;
import it.polimi.deib.deepse.a3e.middleware.utils.A3ELog;

/**
 * Created by giovanniquattrocchi on 16/11/17.
 */

public class OverheadTest extends A3ETest {


    public <T> OverheadTest(A3E a3e, Activity activity,  A3EFunction<T> function, T payload){
        super(a3e, activity, function, payload);
    }

    @Override
    protected Runnable getRunnable() {
        return new TestRun();
    }

    private class TestRun implements Runnable {

        int times = 0;
        int phase = 0;
        LocalDomain domain = new LocalDomain();

        @Override
        public void run() {
            final OverheadTest.TestRun self = this;

            domain.executeFunction(activity, function, payload, new A3EFunction.Callback(){
                @Override
                public void onFunctionResult(A3EFunction.FunctionResult result) {
                    if (result.isSuccess()){
                        times++;
                        A3ELog.append("*TEST*", "call: "+times+" phase: "+phase);
                        if (times == parameters.CALL_PER_PHASE) {
                            try {
                                Thread.sleep(parameters.SLEEP_BETWEEN_PHASES*1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            times = 0;
                            phase++;
                            if (phase < parameters.PHASES)
                                handler.postDelayed(self, 0);
                            else {
                                A3ELog.append("*TEST*", "end");
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        for(A3ETest.Listener listener : listeners)
                                            listener.onTestEnd();
                                    }
                                }, 2000);
                            }
                        }
                        else
                            handler.postDelayed(self, (long) (parameters.CALL_INTERVAL*1000));
                    }
                    else {
                        handler.postDelayed(self, (long) (parameters.CALL_INTERVAL*1000));
                    }
                }
            });


        }


    }
}

