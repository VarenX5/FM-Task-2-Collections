package com.foxminded.android.task2;

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

import com.foxminded.android.task2.databinding.FragmentCollectionsBinding;
import com.foxminded.android.task2.databinding.FragmentMapsBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MapsFragment extends Fragment {

    private FragmentCollectionsBinding mBinding;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<OperationItem> mCollectionsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentCollectionsBinding.inflate(inflater, container, false);

        mRecyclerView = mBinding.myRecyclerView;
        mRecyclerView.setHasFixedSize(true);
        mCollectionsList = new ArrayList<OperationItem>();
        createOperationsList();
        mAdapter = new MyRecyclerAdapter(mCollectionsList);

        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mRecyclerView.setAdapter(mAdapter);

        mBinding.startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((mBinding.editTextOperations.getText().toString().equals("0")) | (mBinding.editTextOperations.getText().toString().equals(""))) {
                    Toast.makeText(getActivity(), "You need to type more than zero operations", Toast.LENGTH_LONG).show();
                    mBinding.editTextOperations.setText("");
                } else if ((mBinding.editTextThreads.getText().toString().equals("0")) | (mBinding.editTextThreads.getText().toString().equals(""))) {
                    Toast.makeText(getActivity(), "You need to type more than zero threads", Toast.LENGTH_LONG).show();
                    mBinding.editTextThreads.setText("");
                } else {
                    startOfOperationsList();
                    mAdapter.notifyDataSetChanged();
                    startOfExecution();

                }


            }
        });
        return mBinding.getRoot();
    }

    public static MapsFragment newInstance() {
        return new MapsFragment();
    }

    private void createOperationsList() {
        mCollectionsList.add(new OperationItem("Adding to TreeMap: ", "N/A ms", false));
        mCollectionsList.add(new OperationItem("Adding to HashMap: ", "N/A ms", false));

        mCollectionsList.add(new OperationItem("Search in TreeMap: ", "N/A ms", false));
        mCollectionsList.add(new OperationItem("Search in HashMap: ", "N/A ms", false));

        mCollectionsList.add(new OperationItem("Removing from TreeMap: ", "N/A ms", false));
        mCollectionsList.add(new OperationItem("Removing from HashMap: ", "N/A ms", false));
    }

    private void startOfOperationsList() {
        //start of Progress bar animation only for HashMap cause TreeMap not ready
        mCollectionsList.set(0, new OperationItem("Adding to TreeMap: ", "N/A ms", false));
        mCollectionsList.set(1, new OperationItem("Adding to HashMap: ", "N/A ms", true));

        mCollectionsList.set(2, new OperationItem("Search in TreeMap: ", "N/A ms", false));
        mCollectionsList.set(3, new OperationItem("Search in HashMap: ", "N/A ms", true));

        mCollectionsList.set(4, new OperationItem("Removing from TreeMap: ", "N/A ms", false));
        mCollectionsList.set(5, new OperationItem("Removing from HashMap: ", "N/A ms", true));
    }

    private void startOfExecution() {
        ExecutorService executor = Executors.newFixedThreadPool(Integer.parseInt(mBinding.editTextThreads.getText().toString()));

        Callable<Double> callable1 = new MyCallable(Integer.parseInt(mBinding.editTextOperations.getText().toString()), 0);
        Callable<Double> callable2 = new MyCallable(Integer.parseInt(mBinding.editTextOperations.getText().toString()), 1);
        Callable<Double> callable3 = new MyCallable(Integer.parseInt(mBinding.editTextOperations.getText().toString()), 2);

        Future<Double> future1 = executor.submit(callable1);
        Future<Double> future2 = executor.submit(callable2);
        Future<Double> future3 = executor.submit(callable3);

        while (true) {
            try {
                if (!future1.isDone()) {
                    mCollectionsList.set(1, new OperationItem("Adding to HashMap: ", future1.get().toString() + " ms", false));
                    mAdapter.notifyItemChanged(1);
                }
                if (!future2.isDone()) {
                    mCollectionsList.set(3, new OperationItem("Search in HashMap: ", future2.get().toString() + " ms", false));
                    mAdapter.notifyItemChanged(3);
                }
                if (!future3.isDone()) {
                    mCollectionsList.set(5, new OperationItem("Removing from HashMap: ", future3.get().toString() + " ms", false));
                    mAdapter.notifyItemChanged(5);
                }

                if (future1.isDone() && future2.isDone() && future3.isDone()) {
                    break;
                }


            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

    }

    public class MyCallable implements Callable<Double> {

        private final Integer mOperations;
        private final Integer mNumberOfOperation;

        public MyCallable(int operations, int numOfOperation) {
            mOperations = operations;
            mNumberOfOperation = numOfOperation;
        }

        @Override
        public Double call() throws Exception {
            long startTime = 0, endTime = 0;

            HashMap<Integer, Integer> hashMap = new HashMap<Integer, Integer>();
            for (int i = 0; i < mOperations; i++) {
                hashMap.put(i, 150);
            }
            switch (mNumberOfOperation) {
                case 0:
                    startTime = System.nanoTime();
                    hashMap.put(1, 125);
                    endTime = System.nanoTime();
                    break;
                case 1:
                    startTime = System.nanoTime();
                    //cause i didnt know that item needed to be search for i decided to search for an item in the middle of hashMap
                    boolean contain = hashMap.containsKey(mOperations / 2);
                    endTime = System.nanoTime();
                    break;
                case 2:
                    startTime = System.nanoTime();
                    hashMap.remove(mOperations / 2);
                    endTime = System.nanoTime();
                    break;
                default:
                    return null;

            }
            return (Double) (double) (endTime - startTime) / 1000000;
        }
    }


}

