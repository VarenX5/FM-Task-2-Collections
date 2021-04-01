package com.foxminded.android.task2.ui.operations;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.foxminded.android.task2.databinding.FragmentCollectionsBinding;
import com.foxminded.android.task2.dto.OperationItem;
import com.foxminded.android.task2.model.FragmentViewModelFactory;
import com.foxminded.android.task2.model.FragmentViewModel;

import java.util.List;
import java.util.Objects;

public class OperationsFragment extends Fragment {
    public static final String MAPS_FRAGMENT = "MapsFragment";
    private static final String FRAGMENT_NAME = "FragmentName";
    private FragmentViewModel mViewModel;
    private FragmentCollectionsBinding mBinding;
    private RecyclerView mRecyclerView;
    private final OperationsAdapter mAdapter = new OperationsAdapter();


    public static OperationsFragment newInstance(String argument) {
        OperationsFragment operationsFragment = new OperationsFragment();
        Bundle args = new Bundle();
        args.putString(FRAGMENT_NAME, argument);
        operationsFragment.setArguments(args);
        return operationsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String fragmentName = getArguments().getString(FRAGMENT_NAME);

        mViewModel = new ViewModelProvider(this, new FragmentViewModelFactory(Objects.requireNonNull(getActivity()).getApplication(), fragmentName)).get(FragmentViewModel.class);
        mViewModel.getCollectionsLiveData().observe(getActivity(), new Observer<List<OperationItem>>() {
            @Override
            public void onChanged(List<OperationItem> operationsList) {
                mAdapter.setItems(operationsList);
            }
        });
        mViewModel.setOperations();
        mViewModel.getIsExecutionOnLiveData().observe(getActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isExecutionOnLiveData) {
                if (!isExecutionOnLiveData) {
                    mBinding.startButton.setChecked(false);
                }
            }
        });
        mViewModel.getToastText().observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String toastText) {
                Toast.makeText(getActivity(), toastText, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentCollectionsBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mRecyclerView = mBinding.myRecyclerView;
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), mViewModel.getColumnCount()));
        mRecyclerView.setAdapter(mAdapter);
        mBinding.startButton.setOnCheckedChangeListener((buttonView, isChecked) -> mViewModel.validateAndStart(getThreadsText(), getOperationsText(), isChecked));
    }
    private String getThreadsText(){
        return mBinding.editTextThreads.getText().toString().trim();
    }
    private String getOperationsText(){
        return mBinding.editTextOperations.getText().toString().trim();
    }

}