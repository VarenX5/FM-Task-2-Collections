package com.foxminded.android.task2.ui.operations;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.foxminded.android.task2.BenchmarkApp;
import com.foxminded.android.task2.R;
import com.foxminded.android.task2.databinding.FragmentCollectionsBinding;
import com.foxminded.android.task2.dto.OperationItem;
import com.foxminded.android.task2.model.MapsOperations;
import com.foxminded.android.task2.model.Operations;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MapsFragment extends Fragment {

    private final Operations operations = new MapsOperations(BenchmarkApp.getInstance());
    private FragmentCollectionsBinding mBinding;
    private RecyclerView mRecyclerView;
    private final CollectionsRecyclerAdapter mAdapter = new CollectionsRecyclerAdapter();
    private List<OperationItem> mCollectionsList;
    private ExecutorService mExecutorService;

    public static MapsFragment newInstance() {
        return new MapsFragment();
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
        mCollectionsList = operations.getOperations();
        mAdapter.setItems(mCollectionsList);

        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), operations.getColumnCount()));
        mRecyclerView.setAdapter(mAdapter);

        mBinding.startButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                String numOfOperation = mBinding.editTextOperations.getText().toString();
                String numOfThreads = mBinding.editTextThreads.getText().toString();

                if (numOfOperation.equals("0") || numOfOperation.isEmpty()) {
                    Toast.makeText(getActivity(), getString(R.string.message_need_more_than_zero_operations), Toast.LENGTH_LONG).show();
                    mBinding.editTextOperations.setText("");
                } else if (numOfThreads.equals("0") || numOfThreads.isEmpty()) {
                    Toast.makeText(getActivity(), getString(R.string.message_need_more_than_zero_threads), Toast.LENGTH_LONG).show();
                    mBinding.editTextThreads.setText("");
                } else {
                    startOfOperationsList();
                    mAdapter.notifyDataSetChanged();
                    startOfExecution();
                }
            } else {
                shutdownExecution();
            }
        });
    }

    private void startOfOperationsList() {
        Resources res = getResources();
        String[] operations = res.getStringArray(R.array.name_of_map_operations);
        String ms = getString(R.string.n_a_ms);
        for (int i = 0; i < 6; i++) {
            mCollectionsList.set(i, new OperationItem(operations[i], ms, true));
        }
        mAdapter.notifyDataSetChanged();
    }

    private void startOfExecution() {
        mExecutorService = Executors.newFixedThreadPool(Integer.parseInt(mBinding.editTextThreads.getText().toString()));
        final int amountOfElements = Integer.parseInt(mBinding.editTextOperations.getText().toString());

        for (OperationItem operation : operations.getOperations()) {
            mExecutorService.submit(() -> {
                operation.setTime(operations.measureTime(amountOfElements, operation));

                // Update ui
                // check if all operations done and notify ui about this, release executor
            });
        }
    }

    public void shutdownExecution() {
    }

}