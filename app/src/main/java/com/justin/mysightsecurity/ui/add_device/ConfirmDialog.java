package com.justin.mysightsecurity.ui.add_device;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.navigation.fragment.NavHostFragment;

import com.justin.mysightsecurity.R;

public class ConfirmDialog extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button yes, no;
    public String textContent = "";
    public String textDeviceName = "";
    public String textDeviceId = "";
    public String textSSID = "";
    public String textPassword = "";
    public String textIpAddress = "";
    private TextView textView;

    public ConfirmDialog(Activity a,Callback ss) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.ss = ss;
    }

    public interface Callback {
        void onDone();
    }

    Callback ss;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.confirmdialog);
        textView = (TextView) this.findViewById(R.id.txt_dia);
        textView.setText(textContent);
        yes = (Button) findViewById(R.id.btn_yes);
        no = (Button) findViewById(R.id.btn_no);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:
                ss.onDone();
                break;
            case R.id.btn_no:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }


}