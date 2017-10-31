package it.polimi.deib.deepse.a3e.middleware.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by giovanniquattrocchi on 31/10/17.
 */

public class A3ELog {

    private static List<String> log = new ArrayList<>();
    private static List<Listener> listeners = new ArrayList<>();
    public static synchronized void append(String message){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        System.out.println();
        message = sdf.format(date)+" "+message;
        log.add(message);
        for (Listener listener : listeners)
            listener.onLogUpdate(message);
    }

    public static synchronized void addListener(Listener listener){
        listeners.add(listener);
    }



    public interface Listener {
        public void onLogUpdate(String message);
    }

}
