package com.justin.mysightsecurity.ui.pin_input;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PinInputViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public PinInputViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}