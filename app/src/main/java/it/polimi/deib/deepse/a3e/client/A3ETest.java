package it.polimi.deib.deepse.a3e.client;

import android.app.Activity;
import android.os.Handler;
import android.os.HandlerThread;


import java.util.ArrayList;
import java.util.List;

import it.polimi.deib.deepse.a3e.middleware.core.A3E;
import it.polimi.deib.deepse.a3e.middleware.core.A3EFunction;
import it.polimi.deib.deepse.a3e.middleware.utils.A3ELog;

/**
 * Created by giovanniquattrocchi on 08/11/17.
 */

public class A3ETest implements Test {

    protected A3E a3e;
    protected Activity activity;
    protected A3EFunction function;
    protected Object payload;

    protected HandlerThread handlerThread;
    protected Handler handler;

    protected Parameters parameters;

    protected List<Listener> listeners;


    public <T> A3ETest(A3E a3e, Activity activity, A3EFunction<T> function, T payload){
        this.function = function;
        this.payload = payload;
        this.a3e = a3e;
        this.activity = activity;
        handlerThread = new HandlerThread("A3ETest");
        listeners = new ArrayList<>();
    }

    public void addListener(Listener listener){
        listeners.add(listener);
    }

    public void setParameters(Parameters parameters){
        this.parameters = parameters;
    }

    public String toString(){
        return "test with parameters: ("+parameters.toString()+")";
    }

    protected Runnable getRunnable(){
        return new TestRun();
    }

    public void start(){
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
        handler.postDelayed(getRunnable(), 5000);
        A3ELog.append("*TEST*", "start "+this);
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
                                       for(Listener listener : listeners)
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

    public static class Parameters {
        public final double CALL_INTERVAL;
        public final int CALL_PER_PHASE;
        public final int PHASES;
        public final int SLEEP_BETWEEN_PHASES;

        public Parameters(double callInterval, int callPerPhase, int phases, int sleepBetweenPhases){
            this.CALL_INTERVAL = callInterval;
            this.CALL_PER_PHASE = callPerPhase;
            this.PHASES = phases;
            this.SLEEP_BETWEEN_PHASES = sleepBetweenPhases;
        }

        public String toString(){
            return "tCall: "+CALL_INTERVAL+" nCalls: "+CALL_PER_PHASE+" nPhases: "+PHASES+" tPhase: "+SLEEP_BETWEEN_PHASES;
        }
    }

    public interface Listener {
        void onTestEnd();
    }



}
