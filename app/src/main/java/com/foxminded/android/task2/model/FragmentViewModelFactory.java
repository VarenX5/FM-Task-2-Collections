package com.foxminded.android.task2.model;

import android.app.Application;
import android.provider.SyncStateContract;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.foxminded.android.task2.BenchmarkApp;
import com.foxminded.android.task2.ui.operations.OperationsFragment;

import java.lang.invoke.ConstantCallSite;

public class FragmentViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {
    private final Operations mOperations;

    /**
     * Creates a {@code AndroidViewModelFactory}
     *
     * @param application an application to pass in {@link AndroidViewModel}
     */
    public FragmentViewModelFactory(@NonNull Application application, String fragmentName) {
        super(application);
        if (fragmentName.equals(OperationsFragment.MAPS_FRAGMENT)) {
            mOperations = new MapsOperations(application);
        } else {
            mOperations = new CollectionsOperations(application);
        }
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new FragmentViewModel(mOperations);
    }


}
