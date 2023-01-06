package com.justin.mysightsecurity;

import android.app.FragmentManager;
import android.content.ClipData;

import android.database.sqlite.SQLiteDatabase;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.longdo.mjpegviewer.MjpegView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlayFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private SQLiteDatabase db;
    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;


    public PlayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlayFragment newInstance(String param1, String param2) {
        PlayFragment fragment = new PlayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        //((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        AppBarLayout bar =(AppBarLayout) getActivity().findViewById(R.id.appbarLayout);
        bar.setVisibility(View.INVISIBLE);
        BottomNavigationView bottomNav = (BottomNavigationView) getActivity().findViewById(R.id.nav_view);
        bottomNav.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        AppBarLayout bar =(AppBarLayout) getActivity().findViewById(R.id.appbarLayout);
        bar.setVisibility(View.VISIBLE);
//
//        FragmentManager fm = getActivity().getFragmentManager();
//        fm.popBackStackImmediate();
        BottomNavigationView bottomNav = (BottomNavigationView) getActivity().findViewById(R.id.nav_view);
        bottomNav.setVisibility(View.VISIBLE);

        //((MainActivity)getActivity()).dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_play, container, false);
        Toast.makeText(getActivity(), getArguments().getString("playinfo"), Toast.LENGTH_SHORT).show();

        imageView1 = (ImageView) view.findViewById(R.id.imageView3);
        imageView2 = (ImageView) view.findViewById(R.id.imageView13);
        imageView3 = (ImageView) view.findViewById(R.id.imageView14);
        imageView1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Toast.makeText(getActivity(), "1button", Toast.LENGTH_SHORT).show();

                Log.e("imageview1","image view 1 clicked!");
            }
        });

        imageView2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Toast.makeText(getActivity(), "1button", Toast.LENGTH_SHORT).show();

                Log.e("imageview2","image view 2 clicked!");

            }
        });

        imageView3.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Toast.makeText(getActivity(), "1button", Toast.LENGTH_SHORT).show();

                Log.e("imageview3","image view 3 clicked!");

            }
        });

        int TIMEOUT = 5; //seconds

//        MjpegView mjpegView = view.findViewById(R.id.mjpegView);
//        mjpegView.setAdjustHeight(true);
//        //view.setAdjustHeight(true);
//        mjpegView.setMode(MjpegView.MODE_FIT_WIDTH);
//        //view1.setMode(MjpegView.MODE_FIT_HEIGHT);
//        //view1.setMsecWaitAfterReadImageError(1000);
//        mjpegView.setUrl("https://bma-itic1.iticfoundation.org/mjpeg2.php?camid=test");
////        mjpegView.setDistortion(false);
//        mjpegView.startStream();


        return inflater.inflate(R.layout.fragment_play, container, false);
    }
}