package com.foxminded.android.task2.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class FragmentViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {
    private final Operations mOperations;
    private final Application mApplication;

    /**
     * Creates a {@code AndroidViewModelFactory}
     *
     * @param application an application to pass in {@link AndroidViewModel}
     */
    public FragmentViewModelFactory(@NonNull Application application, Operations operations) {
        super(application);
        mOperations = operations;
        mApplication = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new FragmentViewModel(mApplication, mOperations);
    }


}
