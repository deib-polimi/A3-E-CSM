package it.polimi.deib.deepse.a3e.middleware.loop;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import it.polimi.deib.deepse.a3e.middleware.domains.AWSDomain;
import it.polimi.deib.deepse.a3e.middleware.domains.AWSRestDomain;
import it.polimi.deib.deepse.a3e.middleware.domains.Domain;
import it.polimi.deib.deepse.a3e.middleware.domains.EdgeRestDomain;
import it.polimi.deib.deepse.a3e.middleware.domains.LocalDomain;
import it.polimi.deib.deepse.a3e.middleware.utils.A3ELog;

/**
 * Created by Giovanni on 05/11/17.
 */

public class DiscoveryManager {

    private List<Domain> domains;
    private ExecutorService pool;

    public DiscoveryManager(Context context){

        domains =  Collections.synchronizedList(new ArrayList<Domain>());
        //domains.add(new LocalDomain());
        domains.add(new AWSRestDomain("https://q8i5t5834m.execute-api.us-west-2.amazonaws.com/"));
        //domains.add(new AWSDomain(context));
        domains.add(new EdgeRestDomain("131.175.135.184", "http://131.175.135.184:3002/api/v1/namespaces/guest/actions/"));
        pool = Executors.newFixedThreadPool(domains.size());

    }

    public List<Domain> getAvailableDomain(){

        List<Callable<PingResponse>> tasks = new ArrayList<>();

        for (final Domain domain : domains) {
            tasks.add(new Callable<PingResponse>() {
                @Override
                public PingResponse call() {
                    PingResponse pingResponse = new PingResponse();
                    pingResponse.domain = domain;
                    pingResponse.success = domain.ping();
                    return pingResponse;
                }
            });
        }

        List<Future<PingResponse>> results = null;

        try {
            results = pool.invokeAll(tasks);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<Domain> discoveredDomains = new ArrayList<>();

        for (Future<PingResponse> result : results) {
            try {
                if (result.get().success) {
                    discoveredDomains.add(result.get().domain);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        A3ELog.append("*Discovery*", "discovered domains: " + discoveredDomains);

        return discoveredDomains;

    }

    private class PingResponse {
        private Domain domain;
        private boolean success;

        private PingResponse(){
            domain = null;
            success = false;
        }
    }


}
