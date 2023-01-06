package com.justin.mysightsecurity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.justin.mysightsecurity.databinding.ActivityMainBinding;
import com.longdo.mjpegviewer.MjpegView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private TextView titleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setItemIconTintList(null);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.pin_input, R.id.history, R.id.add_device, R.id.pin_setup, R.id.scan)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        titleText = findViewById(R.id.titleText);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                
                switch(navDestination.getId()){

                    case R.id.pin_input:
                        Intent pininput = new Intent(MainActivity.this, PinInputActivity.class);
                        MainActivity.this.startActivity(pininput);
                        break;
                    case R.id.history:
                        titleText.setText("History");
                        break;
                    case R.id.add_device:
                        titleText.setText("Add New Device");
                        break;
                    case R.id.pin_setup:
                        titleText.setText("Password Reset");

                        Intent intent = new Intent(MainActivity.this, PinSetupActivity.class);
                        MainActivity.this.startActivity(intent);
                        break;
                    case R.id.scan:
                        titleText.setText("Gallery");
                        break;
                    default:
                        titleText.setText("");
                        break;

                }
            }
        });
    }


}