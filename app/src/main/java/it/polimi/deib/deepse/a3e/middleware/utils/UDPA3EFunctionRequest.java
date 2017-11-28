package it.polimi.deib.deepse.a3e.middleware.utils;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telecom.Call;
import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import it.polimi.deib.deepse.a3e.middleware.core.A3EFunction;

/**
 * Created by giovanniquattrocchi on 23/11/17.
 */

public class UDPA3EFunctionRequest implements Runnable {

    private A3EFunction function;
    private DatagramSocket socket;
    private Callback callback;
    private InetAddress ip;

    public void run() {
        try {

            socket = new DatagramSocket(Commons.IDENTIFICATION_PORT);

            String data = function.getRepo() + ";" + function.getUniqueName().split("\\?")[0];
            byte[] sendBuf = data.getBytes();
            DatagramPacket firstPacket = new DatagramPacket(sendBuf, sendBuf.length, ip, Commons.IDENTIFICATION_PORT);
            socket.send(firstPacket);

            byte[] recvBuf = new byte[15000];
            DatagramPacket secondPacket = new DatagramPacket(recvBuf, recvBuf.length);
            socket.receive(secondPacket);

            if (secondPacket.getAddress().getHostAddress().equals(ip.getHostAddress())) {
                String message = new String(secondPacket.getData()).trim();
                String[] parts = message.split(";");
                if (parts[0].toLowerCase().equals("ok")) {
                    A3ELog.append("*UDPA3EFunctionRequest*", "function ok message received "+System.currentTimeMillis());
                    callback.onFunctionAvailable(parts[1]);
                }

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            socket.close();
        }

    }


    public UDPA3EFunctionRequest(A3EFunction function, String ip, Callback callback) {
        try {

            this.ip = InetAddress.getByName(ip);
            this.function = function;
            this.callback = callback;

            new Thread(this).start();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }



    public interface Callback {
        void onFunctionAvailable(String functionName);
    }

}
