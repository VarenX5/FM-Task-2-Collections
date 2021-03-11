package com.foxminded.android.task2;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.foxminded.android.task2.databinding.FragmentCollectionsBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MapsFragment extends Fragment {

    private FragmentCollectionsBinding mBinding;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<OperationItem> mCollectionsList;
    private ExecutorService mExecutorService;

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
        mCollectionsList = new ArrayList<OperationItem>();
        createOperationsList();
        mAdapter = new CollectionsRecyclerAdapter(mCollectionsList);

        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mRecyclerView.setAdapter(mAdapter);

        mBinding.startButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    String numOfOperation = mBinding.editTextOperations.getText().toString();
                    String numOfThreads = mBinding.editTextThreads.getText().toString();

                    if ((numOfOperation.equals("0")) | (numOfOperation.isEmpty())) {
                        Toast.makeText(getActivity(), getString(R.string.message_need_more_than_zero_operations), Toast.LENGTH_LONG).show();
                        mBinding.editTextOperations.setText("");
                    } else if ((numOfThreads.equals("0")) | (numOfThreads.isEmpty())) {
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

            }
        });
    }


    public static MapsFragment newInstance() {
        return new MapsFragment();
    }

    private void createOperationsList() {
        Resources res = getResources();
        String[] operations = res.getStringArray(R.array.name_of_map_operations);
        String ms = getString(R.string.n_a_ms);
        for (int i = 0; i < 6; i++) {
            mCollectionsList.add(new OperationItem(operations[i], ms, false));
        }
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
        ArrayList<Future<Double>> futureArrayList = new ArrayList<>();
        ArrayList<Callable<Double>> callableArrayList = new ArrayList<>();
        int amountOfElements = Integer.parseInt(mBinding.editTextOperations.getText().toString());

        Resources res = getResources();
        String[] nameOfOperations = res.getStringArray(R.array.name_of_map_operations);
        //i will change it to for loop later
        Callable<Double> callable0 = new TreeMapCallable(amountOfElements, 0);//
        Callable<Double> callable1 = new HashMapCallable(amountOfElements, 1);
        Callable<Double> callable2 = new TreeMapCallable(amountOfElements, 2);//
        Callable<Double> callable3 = new HashMapCallable(amountOfElements, 3);
        Callable<Double> callable4 = new TreeMapCallable(amountOfElements, 4);//
        Callable<Double> callable5 = new HashMapCallable(amountOfElements, 5);

        callableArrayList.add(callable0);//
        callableArrayList.add(callable1);
        callableArrayList.add(callable2);//
        callableArrayList.add(callable3);
        callableArrayList.add(callable4);//
        callableArrayList.add(callable5);
        int index = 0;
        for (Callable<Double> cal : callableArrayList) {

            Future<Double> future = mExecutorService.submit(cal);
//            try {
//                mCollectionsList.set(index, new OperationItem(nameOfOperations[index], future.get().toString() + " ms", false));
//            } catch (InterruptedException | ExecutionException e) {
//                e.printStackTrace();
//            }
            mAdapter.notifyItemChanged(index);
            futureArrayList.add(future);
            index++;

            if (futureArrayList.size() == callableArrayList.size()) {
                boolean check = true;
                for (Future<Double> futureArray : futureArrayList) {
                    if (!futureArray.isDone()) {
                        check = false;
                    }
                }
//                if (check) {
//                    for (int i = 0; i < futureArrayList.size(); i++) {
//                        try {
//                            mCollectionsList.set(i, new OperationItem(nameOfOperations[i], futureArrayList.get(i).get().toString() + " ms", false));
//                        } catch (InterruptedException | ExecutionException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    mBinding.startButton.setChecked(false);
//                }
            }
        }
    }

    public void shutdownExecution() {
    }

