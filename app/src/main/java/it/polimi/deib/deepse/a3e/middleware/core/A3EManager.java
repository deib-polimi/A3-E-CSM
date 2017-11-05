package it.polimi.deib.deepse.a3e.middleware.core;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polimi.deib.deepse.a3e.middleware.loop.DiscoveryManager;
import it.polimi.deib.deepse.a3e.middleware.loop.IdentificationManager;
import it.polimi.deib.deepse.a3e.middleware.loop.DomainSelector;
import it.polimi.deib.deepse.a3e.middleware.utils.Commons;
import it.polimi.deib.deepse.a3e.middleware.domains.Domain;

/**
 * Created by giovanniquattrocchi on 30/10/17.
 */

public class A3EManager {

    private Map<A3EFunction, Domain> functions;

    private Handler handler;
    private HandlerThread handlerThread;

    private DiscoveryManager discoveryManager;
    private IdentificationManager identificationManager;
    private DomainSelector domainSelector;

    public A3EManager(Context context){

        discoveryManager = new DiscoveryManager(context);
        identificationManager = new IdentificationManager();
        domainSelector = new DomainSelector();

        functions =  Collections.synchronizedMap(new HashMap<A3EFunction, Domain>());

        handlerThread = new HandlerThread("A3EManager");
        handlerThread.start();

        handler = new Handler(handlerThread.getLooper());

        handler.postDelayed(new Runnable(){
            public void run(){
                controlLoop();
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


    public synchronized void controlLoop() {

        List<Domain> availableDomains = discoveryManager.getAvailableDomain();

        for (A3EFunction function : functions.keySet()) {

            List<Domain> properDomain = identificationManager.identify(function, availableDomains);
            Domain selectedDomain = domainSelector.selectDomainForRequirements(function, properDomain);
            functions.put(function, selectedDomain);

        }
    }






}
