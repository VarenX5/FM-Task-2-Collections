package com.foxminded.android.task2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.foxminded.android.task2.databinding.FragmentCollectionsBinding;

public class MapsFragment extends Fragment {
    private FragmentCollectionsBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentCollectionsBinding.inflate(inflater,container,false);

        View v = inflater.inflate(R.layout.fragment_collections, container, false);
        return mBinding.getRoot();
    }

    public static CollectionsFragment newInstance() {
        return new CollectionsFragment();
    }
}

