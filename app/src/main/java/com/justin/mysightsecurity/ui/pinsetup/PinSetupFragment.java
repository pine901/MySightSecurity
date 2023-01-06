package com.justin.mysightsecurity.ui.pinsetup;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.justin.mysightsecurity.MainActivity;
import com.justin.mysightsecurity.PinInputActivity;
import com.justin.mysightsecurity.PinSetupActivity;
import com.justin.mysightsecurity.R;

public class PinSetupFragment extends Fragment {

    private PinSetupViewModel mViewModel;

    public static PinSetupFragment newInstance() {
        return new PinSetupFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_pinsetup, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PinSetupViewModel.class);
        // TODO: Use the ViewModel
    }

}