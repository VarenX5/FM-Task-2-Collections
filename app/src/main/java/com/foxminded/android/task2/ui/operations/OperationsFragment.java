package com.foxminded.android.task2.ui.operations;

import android.os.Bundle;
import android.util.Log;
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

import com.foxminded.android.task2.BenchmarkApp;
import com.foxminded.android.task2.R;
import com.foxminded.android.task2.databinding.FragmentCollectionsBinding;
import com.foxminded.android.task2.dto.OperationItem;
import com.foxminded.android.task2.model.CollectionsOperations;
import com.foxminded.android.task2.model.FragmentViewModelFactory;
import com.foxminded.android.task2.model.FragmentViewModel;
import com.foxminded.android.task2.model.MapsOperations;
import com.foxminded.android.task2.model.Operations;

import java.util.List;
import java.util.Objects;

public class OperationsFragment extends Fragment {
    private static final String MAPS_FRAGMENT = "MapsFragment";
    private static final String FRAGMENT_NAME = "FragmentName";
    private FragmentViewModel mViewModel;
    private Operations operations;
    private FragmentCollectionsBinding mBinding;
    private RecyclerView mRecyclerView;
    private final OperationsAdapter mAdapter = new OperationsAdapter();
    private boolean isExecutionOn = false;


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
        if (fragmentName.equals(MAPS_FRAGMENT)) {
            operations = new MapsOperations(BenchmarkApp.getInstance());
        } else {
            operations = new CollectionsOperations(BenchmarkApp.getInstance());
        }
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
        mViewModel = new ViewModelProvider(this, new FragmentViewModelFactory(Objects.requireNonNull(getActivity()).getApplication(), operations)).get(FragmentViewModel.class);

        mViewModel.getCollectionsLiveData().observe(getActivity(), new Observer<List<OperationItem>>() {
            @Override
            public void onChanged(List<OperationItem> operationsList) {
                mAdapter.setItems(operationsList);
            }
        });

        mViewModel.getIsExecutionOnLiveData().observe(getActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isExecutionOnLiveData) {
                isExecutionOn = isExecutionOnLiveData;
                if (!isExecutionOn) {
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

        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), mViewModel.getColumnCount()));
        mRecyclerView.setAdapter(mAdapter);

        mBinding.startButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                String numOfOperation = mBinding.editTextOperations.getText().toString().trim();
                String numOfThreads = mBinding.editTextThreads.getText().toString().trim();

                if (numOfOperation.equals("0") || numOfOperation.isEmpty()) {
                    Toast.makeText(getActivity(), getString(R.string.message_need_more_than_zero_operations), Toast.LENGTH_LONG).show();
                    mBinding.editTextOperations.setText("");
                    mBinding.startButton.setChecked(false);
                } else if (numOfThreads.equals("0") || numOfThreads.isEmpty()) {
                    Toast.makeText(getActivity(), getString(R.string.message_need_more_than_zero_threads), Toast.LENGTH_LONG).show();
                    mBinding.editTextThreads.setText("");
                    mBinding.startButton.setChecked(false);
                } else {
                    mViewModel.startOfExecution(Integer.parseInt(mBinding.editTextThreads.getText().toString()), Integer.parseInt(mBinding.editTextOperations.getText().toString()));
                }
            } else {
                if (isExecutionOn) {
                    mViewModel.forceShutdownExecution();
                }
            }
        });
    }


}