package com.justin.mysightsecurity.socket;

import static androidx.core.content.PackageManagerCompat.LOG_TAG;

import android.app.Activity;
import android.service.voice.VoiceInteractionSession;
import android.util.Log;
import android.widget.TextView;
import android.os.Handler;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

public class Global {
    static public ServerSocket serverSocket;

    static public Handler updateConversationHandler;

    static public Thread serverThread = null;

    static public TextView text;

    static public int SERVERPORT = 6000;
    static public Activity activity = null;
    static public String newIP = "";
    static public String newPort = "";
    static public String newDeviceId = "";
    static public String requestedDevicedId = "";
    static public Boolean isConnected = false;

    static public String getLocalIpAddress() {
        try {
            for (Enumeration < NetworkInterface > en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration < InetAddress > enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        if(inetAddress.getHostAddress().toString().indexOf("wlan") > 0) continue;
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("LOG_TAG", ex.toString());
        }
        return null;
    }
}
