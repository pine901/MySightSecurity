package com.justin.mysightsecurity.socket;

import android.util.Log;
import android.widget.Toast;

import com.justin.mysightsecurity.Utils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread implements Runnable {

    public void run() {

        Socket socket = null;
        try {
            Global.serverSocket = new ServerSocket(Global.SERVERPORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (!Thread.currentThread().isInterrupted()) {
            try {

                socket = Global.serverSocket.accept();
                Global.activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("Just connected",Global.getLocalIpAddress()+Global.SERVERPORT);
                    }
                });

                CommunicationThread commThread = new CommunicationThread(socket);
                new Thread(commThread).start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}