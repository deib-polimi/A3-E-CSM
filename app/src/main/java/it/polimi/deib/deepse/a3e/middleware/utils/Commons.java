package it.polimi.deib.deepse.a3e.middleware.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by giovanniquattrocchi on 31/10/17.
 */

public class Commons {

    public static final int NUM_THREAD_PER_DOMAIN = 10;
    public static final int DOMAIN_AWARENESS_TIME_SAMPLE = 2;

    public static long ping(String host){
        System.out.println(" mExitValue?");
        Runtime runtime = Runtime.getRuntime();
        try
        {
            long i = System.currentTimeMillis();
            Process  mIpAddrProcess = runtime.exec("/system/bin/ping -c 1 -W 0.5 "+host);
            int mExitValue = mIpAddrProcess.waitFor();
            long f=System.currentTimeMillis();
            System.out.println(" mExitValue "+mExitValue+" ms: "+(f-i));
            if(mExitValue==0){

                System.out.println();
                return f-i;
            }else{
                return -1;
            }
        }
        catch (InterruptedException ignore)
        {
            ignore.printStackTrace();
            System.out.println(" Exception:"+ignore);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.out.println(" Exception:"+e);
        }
        return -1;
    }
}
