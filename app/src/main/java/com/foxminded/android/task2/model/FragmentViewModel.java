package com.foxminded.android.task2.model;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.foxminded.android.task2.R;
import com.foxminded.android.task2.dto.OperationItem;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class FragmentViewModel extends AndroidViewModel {
    private final MutableLiveData<List<OperationItem>> operationsLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isExecutionOn = new MutableLiveData<>();
    private MutableLiveData<String> toastText = new MutableLiveData<>();
    private final Operations mOperations;
    private ExecutorService mExecutorService;
    private final Application mApplication;

    public FragmentViewModel(@NonNull Application application, Operations operations) {
        super(application);
        mApplication = application;
        mOperations = operations;
        operationsLiveData.setValue(mOperations.getOperations());
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

    public void validateAndStart(String amountOfThreadsString, String amountOfElementsString) {
        if (isExecutionOn.getValue() != null) {
            if (isExecutionOn.getValue()) {
                return;
            }
        }
        if (amountOfElementsString.equals("0") || amountOfElementsString.isEmpty()) {
            toastText.setValue(mApplication.getApplicationContext().getString(R.string.message_need_more_than_zero_operations));
            isExecutionOn.setValue(false);
        } else if (amountOfThreadsString.equals("0") || amountOfThreadsString.isEmpty()) {
            toastText.setValue(mApplication.getApplicationContext().getString(R.string.message_need_more_than_zero_threads));
            isExecutionOn.setValue(false);
        } else {
            startOfExecution(Integer.parseInt(amountOfThreadsString), Integer.parseInt(amountOfElementsString));
        }
    }

    public void startOfExecution(int amountOfThreads, int amountOfElements) {
        isExecutionOn.setValue(true);
        startOfOperationsList();
        int counterAmount;
        if (mOperations.getColumnCount() == 2) {
            counterAmount = 6;
        } else {
            counterAmount = 21;
        }
        mExecutorService = Executors.newFixedThreadPool(amountOfThreads);
        AtomicInteger counter = new AtomicInteger(0);
        List<OperationItem> mOperationsList = operationsLiveData.getValue();
        for (OperationItem operation : mOperationsList) {
            mExecutorService.submit(() -> {
                operation.setTime(Double.toString(mOperations.measureTime(amountOfElements, operation)));
                operation.setOperationOn(false);
                operationsLiveData.postValue(mOperationsList);
                counter.getAndIncrement();
                if (counter.get() == counterAmount) {
                    isExecutionOn.postValue(false);
                    toastText.postValue(mApplication.getString(R.string.execution_done));
                    mExecutorService.shutdown();
                    try {
                        if (!mExecutorService.awaitTermination(60, TimeUnit.MILLISECONDS)) {
                            mExecutorService.shutdownNow();
                            mExecutorService = null;
                        }
                    } catch (InterruptedException e) {
                        mExecutorService.shutdownNow();
                    }
                }
            });

        }
    }

    private void startOfOperationsList() {
        List<OperationItem> mOperationsList = operationsLiveData.getValue();
        for (OperationItem item : mOperationsList) {
            item.setOperationOn(true);
        }
        operationsLiveData.setValue(mOperationsList);
    }

    public void forceShutdownExecution() {
        if (!isExecutionOn.getValue()) {
            return;
        }
        List<OperationItem> mOperationsList = operationsLiveData.getValue();
        toastText.setValue(mApplication.getString(R.string.execution_shutdown));
        mExecutorService.shutdownNow();
        mExecutorService = null;
        for (OperationItem item : mOperationsList) {
            item.setOperationOn(false);
        }
        operationsLiveData.setValue(mOperationsList);
        isExecutionOn.setValue(false);
    }
}
