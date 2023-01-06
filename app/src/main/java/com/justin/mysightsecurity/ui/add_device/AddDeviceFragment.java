package com.justin.mysightsecurity.ui.add_device;

import static android.content.Context.WIFI_SERVICE;
import android.app.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiEnterpriseConfig;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.appbar.AppBarLayout;
import com.justin.mysightsecurity.GalleryFragment;
import com.justin.mysightsecurity.PinSetupActivity;
import com.justin.mysightsecurity.R;
import com.justin.mysightsecurity.SplashActivity;
import com.justin.mysightsecurity.Utils;
import com.justin.mysightsecurity.databinding.FragmentAdddeviceBinding;
import com.justin.mysightsecurity.socket.Global;

public class AddDeviceFragment extends Fragment {

    private FragmentAdddeviceBinding binding;
    private TextView textDeviceName;
    private TextView textDeviceId;
    private TextView textSSID;
    private TextView textPassword;
    private Button btnAddDevice;
    private String ssid;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AddDeviceViewModel notificationsViewModel =
                new ViewModelProvider(this).get(AddDeviceViewModel.class);

        binding = FragmentAdddeviceBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        textDeviceName = (TextView) root.findViewById(R.id.text_device_name);
        textDeviceId = (TextView) root.findViewById(R.id.text_device_id);
        textSSID = (TextView) root.findViewById(R.id.text_ssid);
        textPassword = (TextView) root.findViewById(R.id.text_password);

        btnAddDevice = (Button) root.findViewById(R.id.btn_add_device);

        // Here I will get WI-FI SSID
        WifiManager wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();

//        if (info.getSupplicantState() == SupplicantState.COMPLETED) {
//            ssid = info.getSSID();
//        }
        ssid  = info.getSSID();
        ssid = ssid.substring(1, ssid.length()-1);
        textSSID.setText(ssid);
        // END -Edited by BINGO
//        Utils.getMACAddress("wlan0");
//        Utils.getMACAddress("eth0");
//        Utils.getIPAddress(true); // IPv4
//        Utils.getIPAddress(false); // IPv6
        textDeviceName.setText(Utils.getIPAddress(true));

        btnAddDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(textDeviceName.getText().length() == 0 || textDeviceId.getText().length() == 0 || textSSID.getText().length() == 0 || textPassword.getText().length() == 0) {
                    Toast.makeText(getActivity(), "All fields are required!", Toast.LENGTH_SHORT).show();
                    return;
                }
                ConfirmDialog confirm = new ConfirmDialog((Activity) getContext(), new ConfirmDialog.Callback() {
                    @Override
                    public void onDone() {
                        Global.requestedDevicedId = textDeviceId.getText().toString();
                        Bundle bundle = new Bundle();
                        bundle.putString("device_name", textDeviceName.getText().toString());
                        bundle.putString("device_id", textDeviceId.getText().toString());
                        bundle.putString("ssid", textSSID.getText().toString());
                        bundle.putString("password", textPassword.getText().toString());
                        bundle.putString("ip_address", Utils.getIPAddress(true));

                        NavHostFragment.findNavController(AddDeviceFragment.this)
                                .navigate(R.id.action_add_device_to_galleryFragment, bundle);
                        // checking end
                    }
                });
                confirm.textContent = ("Are these informations correct?"+
                        "\n"+"device_name: "+textDeviceName.getText().toString()+
                        "\n"+ "device_id: "+ textDeviceId.getText().toString()+
                        "\n"+ "ssid: "+ textSSID.getText().toString()+
                        "\n"+ "password: "+textPassword.getText().toString()+
                        "\n"+"ip_address: "+ Utils.getIPAddress(true));
                confirm.show();
//                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
//                alert.setTitle("Are these informations correct?"+
//                        "\n"+"device_name:"+textDeviceName.getText().toString()+
//                        "\n"+ "device_id"+ textDeviceId.getText().toString()+
//                        "\n"+ "ssid"+ textSSID.getText().toString()+
//                        "\n"+  "password"+textPassword.getText().toString()+
//                        "\n"+"ip_address"+ Utils.getIPAddress(true) );
//
//                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        //Your action here
//                        Bundle bundle = new Bundle();
//                        bundle.putString("device_name", textDeviceName.getText().toString());
//                        bundle.putString("device_id", textDeviceId.getText().toString());
//                        bundle.putString("ssid", textSSID.getText().toString());
//                        bundle.putString("password", textPassword.getText().toString());
//                        bundle.putString("ip_address", Utils.getIPAddress(true));
//
//                        NavHostFragment.findNavController(AddDeviceFragment.this)
//                                .navigate(R.id.action_add_device_to_galleryFragment, bundle);
//                        // checking end
//                    }
//                });
//
//                alert.setNegativeButton("NO",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int whichButton) {
//                                textDeviceName.setText("");
//                                textDeviceId.setText("");
//                                //textSSID.setText("");
//                                textSSID.setText(ssid);
//                                textPassword.setText("");
//                            }
//                        });
//
//                alert.show();
            }
        });

        return root;
    }

//    public boolean connectWifi(String ssid, String password) {
//        WifiConfiguration wifiConfig = new WifiConfiguration();
//        wifiConfig.SSID = String.format("\"%s\"", ssid);
//        wifiConfig.preSharedKey = String.format("\"%s\"", password);
//        WifiManager wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(WIFI_SERVICE);
//
//        int netId =wifiManager.addNetwork(wifiConfig);
//        wifiManager.disconnect();
//        wifiManager.enableNetwork(netId, true);
//
//        boolean isConnectionSuccessful = wifiManager.reconnect();
//
//        return isConnectionSuccessful;
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}