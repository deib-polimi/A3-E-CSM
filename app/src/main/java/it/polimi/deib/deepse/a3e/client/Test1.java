package it.polimi.deib.deepse.a3e.client;

import android.app.Activity;
import android.os.Handler;
import android.os.HandlerThread;


import it.polimi.deib.deepse.a3e.middleware.core.A3E;
import it.polimi.deib.deepse.a3e.middleware.core.A3EFunction;
import it.polimi.deib.deepse.a3e.middleware.utils.A3ELog;
import it.polimi.deib.deepse.a3e.middleware.utils.Commons;

/**
 * Created by giovanniquattrocchi on 08/11/17.
 */

public class Test1 {

    private A3E a3e;
    private Activity activity;
    private A3EFunction function;
    private Object payload;

    private HandlerThread handlerThread;
    private Handler handler;

    private TestParameters parameters;

    public <T> Test1(A3E a3e, Activity activity, A3EFunction<T> function, T payload, TestParameters param){
        this.function = function;
        this.payload = payload;
        this.a3e = a3e;
        this.activity = activity;
        handlerThread = new HandlerThread("Test1");
        this.parameters = param;
    }

    public void start(){
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
        handler.postDelayed(new TestRun(), 5000);
    }


    private class TestRun implements Runnable {

        int times = 0;
        int phase = 0;

        @Override
        public void run() {
            final TestRun self = this;
            a3e.executeFunction(activity, function, payload, new A3EFunction.Callback() {
                @Override
                public void onFunctionResult(A3EFunction.FunctionResult result) {
                    if (result.isSuccess()){
                        times++;

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
                                A3ELog.append("Test1", "END");
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        activity.finish();
                                    }
                                }, 5000);
                            }
                        }
                        else
                            handler.postDelayed(self, parameters.CALL_INTERVAL*1000);
                    }
                    else {
                        handler.postDelayed(self, parameters.CALL_INTERVAL*1000);
                    }
                }
            });

        }
    }


    public static class TestParameters {
        public final int CALL_INTERVAL;
        public final int CALL_PER_PHASE;
        public final int PHASES;
        public final int SLEEP_BETWEEN_PHASES;

        public TestParameters(int callInterval, int callPerPhase, int phases, int sleepBetweenPhases){
            this.CALL_INTERVAL = callInterval;
            this.CALL_PER_PHASE = callPerPhase;
            this.PHASES = phases;
            this.SLEEP_BETWEEN_PHASES = sleepBetweenPhases;
        }

    }



}
