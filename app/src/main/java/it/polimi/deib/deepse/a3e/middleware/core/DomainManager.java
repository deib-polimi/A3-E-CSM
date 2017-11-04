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
import it.polimi.deib.deepse.a3e.middleware.domains.AWSRestDomain;
import it.polimi.deib.deepse.a3e.middleware.utils.A3ELog;
import it.polimi.deib.deepse.a3e.middleware.utils.Commons;
import it.polimi.deib.deepse.a3e.middleware.domains.Domain;
import it.polimi.deib.deepse.a3e.middleware.domains.LocalDomain;

/**
 * Created by giovanniquattrocchi on 30/10/17.
 */

public class DomainManager {

    private List<Domain> domains;
    private Map<A3EFunction, Domain> functions;

    private Handler handler;
    private HandlerThread handlerThread;
    private DomainSelector domainSelector = new DomainSelector();

    public DomainManager(Context context){
        domains =  Collections.synchronizedList(new ArrayList<Domain>());
        functions =  Collections.synchronizedMap(new HashMap<A3EFunction, Domain>());

        domains.add(new LocalDomain());
        // domains.add(new AWSDomain(context));
        domains.add(new AWSRestDomain("https://q8i5t5834m.execute-api.us-west-2.amazonaws.com/"));

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
        functions.put(function, domainSelector.selectDomainForRequirements(function, new ArrayList<Domain>()));
    }


    public synchronized Domain getSelectedDomain(A3EFunction function){
        return functions.get(function);
    }


    public synchronized void awareLoop() {

        for (A3EFunction function : functions.keySet()){
            List<Domain> availableDomains = new ArrayList<>();

            for (Domain domain : domains){
                if (domain.ping()){
                    availableDomains.add(domain);
                    domain.notifyRequirements(function);
                }
            }
            A3ELog.append("*Awareness*", "available domains: "+availableDomains+" for function: "+function.getUniqueName());


            Domain selectedDomain = domainSelector.selectDomainForRequirements(function, availableDomains);
            functions.put(function, selectedDomain);

        }
    }

}
