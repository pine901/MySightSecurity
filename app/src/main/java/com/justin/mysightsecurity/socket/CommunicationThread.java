package com.justin.mysightsecurity.socket;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.justin.mysightsecurity.CameraActivity;
import com.justin.mysightsecurity.R;
import com.justin.mysightsecurity.SuccessActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

class CommunicationThread implements Runnable {

    private Socket clientSocket;

    private BufferedReader input;

    public CommunicationThread(Socket clientSocket) {

        this.clientSocket = clientSocket;

        try {

            this.input = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {

        while (!Thread.currentThread().isInterrupted()) {


            try {
                String read = input.readLine();//{ip:"192.168.2.30", port:"554"}


                String jsonStr = read;//"{\"ip\":\"192.168.2.30\", \"port\":\"554\"}";
                String ip="", port="",id="";
                JSONObject obj = null;
                try {
                    obj = new JSONObject(jsonStr);
                    ip = obj.getString("ip");
                    port = obj.getString("port");
                    id = obj.getString("id");
                    String finalIp = ip;
                    String finalPort = port;
                    String finalId = id;
                    Global.activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("read_","communication"+ finalIp + finalPort);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Add IP Camera Address
                Global.newIP = ip;
                Global.newPort = port;
                Global.newDeviceId = id;
                if(Global.newDeviceId == Global.requestedDevicedId)
                {
                    PrintWriter output = new PrintWriter(clientSocket.getOutputStream());
                    output.println("{'host_ip' :'192.168.1.76', 'command':'stream', 'value':1}");
                    output.flush();

                }
                else{
                    PrintWriter output = new PrintWriter(clientSocket.getOutputStream());
                    output.println("{'host_ip' :'192.168.1.76', 'command':'stream', 'value':0}");
                    output.flush();
                    output.close();
                    return;
                }
                Global.activity.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(Global.activity, "New IP Camera:"+Global.newIP+" Port:"+Global.newPort, Toast.LENGTH_LONG).show();

                        Global.isConnected = true;
                        Button btn = Global.activity.findViewById(R.id.btn_scan);
                        //btn.setEnabled(true);
                        //ui, db update
                        SQLiteDatabase db;
                        try {
                            db= Global.activity.openOrCreateDatabase("sight.db", Context.MODE_PRIVATE,null);
                            ContentValues val = new ContentValues();

//                        val.put("device_name", getArguments().getString("device_name"));
//                        val.put("device_id", getArguments().getString("device_id"));
                            val.put("ip_address", Global.newIP);
                            val.put("port", String.valueOf(Global.newPort));
                            val.put("device_id", String.valueOf(Global.newDeviceId));
                            val.put("ico_url", "horse");

                            db.insert("Device", null, val);
                            db.close();


                            Intent intent = new Intent(Global.activity, SuccessActivity.class);
                            Global.activity.startActivity(intent);

                            Intent myIntent = new Intent(Global.activity, CameraActivity.class);
                            myIntent.putExtra("playinfo", String.format("%d", -1));
                            Global.activity.startActivity(myIntent);

                        }catch (Exception e) {
                            Toast.makeText(Global.activity, "Can not access database: "+ e.toString(), Toast.LENGTH_SHORT).show();
                        }
                        // Add device to DB
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
