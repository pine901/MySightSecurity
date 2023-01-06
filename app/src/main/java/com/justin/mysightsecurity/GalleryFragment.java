package com.justin.mysightsecurity;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.DKGRAY;
import static android.graphics.Color.GRAY;
import static android.graphics.Color.WHITE;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.justin.mysightsecurity.databinding.FragmentGalleryBinding;
import com.justin.mysightsecurity.socket.Global;
import com.justin.mysightsecurity.socket.ServerThread;
import com.justin.mysightsecurity.ui.add_device.AddDeviceFragment;

import java.io.Console;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GalleryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GalleryFragment extends Fragment {
    public interface OnIPCameraListener {
        public void onIPCameraFind(String ip, int port);
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private FragmentGalleryBinding binding;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Bitmap bmp;
    private ImageView imageView;
    private Activity selfActivity=null;
    androidx.appcompat.app.AlertDialog.Builder alert = null;

    private Button btnScan;
    public OnIPCameraListener onIPCameraListner;
    public SQLiteDatabase db;

    public GalleryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GalleryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GalleryFragment newInstance(String param1, String param2) {
        GalleryFragment fragment = new GalleryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public void setIPCameraListner(OnIPCameraListener listener) {
        this.onIPCameraListner = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        AppBarLayout bar =(AppBarLayout) getActivity().findViewById(R.id.appbarLayout);
//        bar.setVisibility(View.VISIBLE);

        this.onIPCameraListner = null;
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        selfActivity = getActivity();
        Global.activity = selfActivity;
//        alert = new androidx.appcompat.app.AlertDialog.Builder(selfActivity);
//        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//
//            }
//        });
//        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//
//            }
//        });
//
//        setIPCameraListner(new OnIPCameraListener() {
//            @Override
//            public void onIPCameraFind(String ip, int port) {
//                selfActivity.runOnUiThread(new Runnable() {
//                    public void run() {
//                        String msg = "Available IP Camera : "+ip+"  : Port"+String.valueOf(port);
//                        if(port == -1) msg = "Available IP Camera not found.";
//                        Toast.makeText(selfActivity, msg, Toast.LENGTH_LONG).show();
//                    }
//                });
//                // Determine whether to available ip address exits
//                if(ip.equals("")){
//                    // Go to Failure activity.
//                    Intent intent = new Intent(getActivity(), FailureActivity.class);
//                    getActivity().startActivity(intent);
//                }
//                else {
//                    // Go to Success activity.
//                    try {
//                        db= getActivity().openOrCreateDatabase("sight.db", Context.MODE_PRIVATE,null);
//                    }catch (Exception e) {
//                        Toast.makeText(getActivity(), "Can not access database: "+ e.toString(), Toast.LENGTH_SHORT).show();
//                        db.close();
//                    }
//                    // Add device to DB
//                    ContentValues val = new ContentValues();
//
//                    val.put("device_name", getArguments().getString("device_name"));
//                    val.put("device_id", getArguments().getString("device_id"));
//                    val.put("ip_address", ip);
//                    val.put("port_number", String.valueOf(port));
//
//                    db.insert("Device", null, val);
//                    db.close();
//                    // Go
//                    Intent intent = new Intent(getActivity(), SuccessActivity.class);
//                    getActivity().startActivity(intent);
//                }
//                // Edited By BINGO
//            }
//        });

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        btnScan = (Button) view.findViewById(R.id.btn_scan);

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Here, Scan IP address to connect
                btnScan.setEnabled(false);
                Global.serverThread = new Thread(new ServerThread());
//
                Global.serverThread.start();
//                Intent intent = new Intent(getContext(), SuccessActivity.class);
//                Global.activity.startActivity(intent)
                // Edited by BINGO
            }
        });
        String str = "{\"name\": \"" + getArguments().getString("device_name")
                + "\", \"id\": \"" + getArguments().getString("device_id")
                + "\", \"ssid\": \""+ getArguments().getString("ssid")
                +"\", \"pass\": \""+ getArguments().getString("password")
                +"\", \"ip\": \""+ getArguments().getString("ip_address")
                +"\"}";
        //Toast.makeText(getActivity(), str,Toast.LENGTH_SHORT).show();
        imageView = (ImageView) view.findViewById(R.id.imgQR);
        String charset = "UTF-8";
        Map<EncodeHintType, ErrorCorrectionLevel> hintMap =new HashMap<EncodeHintType, ErrorCorrectionLevel>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

        try {
            CreateQRCode(str, charset, hintMap,500, 500);
        }catch (Exception e) {
            Toast.makeText(getActivity(), e.toString(),Toast.LENGTH_SHORT).show();
        }
    }


    public  void CreateQRCode(String qrCodeData, String charset, Map hintMap, int qrCodeheight, int qrCodewidth){

        try {
            //generating qr code.
            BitMatrix matrix = new MultiFormatWriter().encode(new String(qrCodeData.getBytes(charset), charset),
                    BarcodeFormat.QR_CODE, qrCodewidth, qrCodeheight, hintMap);
            //converting bitmatrix to bitmap

            int width = matrix.getWidth();
            int height = matrix.getHeight();
            int[] pixels = new int[width * height];
            // All are 0, or black, by default
            for (int y = 0; y < height; y++) {
                int offset = y * width;
                for (int x = 0; x < width; x++) {
                    //for black and white
                    pixels[offset + x] = matrix.get(x, y) ? BLACK : DKGRAY;
                    //for custom color
//                    pixels[offset + x] = matrix.get(x, y) ?
//                            ResourcesCompat.getColor(getResources(),R.color.colorB,null) :WHITE;
                }
            }
            //creating bitmap
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

            //getting the logo
            Bitmap overlay = BitmapFactory.decodeResource(getResources(), R.drawable.logo_1);
            //setting bitmap to image view
            imageView.setImageBitmap(mergeBitmaps(overlay, bitmap));

        }catch (Exception er){
            Log.e("QrGenerate",er.getMessage());
        }
    }
    public Bitmap mergeBitmaps(Bitmap overlay, Bitmap bitmap) {

        int height = bitmap.getHeight();
        int width = bitmap.getWidth();

        Bitmap combined = Bitmap.createBitmap(width, height, bitmap.getConfig());
        Canvas canvas = new Canvas(combined);
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();

        canvas.drawBitmap(bitmap, new Matrix(), null);

        int centreX = (canvasWidth  - overlay.getWidth()) /2;
        int centreY = (canvasHeight - overlay.getHeight()) /2 ;
        canvas.drawBitmap(overlay, centreX, centreY, null);

        return combined;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_gallery, container, false);
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            if(Global.serverSocket != null) Global.serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}