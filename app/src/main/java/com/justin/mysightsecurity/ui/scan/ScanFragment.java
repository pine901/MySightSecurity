package com.justin.mysightsecurity.ui.scan;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.justin.mysightsecurity.CameraActivity;
import com.justin.mysightsecurity.MainActivity;
import com.justin.mysightsecurity.MyRecyclerViewAdapter;
import com.justin.mysightsecurity.R;

import java.util.ArrayList;

public class ScanFragment extends Fragment {

    private ScanViewModel mViewModel;
    MyRecyclerViewAdapter adapter;
    ArrayList<String> mCameraImagePaths;
    SQLiteDatabase db;
    public static ScanFragment newInstance() {
        return new ScanFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_scan, container, false);

        mCameraImagePaths = new ArrayList<>();
        try {
            db= getActivity().openOrCreateDatabase("sight.db", Context.MODE_PRIVATE,null);
        }catch (Exception e) {
            Toast.makeText(getActivity(), "Can not access database: "+ e.toString(), Toast.LENGTH_SHORT).show();
            db.close();
        }
//        mCameraImagePaths.add("c.getString(3)");


        Cursor c = db.rawQuery("SELECT * FROM Device",null);
        //Log.d("My Test", "All is Ok right here!");
        if(c.getCount()==0) {
            //Log.d("My Test", "All is Ok right if");
            Toast.makeText(getActivity(), "No Camera", Toast.LENGTH_SHORT).show();
        }
        else{
            c.moveToFirst();
            do {
                //Toast.makeText(getActivity(), c.getString(3), Toast.LENGTH_SHORT).show();
               mCameraImagePaths.add(c.getString(3));
            } while (c.moveToNext());
        }

        // set up the RecyclerView
        RecyclerView recyclerView = root.findViewById(R.id.rvCameras);
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);

        adapter = new MyRecyclerViewAdapter(root.getContext(), mCameraImagePaths);
        adapter.setClickListener(new MyRecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                Toast.makeText(getActivity(), "You clicked " + adapter.getItem(position) + " on item position " + position, Toast.LENGTH_SHORT).show();

                Bundle bundle = new Bundle();

//                NavController nav = NavHostFragment.findNavController(ScanFragment.this);
//                nav.navigateUp();
//                nav.navigate(R.id.action_scan_to_playFragment, bundle);
                Intent myIntent = new Intent(getActivity(), CameraActivity.class);
                myIntent.putExtra("playinfo", String.format("%d", position));
                startActivity(myIntent);


            }
        });
        recyclerView.setAdapter(adapter);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ScanViewModel.class);
        // TODO: Use the ViewModel
    }

}