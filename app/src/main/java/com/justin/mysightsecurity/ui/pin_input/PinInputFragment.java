package com.justin.mysightsecurity.ui.pin_input;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.justin.mysightsecurity.MainActivity;
import com.justin.mysightsecurity.PinInputActivity;
import com.justin.mysightsecurity.PinSetupActivity;
import com.justin.mysightsecurity.SplashActivity;
import com.justin.mysightsecurity.databinding.FragmentPininputBinding;

public class PinInputFragment extends Fragment {

    private FragmentPininputBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PinInputViewModel homeViewModel =
                new ViewModelProvider(this).get(PinInputViewModel.class);

        binding = FragmentPininputBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

//        Intent pininput = new Intent(getActivity(), PinInputActivity.class);
//        ((MainActivity)getActivity()).startActivity(pininput);


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}