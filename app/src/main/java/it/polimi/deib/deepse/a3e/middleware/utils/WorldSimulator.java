package it.polimi.deib.deepse.a3e.middleware.utils;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;

import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import it.polimi.deib.deepse.a3e.middleware.core.A3EFunction;
import it.polimi.deib.deepse.a3e.middleware.domains.Domain;
import it.polimi.deib.deepse.a3e.middleware.loop.DiscoveryManager;
import it.polimi.deib.deepse.a3e.middleware.loop.DomainSelector;
import it.polimi.deib.deepse.a3e.middleware.loop.IdentificationManager;

/**
 * Created by giovanniquattrocchi on 17/11/17.
 */

public class WorldSimulator {


    private static WorldSimulator sim;

    private final int internetOnToOffProbability = 22;
    private final int internetOffToOnProbability = 166;

    private final int edgeOnToOffProbability = 33;
    private final int edgeOffToOnProbability = 66;

    private final int base = 10000;

    private Random rand = new Random();

    private boolean isInternetAvailable = true;

    private boolean isEdgeAvailable = true;


    private Handler handler;
    private HandlerThread handlerThread;

    public static synchronized WorldSimulator getInstance(){

        if (sim == null) {
            sim = new WorldSimulator();
        }

        return sim;
    }

    public WorldSimulator(){
        handlerThread = new HandlerThread("WorldSim", Process.THREAD_PRIORITY_BACKGROUND);
        handlerThread.start();

        handler = new Handler(handlerThread.getLooper());

        handler.postDelayed(new Runnable(){
            public void run(){
                tick();
                handler.postDelayed(this, Commons.WORLD_SIMULATOR_TIME_SAMPLE*1000);
            }
        }, Commons.WORLD_SIMULATOR_TIME_SAMPLE*1000);
    }

    public synchronized boolean isInternetAvailable() {
        return isInternetAvailable;
    }

    public synchronized boolean isEdgeAvailable() {
        return isInternetAvailable && isEdgeAvailable;
    }


    private synchronized void tick(){

        float internetThreshold = isInternetAvailable ?
                internetOnToOffProbability : internetOffToOnProbability;

        int r = rand.nextInt(base)+1;

        if (r <= internetThreshold){
            isInternetAvailable = !isInternetAvailable;
        }


        int edgeThreshold = isInternetAvailable ?
                edgeOnToOffProbability : edgeOffToOnProbability;

        r = rand.nextInt(base)+1;

        if (r <= edgeThreshold){
            isEdgeAvailable = !isEdgeAvailable;
        }

    }




}
