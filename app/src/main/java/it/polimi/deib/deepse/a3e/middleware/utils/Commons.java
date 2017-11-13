package it.polimi.deib.deepse.a3e.middleware.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by giovanniquattrocchi on 31/10/17.
 */

public class Commons {

    public static final int NUM_THREAD_PER_DOMAIN = 10;
    public static final int DOMAIN_AWARENESS_TIME_SAMPLE = 2;

    public static synchronized long ping(final String host){

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Long> future = executor.submit(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                Runtime runtime = Runtime.getRuntime();
                long i = System.currentTimeMillis();
                Process  mIpAddrProcess = runtime.exec("/system/bin/ping -c 1 -W 1.0 "+host);
                mIpAddrProcess.waitFor();
                long f=System.currentTimeMillis();
                if(mIpAddrProcess.exitValue() == 0){
                    return f-i;
                }else{
                    return Long.valueOf(-1);
                }
            }
        });

        try {
            Long out = future.get(2, TimeUnit.SECONDS);
            System.out.println("host: "+host+" ping: "+out);
            return out;
        } catch (Exception e) {
            e.printStackTrace();
            future.cancel(true);
        } finally {
            executor.shutdownNow();
        }

        return -1;
    }
}
