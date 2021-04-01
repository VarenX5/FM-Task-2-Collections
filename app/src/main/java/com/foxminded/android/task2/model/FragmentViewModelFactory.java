package com.foxminded.android.task2.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.foxminded.android.task2.BenchmarkApp;

public class FragmentViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {
    private static final String MAPS_FRAGMENT = "MapsFragment";
    private final Operations mOperations;
    private final Application mApplication;

    /**
     * Creates a {@code AndroidViewModelFactory}
     *
     * @param application an application to pass in {@link AndroidViewModel}
     */
    public FragmentViewModelFactory(@NonNull Application application, String fragmentName) {
        super(application);
        if (fragmentName.equals(MAPS_FRAGMENT)) {
            mOperations = new MapsOperations(BenchmarkApp.getInstance());
        } else {
            mOperations = new CollectionsOperations(BenchmarkApp.getInstance());
        }
        mApplication = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new FragmentViewModel(mApplication, mOperations);
    }


}
