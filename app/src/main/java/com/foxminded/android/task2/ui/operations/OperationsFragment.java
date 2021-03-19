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
import com.foxminded.android.task2.model.CollectionsOperations;
import com.foxminded.android.task2.model.MapsOperations;
import com.foxminded.android.task2.model.Operations;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class OperationsFragment extends Fragment {
    private static final String MAPS_FRAGMENT = "MapsFragment";
    private static final String FRAGMENT_NAME = "FragmentName";

    private Operations operations;
    private FragmentCollectionsBinding mBinding;
    private RecyclerView mRecyclerView;
    private final OperationsAdapter mAdapter = new OperationsAdapter();
    private List<OperationItem> mCollectionsList;
    private ExecutorService mExecutorService;
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
        mCollectionsList = operations.getOperations();
        mAdapter.setItems(mCollectionsList);

        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), operations.getColumnCount()));
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
                    startOfOperationsList();
                    mAdapter.notifyDataSetChanged();
                    startOfExecution();
                }
            } else {
                if (isExecutionOn) {
                    forceShutdownExecution();
                }
            }
        });
    }

    private void startOfOperationsList() {
        Resources res = getResources();
        String ms = getString(R.string.n_a_ms);
        if (operations.getColumnCount() == 2) {
            String[] operations = res.getStringArray(R.array.name_of_map_operations);
            for (int i = 0; i < 6; i++) {
                mCollectionsList.set(i, new OperationItem(operations[i], ms, true, i));
            }
        } else {
            String[] operations = res.getStringArray(R.array.name_of_collections_operations);
            for (int i = 0; i < 21; i++) {
                mCollectionsList.set(i, new OperationItem(operations[i], ms, true, i));
            }
        }

        mAdapter.setItems(mCollectionsList);
    }

    private void startOfExecution() {
        Integer counterAmount;
        if (operations.getColumnCount() == 2) {
            counterAmount = 6;
        } else {
            counterAmount = 21;
        }
        isExecutionOn = true;
        mExecutorService = Executors.newFixedThreadPool(Integer.parseInt(mBinding.editTextThreads.getText().toString()));
        final int amountOfElements = Integer.parseInt(mBinding.editTextOperations.getText().toString());
        AtomicInteger counter = new AtomicInteger(0);
        for (OperationItem operation : operations.getOperations()) {
            mExecutorService.submit(() -> {
                operation.setTime(Double.toString(operations.measureTime(amountOfElements, operation)));
                mAdapter.setItem(operation.getNumber(), operation);
                counter.getAndIncrement();
                if (counter.get() == counterAmount) {
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
        isExecutionOn = false;
        Toast.makeText(getActivity(), getString(R.string.execution_done), Toast.LENGTH_LONG).show();
        mBinding.startButton.setChecked(false);
    }

    public void forceShutdownExecution() {
        Log.d("wtf", "Execution was forced to shutdown");
        mExecutorService.shutdownNow();
        isExecutionOn = false;
        mBinding.startButton.setChecked(false);
        Toast.makeText(getActivity(), getString(R.string.execution_shutdown), Toast.LENGTH_LONG).show();
        List<OperationItem> operationItemList = new ArrayList<>(mAdapter.getItems());
        for (OperationItem item : operationItemList) {
            item.setOperationOn(false);
        }
        mAdapter.setItems(operationItemList);
    }

}