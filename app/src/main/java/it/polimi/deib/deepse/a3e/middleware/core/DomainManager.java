package it.polimi.deib.deepse.a3e.middleware.core;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polimi.deib.deepse.a3e.middleware.domains.AWSDomain;
import it.polimi.deib.deepse.a3e.middleware.utils.A3ELog;
import it.polimi.deib.deepse.a3e.middleware.utils.Commons;
import it.polimi.deib.deepse.a3e.middleware.domains.Domain;
import it.polimi.deib.deepse.a3e.middleware.domains.LocalDomain;

/**
 * Created by giovanniquattrocchi on 30/10/17.
 */

public class DomainManager {

    private List<Domain> domains;
    private Map<A3EFunction, List<Domain>> functions;

    private Handler handler;
    private HandlerThread handlerThread;

    public DomainManager(Context context){
        domains =  Collections.synchronizedList(new ArrayList<Domain>());
        functions =  Collections.synchronizedMap(new HashMap<A3EFunction, List<Domain>>());

        domains.add(new LocalDomain());
        domains.add(new AWSDomain(context));

        handlerThread = new HandlerThread("DomainManager");
        handlerThread.start();

        handler = new Handler(handlerThread.getLooper());

        handler.postDelayed(new Runnable(){
            public void run(){
                awareLoop();
                handler.postDelayed(this, Commons.DOMAIN_AWARENESS_TIME_SAMPLE*1000);
            }
        }, 0);

    }

    public synchronized void registerFunction(A3EFunction function){
        functions.put(function, new ArrayList<Domain>());
    }


    public synchronized List<Domain> getAvailableDomainsForFunction(A3EFunction function){
        return new ArrayList<>(functions.get(function));
    }


    public synchronized void awareLoop() {

        for (A3EFunction function : functions.keySet()){
            List<Domain> functionDomains = functions.get(function);

            for (Domain domain : domains){
                if (functionDomains.contains(domain)){
                    if (!domain.ping())
                        functionDomains.remove(domain);
                }
                else if (domain.ping() && domain.notifyRequirements(function))
                    functionDomains.add(domain);
            }

            A3ELog.append("Available domains: "+functionDomains+" for function "+function.getUniqueName());
        }
    }

}
