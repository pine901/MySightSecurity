package com.justin.mysightsecurity.ui.add_device;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddDeviceViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public AddDeviceViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}