//        for (int i = 0; i < callableArrayList.size(); i++) {
//            Future<Double> future = executor.submit(callableArrayList.get(i));
//            try {
//                mCollectionsList.set(i, new OperationItem(nameOfOperations[i], future.get().toString() + " ms", false));
//            } catch (InterruptedException | ExecutionException e) {
//                e.printStackTrace();
//            }
//            mAdapter.notifyItemChanged(i);
//            futureArrayList.add(future);
//        }
//
//        if (futureArrayList.size() == callableArrayList.size()) {
//            boolean check = true;
//            for (Future<Double> future : futureArrayList) {
//                if (!future.isDone()) {
//                    check = false;
//                }
//            }
//            if (check) {
//                for (int i = 0; i < futureArrayList.size(); i++) {
//                    try {
//                        mCollectionsList.set(i, new OperationItem(nameOfOperations[i], futureArrayList.get(i).get().toString() + " ms", false));
//                    } catch (InterruptedException | ExecutionException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }


    //And i think it might be better to move it to standalone class
    public class HashMapCallable implements Callable<Double> {

        private final Integer mOperations;
        private final Integer mNumberOfOperation;

        public HashMapCallable(int operations, int numOfOperation) {
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
                case 1:
                    startTime = System.nanoTime();
                    hashMap.put(1, 125);
                    endTime = System.nanoTime();
                    break;
                case 3:
                    startTime = System.nanoTime();
                    //cause i didnt know that item needed to be search for i decided to search for an item in the middle of hashMap
                    boolean contain = hashMap.containsKey(mOperations / 2);
                    endTime = System.nanoTime();
                    break;
                case 5:
                    startTime = System.nanoTime();
                    hashMap.remove(mOperations / 2);
                    endTime = System.nanoTime();
                    break;
                default:
                    throw new RuntimeException("We got a runtime exception");

            }
            return (double) (endTime - startTime) / 1000000;
        }
    }

    public class TreeMapCallable implements Callable<Double> {

        private final Integer mOperations;
        private final Integer mNumberOfOperation;

        public TreeMapCallable(int operations, int numOfOperation) {
            mOperations = operations;
            mNumberOfOperation = numOfOperation;
        }

        @Override
        public Double call() throws Exception {
            long startTime = 0, endTime = 0;

            TreeMap<Integer, Integer> treeMap = new TreeMap<>();
            for (int i = 0; i < mOperations; i++) {
                treeMap.put(i, 150);
            }
            switch (mNumberOfOperation) {
                case 0:
                    startTime = System.nanoTime();
                    treeMap.put(1, 125);
                    endTime = System.nanoTime();
                    break;
                case 2:
                    startTime = System.nanoTime();
                    boolean contain = treeMap.containsKey(mOperations / 2);
                    endTime = System.nanoTime();
                    break;
                case 4:
                    startTime = System.nanoTime();
                    treeMap.remove(mOperations / 2);
                    endTime = System.nanoTime();
                    break;
                default:
                    throw new RuntimeException("We got a runtime exception");

            }
            return (double) (endTime - startTime) / 1000000;
        }
    }


    interface CalculationOfOperations {
        public void getColumnCount();

        public ArrayList<Integer> getOperations();

        public double measureTime(int numOfOperation);
    }

    public class CalculationForMaps implements CalculationOfOperations {
        int mOperations;

        public CalculationForMaps(int numberOfItemsInMap) {
            mOperations = numberOfItemsInMap;
        }

        @Override
        public void getColumnCount() {
            //tbh no idea what it can be used for
        }

        @Override
        public ArrayList<Integer> getOperations() {
            ArrayList<Integer> arrayList = new ArrayList<>();
            for (int i = 0; i < 6; i++) {
                arrayList.add(i);
            }
            return arrayList;
        }

        @Override
        public double measureTime(int numOfOperation) {

            long startTime = 0, endTime = 0;
            if ((numOfOperation == 0) | (numOfOperation == 2) | (numOfOperation == 4)) {

                TreeMap<Integer, Integer> treeMap = new TreeMap<>();
                for (int i = 0; i < mOperations; i++) {
                    treeMap.put(i, 150);
                }
                switch (numOfOperation) {
                    case 0:
                        startTime = System.nanoTime();
                        treeMap.put(1, 125);
                        endTime = System.nanoTime();
                        break;
                    case 2:
                        startTime = System.nanoTime();
                        boolean contain = treeMap.containsKey(mOperations / 2);
                        endTime = System.nanoTime();
                        break;
                    case 4:
                        startTime = System.nanoTime();
                        treeMap.remove(mOperations / 2);
                        endTime = System.nanoTime();
                        break;
                    default:
                        throw new RuntimeException("We got a runtime exception");
                }
            } else {
                HashMap<Integer, Integer> hashMap = new HashMap<Integer, Integer>();
                for (int i = 0; i < mOperations; i++) {
                    hashMap.put(i, 150);
                }
                switch (numOfOperation) {
                    case 1:
                        startTime = System.nanoTime();
                        hashMap.put(1, 125);
                        endTime = System.nanoTime();
                        break;
                    case 3:
                        startTime = System.nanoTime();
                        //cause i didnt know that item needed to be search for i decided to search for an item in the middle of hashMap
                        boolean contain = hashMap.containsKey(mOperations / 2);
                        endTime = System.nanoTime();
                        break;
                    case 5:
                        startTime = System.nanoTime();
                        hashMap.remove(mOperations / 2);
                        endTime = System.nanoTime();
                        break;
                    default:
                        throw new RuntimeException("We got a runtime exception");

                }
            }
            return (double) (endTime - startTime) / 1000000;

        }

    }

}