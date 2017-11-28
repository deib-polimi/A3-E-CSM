package it.polimi.deib.deepse.a3e.middleware.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
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
    public static final int WORLD_SIMULATOR_TIME_SAMPLE = 2;
    public static final int DISCOVERY_PORT = 12340;
    public static final int IDENTIFICATION_PORT = 12341;

    public static synchronized long ping(final String host){

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Long> future = executor.submit(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                Runtime runtime = Runtime.getRuntime();
                Process  mIpAddrProcess = runtime.exec("/system/bin/ping -c 1 -W 3.0 "+host);
                long i = System.currentTimeMillis();
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
            Long out = future.get(3, TimeUnit.SECONDS);
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

    public static String getCurrentIpAddress() {
        try {
            System.setProperty("java.net.preferIPv4Stack", "true");
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':')<0;

                        if (isIPv4)
                            return sAddr;
                    }
                }
            }
        } catch (Exception ex) { } // for now eat exceptions
        return "";
    }

    public static String getBroadcastIpAddress()  {
        try {
        System.setProperty("java.net.preferIPv4Stack", "true");
        for (Enumeration<NetworkInterface> niEnum = NetworkInterface.getNetworkInterfaces(); niEnum.hasMoreElements();) {
            NetworkInterface ni = niEnum.nextElement();
            if (!ni.isLoopback()) {
                for (InterfaceAddress interfaceAddress : ni.getInterfaceAddresses()) {
                    InetAddress res = interfaceAddress.getBroadcast();
                    if(res != null){
                        return res.toString().substring(1);

                    }
                }
            }
        }
        } catch (SocketException ex) {ex.printStackTrace();}
        return null;
    }

}
