package com.justin.mysightsecurity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import com.justin.mysightsecurity.socket.Global;
import com.justin.mysightsecurity.socket.ServerThread;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class SocketServerActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket_server);

        String jsonStr = "{\"ip\":\"192.168.1.71\", \"port\":\"8080\"}";
        String ip="", port="";
        JSONObject obj = null;
        try {
            obj = new JSONObject(jsonStr);
            ip = obj.getString("ip");
            port = obj.getString("port");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Global.activity = this;
        Global.newIP = ip;
        Global.newPort = port;

        Global.activity.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(Global.activity, "New IP Camera:"+Global.newIP+" Port:"+Global.newPort, Toast.LENGTH_LONG).show();
            }
        });

        Global.text = (TextView) findViewById(R.id.text_socket);
        Global.text.setText("Starting...at" + port +" -- " + ip );

        Global.updateConversationHandler = new Handler();
        Global.serverThread = new Thread(new ServerThread());
        Global.serverThread.start();

    }
    @Override
    protected void onStop() {
        super.onStop();
        try {
            Global.serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
