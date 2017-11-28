package it.polimi.deib.deepse.a3e.middleware.loop;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import it.polimi.deib.deepse.a3e.middleware.core.A3E;
import it.polimi.deib.deepse.a3e.middleware.domains.AWSDomain;
import it.polimi.deib.deepse.a3e.middleware.domains.AWSRestDomain;
import it.polimi.deib.deepse.a3e.middleware.domains.Domain;
import it.polimi.deib.deepse.a3e.middleware.domains.EdgeRestDomain;
import it.polimi.deib.deepse.a3e.middleware.domains.LocalDomain;
import it.polimi.deib.deepse.a3e.middleware.utils.A3ELog;
import it.polimi.deib.deepse.a3e.middleware.utils.Commons;
import it.polimi.deib.deepse.a3e.middleware.utils.UDPListenerService;

/**
 * Created by Giovanni on 05/11/17.
 */

public class DiscoveryManager extends BroadcastReceiver {

    private List<Domain> domains;
    private ExecutorService pool;
    private HashSet<String> discoveredIps;
    private Context context;

    public DiscoveryManager(Context context){
        this.context = context;
        domains =  Collections.synchronizedList(new ArrayList<Domain>());
        pool = Executors.newFixedThreadPool(10);
        discoveredIps = new HashSet<>();

    }

    public synchronized void registerDomain(Domain domain){
        domains.add(domain);
    }

    public synchronized void unRegisterDomain(Domain domain){
        domains.remove(domain);
    }

    public synchronized List<Domain> getAvailableDomain(){

        discoverViaUDP();

        List<Domain> discoveredDomains = pingAll();

        A3ELog.append("*Discovery*", "discovered domains: " + discoveredDomains);

        return discoveredDomains;
    }

    private void discoverViaUDP(){
        String ip = Commons.getCurrentIpAddress();
        String broadcastIp = Commons.getBroadcastIpAddress();
        A3ELog.append("*Discovery*", "broadcast ip: "+broadcastIp+" ip: "+ip);

        if (ip != null && broadcastIp != null && !discoveredIps.contains(broadcastIp)) {

            boolean found = false;
            String[] parts = ip.split("\\.");
            String prefix = parts[0]+"."+parts[1]+"."+parts[2]+".";

            for (Domain d : domains){
                if(d.getIp().startsWith(prefix)){
                    found = true;
                    break;
                }
            }

            if (!found){
                A3ELog.append("*Discovery*", "launching service");
                Intent intent = new Intent(context, UDPListenerService.class);
                intent.putExtra(UDPListenerService.IP_EXTRA,  broadcastIp);
                intent.putExtra(UDPListenerService.PORT_EXTRA,  Commons.DISCOVERY_PORT);
                context.startService(intent);
                context.registerReceiver(this, new IntentFilter(UDPListenerService.UDP_BROADCAST));
                discoveredIps.add(broadcastIp);
            }
        }
    }

    private List<Domain> pingAll(){

        List<Callable<PingResponse>> tasks = new ArrayList<>();
        List<Domain> currentDomains = new ArrayList<>(domains);

        for (final Domain domain : currentDomains) {
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

        return discoveredDomains;
    }

    @Override
    public synchronized void onReceive(Context context, final Intent intent) {
        pool.execute(new Runnable() {
            @Override
            public void run() {
                String ip = intent.getStringExtra(UDPListenerService.IP_EXTRA);
                boolean found = false;
                for (Domain d : domains){
                    if(d.getIp().equals(ip)){
                        found = true;
                        break;
                    }
                }
                if (!found)
                    registerDomain(new EdgeRestDomain(ip, ip));
            }
        });

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
