package com.justin.mysightsecurity;

import static android.database.sqlite.SQLiteDatabase.CREATE_IF_NECESSARY;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.widget.Toast;

import com.justin.mysightsecurity.databinding.ActivitySplashBinding;
import com.justin.mysightsecurity.socket.Global;
import com.justin.mysightsecurity.socket.ServerThread;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;
    private static boolean MANUAL_HIDE = false;
    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private ContentValues values, val, deviceVal;
    private final Handler mHideHandler = new Handler(Looper.myLooper());
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar
            if (Build.VERSION.SDK_INT >= 30) {

            } else {
                // Note that some of these constants are new as of API 16 (Jelly Bean)
                // and API 19 (KitKat). It is safe to use them, as they are inlined
                // at compile-time and do nothing on earlier devices.

            }
        }
    };
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();

            if( !MANUAL_HIDE ) {

                // Edited by BINGO
                // Determine whether password is set or not

                SQLiteDatabase db;

                db=SplashActivity.this.openOrCreateDatabase("sight.db", Context.MODE_PRIVATE,null);
                db.execSQL("DROP TABLE IF EXISTS Device");
            //    db.execSQL("DROP TABLE IF EXISTS History");
                String query_user = "CREATE TABLE IF NOT EXISTS User (id INTEGER PRIMARY KEY AUTOINCREMENT, user_email Text, user_password Text)";
                String query_history = "CREATE TABLE IF NOT  EXISTS History (id INTEGER PRIMARY KEY AUTOINCREMENT, date Text, time Text, img_url Text, mov_url Text)";
                String query_device = "CREATE TABLE IF NOT EXISTS Device (id INTEGER PRIMARY KEY AUTOINCREMENT, ip_address Text, port Text,device_id Text, ico_url Text)";
                db.execSQL(query_user);
                db.execSQL(query_history);
                db.execSQL(query_device);
//                val = new ContentValues();
//                val.put("date", "9, 10, 2022");
//                val.put("time", "6:8 PM");
//                val.put("img_url", "/res/drawale/gallery_show_2");
//                val.put("mov_url", "/res/raw/sample.mp4");
//                db.insert("History", null, val);
//

                deviceVal = new ContentValues();
                deviceVal.put("ip_address", "192.168.1.91");
                deviceVal.put("port", "8080");
                deviceVal.put("device_id", "0000:1111");
                deviceVal.put("ico_url", "horse");
                try {
                 //   Toast.makeText(Global.activity, "successfully added!", Toast.LENGTH_LONG).show();

                    db.insertOrThrow("Device", null, deviceVal);

                } catch (Exception e) {
                  //  Toast.makeText(Global.activity, "sorry! this is an error", Toast.LENGTH_LONG).show();
                    Log.d("DB initialization Error", e.toString());
                }

                values = new ContentValues();
                values.put("id", 1);
                values.put("user_email", "securitysight@gmail.com");
                values.put("user_password", "0000");

                try {
                    db.insertOrThrow("User", null, values);

                } catch (Exception e) {
                   Log.d("DB initialization Error", e.toString());
                }

//                db.insert("User", null, values);

                db.close();

                Intent pininput = new Intent(SplashActivity.this, PinInputActivity.class);
                SplashActivity.this.startActivity(pininput);

                //

            }
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (AUTO_HIDE) {
                        delayedHide(AUTO_HIDE_DELAY_MILLIS);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    view.performClick();
                    break;
                default:
                    break;
            }
            return false;
        }
    };
    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mVisible = true;

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        //binding.dummyButton.setOnTouchListener(mDelayHideTouchListener);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(3000);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private void show() {
        // Show the system bar
        if (Build.VERSION.SDK_INT >= 30) {
        } else {
        }
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

}