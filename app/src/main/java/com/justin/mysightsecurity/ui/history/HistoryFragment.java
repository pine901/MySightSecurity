package com.justin.mysightsecurity.ui.history;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.appbar.AppBarLayout;
import com.justin.mysightsecurity.CustomExpandableListAdapter;
import com.justin.mysightsecurity.R;
import com.justin.mysightsecurity.databinding.FragmentHistoryBinding;
import com.justin.mysightsecurity.ui.add_device.AddDeviceFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HistoryFragment extends Fragment {

    private FragmentHistoryBinding binding;
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, HashMap<String, String>> expandableListDetail;
    SQLiteDatabase db;

    @SuppressLint("DefaultLocale")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HistoryViewModel dashboardViewModel =
                new ViewModelProvider(this).get(HistoryViewModel.class);

        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        AppBarLayout bar =(AppBarLayout) getActivity().findViewById(R.id.appbarLayout);
//        bar.setVisibility(View.VISIBLE);

        final TextView textView = binding.textDashboard;
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        //db config Edited BY BINGO!

        try {
            db= getActivity().openOrCreateDatabase("sight.db", Context.MODE_PRIVATE,null);
        }catch (Exception e) {
            Toast.makeText(getActivity(), "Can not access database: "+ e.toString(), Toast.LENGTH_SHORT).show();
            db.close();
        }

        Cursor c = db.rawQuery("SELECT * FROM History",null);
        //Log.d("My Test", "All is Ok right here!");
        if(c.getCount()==0) {
            //Log.d("My Test", "All is Ok right if");
            Toast.makeText(getActivity(), "No History", Toast.LENGTH_SHORT).show();
        } else {
            Log.d("My Test", "All is Ok right else");
            c.moveToFirst();
            expandableListDetail = new HashMap<String, HashMap<String, String>>();
            expandableListTitle = new ArrayList<String>();



            do {
                HashMap<String, String> buffer = new HashMap<String, String>();

                buffer.put("time", c.getString(2));
                buffer.put("image",c.getString(3));
//                Toast.makeText(getActivity(),c.getString(3), Toast.LENGTH_SHORT).show();
//
//                Toast.makeText(getActivity(),c.getString(2), Toast.LENGTH_SHORT).show();
////                Log.d("bingo", String.format("%d", buffer.size()));
//                Toast.makeText(getActivity(),String.format("%d", buffer.size()), Toast.LENGTH_SHORT ).show();
                expandableListDetail.put(c.getString(1), buffer);
                expandableListTitle.add(c.getString(1));
            } while (c.moveToNext());
            expandableListView = (ExpandableListView) root.findViewById(R.id.expandableListView);

            expandableListAdapter = new CustomExpandableListAdapter(getActivity(), HistoryFragment.this, expandableListTitle, expandableListDetail);
            expandableListView.setAdapter(expandableListAdapter);

            DisplayMetrics metrics = new DisplayMetrics();
            WindowManager wm= getActivity().getWindowManager();
            wm.getDefaultDisplay().getMetrics(metrics);
            int width = metrics.widthPixels;

            expandableListView.setIndicatorBounds(width - GetPixelFromDips(50), width - GetPixelFromDips(10));

//            expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
//
//                @Override
//                public void onGroupExpand(int groupPosition) {
////                expandableListView.setGroupIndicator();
//
//                }
//            });
//
//            expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
//
//                @Override
//                public void onGroupCollapse(int groupPosition) {
//
//                    Toast.makeText(getActivity(), String.format("%d", groupPosition), Toast.LENGTH_SHORT).show();
//
//                }
//            });
//
//            expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//                @Override
//                public boolean onChildClick(ExpandableListView parent, View v,
//                                            int groupPosition, int childPosition, long id) {
//
//
//                    Toast.makeText(getActivity(), String.format("bingo: %d", groupPosition), Toast.LENGTH_SHORT).show();
////                    Bundle bundle = new Bundle();
////                    bundle.putString("playinfo", str);
////
////                    NavHostFragment.findNavController(HistoryFragment.this)
////                            .navigate(R.id.action_history_to_playFragment, bundle);
//                    return true;
//                }
//            });

        }
        //Log.d("My Test", "Before close");
        c.close();
        //Log.d("My Test", "before db.close");
        db.close();
        //Log.d("My Test", "afer db.close");
        //Edited by BINGO!


        return root;
    }
    public int GetPixelFromDips(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}