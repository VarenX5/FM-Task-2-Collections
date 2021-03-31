package com.foxminded.android.task2.model;

import android.app.Application;
import android.content.res.Resources;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.foxminded.android.task2.R;
import com.foxminded.android.task2.dto.OperationItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class FragmentViewModel extends AndroidViewModel {
    private List<OperationItem> mOperationsList = new ArrayList<>();
    private MutableLiveData<List<OperationItem>> operationsLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isExecutionOn = new MutableLiveData<>();
    private MutableLiveData<String> toastText = new MutableLiveData<>();
    private final Operations mOperations;
    private ExecutorService mExecutorService;
    private final Application mApplication;

    public FragmentViewModel(@NonNull Application application, Operations operations) {
        super(application);
        mApplication = application;
        mOperations = operations;
        init();
    }

    public void init() {
        mOperationsList = mOperations.getOperations();
        operationsLiveData.setValue(mOperationsList);
    }

    public MutableLiveData<List<OperationItem>> getCollectionsLiveData() {
        return operationsLiveData;
    }

    public MutableLiveData<Boolean> getIsExecutionOnLiveData() {
        return isExecutionOn;
    }

    public MutableLiveData<String> getToastText() {
        return toastText;
    }

    public int getColumnCount() {
        return mOperations.getColumnCount();
    }


    public void startOfExecution(int amountOfThreads, int amountOfElements) {
        Log.d("wtf", "Start of startOfExecution() from ModelView");
        startOfOperationsList();
        Integer counterAmount;
        if (mOperations.getColumnCount() == 2) {
            counterAmount = 6;
        } else {
            counterAmount = 21;
        }
        mExecutorService = Executors.newFixedThreadPool(amountOfThreads);
        AtomicInteger counter = new AtomicInteger(0);
        for (OperationItem operation : mOperations.getOperations()) {
            Log.d("wtf", "Start of Cycle foreach");
            mExecutorService.submit(() -> {
                operation.setTime(Double.toString(mOperations.measureTime(amountOfElements, operation)));
                mOperationsList.set(operation.getNumber(), operation);
                operationsLiveData.postValue(mOperationsList);
                counter.getAndIncrement();
                Log.d("wtf", "Counter = " + counter.get());
                if (counter.get() == counterAmount) {
                    Log.d("wtf", "Counter == " + counterAmount);
                    isExecutionOn.postValue(false);
                    toastText.postValue(mApplication.getString(R.string.execution_done));
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

    private void startOfOperationsList() {
        Resources res = mApplication.getResources();
        String ms = mApplication.getString(R.string.n_a_ms);
        if (mOperations.getColumnCount() == 2) {
            String[] operations = res.getStringArray(R.array.name_of_map_operations);
            for (int i = 0; i < 6; i++) {
                mOperationsList.set(i, new OperationItem(operations[i], ms, true, i));
            }
        } else {
            String[] operations = res.getStringArray(R.array.name_of_collections_operations);
            for (int i = 0; i < 21; i++) {
                mOperationsList.set(i, new OperationItem(operations[i], ms, true, i));
            }
        }
        isExecutionOn.setValue(true);
        operationsLiveData.setValue(mOperationsList);
    }

    public void forceShutdownExecution() {
        toastText.setValue(mApplication.getString(R.string.execution_shutdown));
        mExecutorService.shutdownNow();
        for (OperationItem item : mOperationsList) {
            item.setOperationOn(false);
        }
        operationsLiveData.setValue(mOperationsList);
        isExecutionOn.setValue(false);
    }
}
