package com.foxminded.android.task2.ui.operations;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class MapsFragment extends Fragment {

    private final MapsOperations mapsOperations = new MapsOperations(BenchmarkApp.getInstance());
    private FragmentCollectionsBinding mBinding;
    private RecyclerView mRecyclerView;
    private final CollectionsRecyclerAdapter mAdapter = new CollectionsRecyclerAdapter();
    private List<OperationItem> mCollectionsList;
    private ExecutorService mExecutorService;
    private boolean isExecutionOn = false;

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
        mCollectionsList = mapsOperations.getOperations();
        mAdapter.setItems(mCollectionsList);

        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), mapsOperations.getColumnCount()));
        mRecyclerView.setAdapter(mAdapter);

        mBinding.startButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                String numOfOperation = mBinding.editTextOperations.getText().toString().trim();
                String numOfThreads = mBinding.editTextThreads.getText().toString().trim();

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
                if(isExecutionOn){
                    forceShutdownExecution();
                }
            }
        });
    }

    private void startOfOperationsList() {
        Resources res = getResources();
        String[] operations = res.getStringArray(R.array.name_of_map_operations);
        String ms = getString(R.string.n_a_ms);
        for (int i = 0; i < 6; i++) {
            mCollectionsList.set(i, new OperationItem(operations[i], ms, true, i));
        }
        mAdapter.setItems(mCollectionsList);
    }

    private void startOfExecution() {
        isExecutionOn = true;
        mExecutorService = Executors.newFixedThreadPool(Integer.parseInt(mBinding.editTextThreads.getText().toString()));
        final int amountOfElements = Integer.parseInt(mBinding.editTextOperations.getText().toString());
        AtomicInteger counter = new AtomicInteger(0);
        for (OperationItem operation : mapsOperations.getOperations()) {
            mExecutorService.submit(() -> {
                operation.setTime(Double.toString(mapsOperations.measureTime(amountOfElements, operation)));
                //mCollectionsList.set(operation.getNumber(), operation);
                //mAdapter.setItems(mCollectionsList);
                mAdapter.setItem(operation.getNumber(), operation);
                counter.getAndIncrement();
                if (counter.get() == 6) {
                    getActivity().runOnUiThread(this::executionIsDone);
                    mExecutorService.shutdownNow();
                    try {
                        if (!mExecutorService.awaitTermination(60, TimeUnit.MILLISECONDS)) {
                            mExecutorService.shutdownNow();
                            if (!mExecutorService.awaitTermination(60, TimeUnit.SECONDS))
                                System.err.println("Pool did not terminate");
                        }
                    } catch (InterruptedException e) {
                        mExecutorService.shutdownNow();
                    }
                }
            });

        }
    }

    private void executionIsDone() {
        isExecutionOn=false;
        Toast.makeText(getActivity(), getString(R.string.execution_done), Toast.LENGTH_LONG).show();
        mBinding.startButton.setChecked(false);
    }

    public void forceShutdownExecution() {
        Log.d("wtf", "Execution was forced to shutdown");
        mExecutorService.shutdownNow();
        mBinding.startButton.setChecked(false);
        Toast.makeText(getActivity(), getString(R.string.execution_shutdown), Toast.LENGTH_LONG).show();
        List<OperationItem> operationItemList = new ArrayList<>(mAdapter.getItems());
        for (OperationItem item : operationItemList) {
            item.setOperationOn(false);
        }
        mAdapter.setItems(operationItemList);
    }

}