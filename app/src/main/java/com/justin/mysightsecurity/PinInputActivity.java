package com.justin.mysightsecurity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.fragment.NavHostFragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.justin.mysightsecurity.ui.add_device.AddDeviceFragment;

public class PinInputActivity extends Activity {

    private SQLiteDatabase db;
    private TextView text1, text2, text3, text4;
    int count;
    private String password;

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Are you really want to exit?");

        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //Your action here
                PinInputActivity.this.finishAffinity();
                // checking end
            }
        });

        alert.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        return;
                    }
                });

        alert.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_input);

        text1 = (TextView) findViewById(R.id.editTextNumberPassword1);
        text2 = (TextView) findViewById(R.id.editTextNumberPassword2);
        text3 = (TextView) findViewById(R.id.editTextNumberPassword3);
        text4 = (TextView) findViewById(R.id.editTextNumberPassword4);

        text1.clearFocus();
        count = 0;

        try {
            db=PinInputActivity.this.openOrCreateDatabase("sight.db", Context.MODE_PRIVATE,null);
        }catch (Exception e) {
            Toast.makeText(PinInputActivity.this, "Can not access database: "+ e.toString(), Toast.LENGTH_SHORT).show();
            return;
        }

        Cursor c = db.rawQuery("SELECT * FROM User",null);

        if(c.getCount()==0) {
            Toast.makeText(this, "Database Empty", Toast.LENGTH_SHORT).show();
        } else {
            c.moveToFirst();
            password = c.getString(2);
        }

        c.close();
        db.close();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        Intent pininput = new Intent(PinInputActivity.this, PinSetupActivity.class);
//        PinInputActivity.this.startActivity(pininput);
        return super.onTouchEvent(event);
    }
    public void setPasswordText(int count, String num) {
        if (count == 1){
            text1.setText(num);
        } else if (count == 2) {
            text2.setText(num);
        } else if (count == 3) {
            text3.setText(num);
        } else if (count == 4) {
            text4.setText(num);
        }else {
            Toast.makeText(this, "Something go wrong", Toast.LENGTH_SHORT);
        }

        return;
    }
    public void onClick(View view)
    {
        switch (view.getId()) {
            case R.id.button1:
                // Do something
                count++;
                setPasswordText(count, "1");
                if(count == 4){
                    ConfirmPassword();
                }
                break;
            case R.id.button2:
                // Do something
                count++;
                setPasswordText(count, "2");
                if(count == 4){
                    ConfirmPassword();
                }
                break;
            case R.id.button3:
                // Do something
                count++;
                setPasswordText(count, "3");
                if(count == 4){
                    ConfirmPassword();
                }
                break;
            case R.id.button4:
                // Do something
                count++;
                setPasswordText(count, "4");
                if(count == 4){
                    ConfirmPassword();
                }
                break;
            case R.id.button5:
                // Do something
                count++;
                setPasswordText(count, "5");
                if(count == 4){
                    ConfirmPassword();
                }
                break;
            case R.id.button6:
                // Do something
                count++;
                setPasswordText(count, "6");
                if(count == 4){
                    ConfirmPassword();
                }
                break;
            case R.id.button7:
                // Do something
                count++;
                setPasswordText(count, "7");
                if(count == 4){
                    ConfirmPassword();
                }
                break;
            case R.id.button8:
                // Do something
                count++;
                setPasswordText(count, "8");
                if(count == 4){
                    ConfirmPassword();
                }
                break;
            case R.id.button9:
                // Do something
                count++;
                setPasswordText(count, "9");
                if(count == 4){
                    ConfirmPassword();
                }
                break;
            case R.id.button10:
                // Do something
                text4.setText("");
                text3.setText("");
                text2.setText("");
                text1.setText("");

                text1.clearFocus();
                count = 0;
                break;
            case R.id.button11:
                // Do something
                count++;
                setPasswordText(count, "0");
                if(count == 4){
                    ConfirmPassword();
                }
                break;
            default:

                break;
        }
    }

    public Boolean ConfirmPassword(){
        String password = text1.getText().toString() +text2.getText().toString() + text3.getText().toString() + text4.getText().toString();

        if(this.password.equals(password)) {
            text4.setText("");
            text3.setText("");
            text2.setText("");
            text1.setText("");
            count = 0;
            Intent pininput = new Intent(PinInputActivity.this, MainActivity.class);
            PinInputActivity.this.startActivity(pininput);
            return true;
        } else {
            text4.setText("");
            text3.setText("");
            text2.setText("");
            text1.setText("");

            text1.clearFocus();
            count = 0;
            Toast.makeText(this, "Wrong Password!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}