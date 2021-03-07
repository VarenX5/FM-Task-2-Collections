package com.foxminded.android.task2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.foxminded.android.task2.databinding.FragmentCollectionsBinding;
import com.google.android.material.textfield.TextInputEditText;

public class CollectionsFragment extends Fragment {

    static final String ARGUMENT_OBJ = "object";
    private FragmentCollectionsBinding mBinding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentCollectionsBinding.inflate(inflater,container,false);
        //View view = mBinding.getRoot();
        return mBinding.getRoot();
    }


    public static CollectionsFragment newInstance(){
        return new CollectionsFragment();
    }
}
