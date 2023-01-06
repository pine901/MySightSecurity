package com.justin.mysightsecurity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.net.Uri;
import android.os.Bundle;

import com.arthenica.ffmpegkit.FFmpegSession;
import com.arthenica.ffmpegkit.FFmpegSessionCompleteCallback;
import com.arthenica.ffmpegkit.ReturnCode;
import com.google.android.material.slider.Slider;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;
import com.arthenica.ffmpegkit.FFmpegKit;
import com.longdo.mjpegviewer.MjpegView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class CameraActivity extends AppCompatActivity {

    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private ImageView imageViewExit;
    private MjpegView mjpegView ;
    private String ip_address;
    private String port;
    private Switch undistortionSwitch;
    private FFmpegSession session;
    private Slider panSlider;
    private Slider tiltSlider;



    SQLiteDatabase db = null;
    String url = "";

    private boolean isRecording = false;

    public void updateCameraAvatar(String ipAddress, String port){
       if(mjpegView.isStarted){
            Bitmap b = mjpegView.lastBitmap;
           Bitmap cameraAvatar = Bitmap.createScaledBitmap(b, 120, 120, false);
            String fname = "record" +"id"+ ".jpg";
            File file = new File(getBaseContext().getFilesDir(), fname);
            FileOutputStream cout = null;
//            if(file.exists())
//                file.delete();
            try {
                cout = new FileOutputStream(file);
                cameraAvatar.compress(Bitmap.CompressFormat.JPEG, 85, cout);
                cout.flush();
                cout.close();
            } catch (IOException e) {
                Toast.makeText(getBaseContext(), "update failed", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            Cursor c = db.rawQuery("SELECT * FROM Device where 'ico_url' = 'horse'",null);
            //Log.d("My Test", "All is Ok right here!");
            ContentValues data=new ContentValues();
            data.put("ip_address",ipAddress);
            data.put("port",port);
            data.put("ico_url",file.getAbsolutePath());
            db.update("Device", data, "id=" + c.getString(0), null);
           return;

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        Bundle bundle = getIntent().getExtras();
        String stringValue = bundle.getString("playinfo");

        imageView1 = (ImageView) findViewById(R.id.imageView3);
        imageView2 = (ImageView) findViewById(R.id.imageView13);
        imageView3 = (ImageView) findViewById(R.id.imageView14);
        imageViewExit = (ImageView) findViewById(R.id.imageViewExit);
        mjpegView = findViewById(R.id.mjpegView);
        undistortionSwitch = (Switch) findViewById(R.id.undistortionSwitch);
        this.panSlider = (Slider)this.findViewById(R.id.panSlider);
        this.tiltSlider = (Slider)this.findViewById(R.id.tiltSlider);

        try {
            db= CameraActivity.this.openOrCreateDatabase("sight.db", Context.MODE_PRIVATE,null);
        }catch (Exception e) {
            Toast.makeText(CameraActivity.this, "Can not access database: "+ e.toString(), Toast.LENGTH_SHORT).show();
            db.close();
            return;
        }


        Cursor c = db.rawQuery("SELECT * FROM Device",null);
        //Log.d("My Test", "All is Ok right here!");
        c.moveToPosition(Integer.parseInt(stringValue));
//        Toast.makeText(this, stringValue, Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, c.getString(0), Toast.LENGTH_SHORT).show();
        url = "http://"+c.getString(1)+":"+c.getString(2)+"/?action=stream";
        ip_address = c.getString(1);
        port = c.getString(2 );

        panSlider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                mjpegView.panValue =  value;
            }
        });

        tiltSlider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                mjpegView.tiltValue = value;
            }
        });

        undistortionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mjpegView.setDistortion(true);
                } else {
                    // The toggle is disabled
                    mjpegView.setDistortion(false);
                }
            }
        });

        imageView1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                if(!isRecording){
                    isRecording = true;
                    imageView1.setImageResource(R.drawable.record);
                    if(mjpegView.isStarted){
                        Bitmap cameraImage = mjpegView.lastBitmap;

                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
                        SimpleDateFormat yformat = new SimpleDateFormat("yyyy");
                        String strTime = mdformat.format(calendar.getTime());
                        String strYear = yformat.format(calendar.getTime());
                        SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
                        String month_name = month_date.format(calendar.getTime());
                        SimpleDateFormat dformat = new SimpleDateFormat(    "dd");
                        String strDay = dformat.format(calendar.getTime());
                        int weekDay = calendar.DAY_OF_WEEK;

//                        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
//                        File myDir = new File(path + "/recorded_images");
//                        myDir.mkdirs();
                        String fname = "record" +strTime+ ".jpg";
                        File file = new File(getBaseContext().getFilesDir(), fname);
                        String baseDir = getBaseContext().getFilesDir().toString();
//                        String path = Environment.getExternalStorageDirectory().toString();
//                        String fileName = path+"/recorded:"+strTime+strDay+month_name+strYear+".png";

                        FileOutputStream out = null;
                        try {
                            out = new FileOutputStream(file);
                            cameraImage.compress(Bitmap.CompressFormat.JPEG, 85, out);
                            out.flush();
                            out.close();
                        } catch (IOException e) {
                            Toast.makeText(getBaseContext(), "save failed", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                        //Toast.makeText(CameraActivity.this, file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                       // imageView3.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
                        ContentValues  val;
                        Random r = new Random();
                        int i1 = r.nextInt(10000 - 0) + 0;
                        String recordFileName = baseDir+"/video"+String.valueOf(i1)+".mp4";

                     //   Toast.makeText(CameraActivity.this, recordFileName, Toast.LENGTH_SHORT).show();

                        val = new ContentValues();
                        val.put("date", strDay+", "+month_name+", "+strYear);
                        val.put("time", strTime);
                        val.put("img_url", file.getAbsolutePath());
                        val.put("mov_url", recordFileName);
                        db.insert("History", null, val);

                    //    Toast.makeText(CameraActivity.this, url, Toast.LENGTH_SHORT).show();
                        String exeStr = "-y -i "+url+" -c:v mpeg4 "+ recordFileName;

                        session = FFmpegKit.executeAsync((exeStr), new FFmpegSessionCompleteCallback() {

                            @Override
                            public void apply(FFmpegSession session) {
                                Toast.makeText(CameraActivity.this, "FFMpeg record start", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                else
                {
                    FFmpegKit.cancel();
                    isRecording = false;
                    imageView1.setImageResource(R.drawable.process);
                }

            }
        });

        imageView2.setOnClickListener(new View.OnClickListener(){
            public void clearDatabase() {
                //String clearDBQuery = "DELETE FROM Device WHERE id = '"+Integer.parseInt(stringValue)+"'";
                String clearDBQuery = "DELETE FROM Device WHERE id='"+(Integer.parseInt(stringValue)+1)+"'";

                db.execSQL(clearDBQuery);
                Intent mainIntent = new Intent(CameraActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
            }
            public void onClick(View v) {
               clearDatabase();
            }
        });

        imageView3.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                if(undistortionSwitch.getVisibility() == View.INVISIBLE) {
                    undistortionSwitch.setVisibility(View.VISIBLE);
                    panSlider.setVisibility(View.VISIBLE);
                    tiltSlider.setVisibility(View.VISIBLE);
                }//               Intent recordIntent = new Intent(CameraActivity.this,RecordviewAcitvity.class);
//               startActivity(recordIntent);
               else {
                   undistortionSwitch.setVisibility(View.INVISIBLE);
                   panSlider.setVisibility(View.INVISIBLE);
                   tiltSlider.setVisibility(View.INVISIBLE);
               }
            }
        });

        imageViewExit.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                finish();
            }
        });


        mjpegView.setAdjustHeight(true);
        //view.setAdjustHeight(true);
        mjpegView.setMode(MjpegView.MODE_STRETCH);
        //view1.setMode(MjpegView.MODE_FIT_HEIGHT);
        //view1.setMsecWaitAfterReadImageError(1000);
        mjpegView.setUrl(url);
        //mjpegView.setDistortion(false);
        mjpegView.startStream();

        if(stringValue == "-1") {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    updateCameraAvatar(ip_address, port );
                    Intent refresh = new Intent(CameraActivity.this, MainActivity.class);
                    CameraActivity.this.startActivity(refresh);
                }
            }, 5000);
        }


    }
}