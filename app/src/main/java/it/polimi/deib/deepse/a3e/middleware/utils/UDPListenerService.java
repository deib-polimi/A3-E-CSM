package it.polimi.deib.deepse.a3e.middleware.utils;

/**
 * Created by giovanniquattrocchi on 22/11/17.
 */


import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;


/*
 * Linux command to send UDP:
 * #socat - UDP-DATAGRAM:192.168.1.255:11111,broadcast,sp=11111
 */
public class UDPListenerService extends Service {
    public static final String UDP_BROADCAST = "UDPBroadcast";
    public static final String IP_EXTRA = "__ip__";
    public static final String PORT_EXTRA = "__port__";
    public static final String MESSAGE_EXTRA = "__message__";

    private String ip;

    //Boolean shouldListenForUDPBroadcast = false;
    DatagramSocket socket;

    private void listenAndWaitAndThrowIntent(InetAddress broadcastIP, Integer port) throws Exception {
        byte[] recvBuf = new byte[15000];
        if (socket == null || socket.isClosed()) {
            socket = new DatagramSocket(port, broadcastIP);
            socket.setBroadcast(true);
        }
        //socket.setSoTimeout(1000);
        DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
        Log.e("UDP", "Waiting for UDP broadcast");
        socket.receive(packet);

        String senderIP = packet.getAddress().getHostAddress();
        String message = new String(packet.getData()).trim();

        A3ELog.append("*UDPListenerService*", "broadcast message received " + senderIP + ", message: " + message+" "+System.currentTimeMillis());

        broadcastIntent(senderIP, message);
        socket.close();
    }

    private void broadcastIntent(String senderIP, String message) {
        Intent intent = new Intent(UDPListenerService.UDP_BROADCAST);
        intent.putExtra(IP_EXTRA, senderIP);
        intent.putExtra(MESSAGE_EXTRA, message);
        sendBroadcast(intent);
    }

    Thread UDPBroadcastThread;

    void startListenForUDPBroadcast(final String ip, final Integer port) {
        UDPBroadcastThread = new Thread(new Runnable() {
            public void run() {
                try {
                    InetAddress broadcastIP = InetAddress.getByName(ip); //172.16.238.42 //192.168.1.255
                    Integer broadcastPort = port;
                    while (shouldRestartSocketListen) {
                        listenAndWaitAndThrowIntent(broadcastIP, broadcastPort);
                    }
                    //if (!shouldListenForUDPBroadcast) throw new ThreadDeath();
                } catch (Exception e) {
                    Log.i("UDP", "no longer listening for UDP broadcasts cause of error " + e.getMessage());
                }
            }
        });
        UDPBroadcastThread.start();
    }

    private Boolean shouldRestartSocketListen=true;

    void stopListen() {
        shouldRestartSocketListen = false;
        socket.close();
    }

    @Override
    public void onCreate() {

    };

    @Override
    public void onDestroy() {
        stopListen();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        shouldRestartSocketListen = true;
        this.ip = intent.getStringExtra(IP_EXTRA);
        startListenForUDPBroadcast(ip, intent.getIntExtra(PORT_EXTRA, -1));
        Log.i("UDP", "Service started");
        return START_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}