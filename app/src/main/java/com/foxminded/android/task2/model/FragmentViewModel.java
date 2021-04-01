package com.foxminded.android.task2.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.foxminded.android.task2.R;
import com.foxminded.android.task2.dto.OperationItem;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class FragmentViewModel extends ViewModel {
    private final MutableLiveData<List<OperationItem>> operationsLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isExecutionOn = new MutableLiveData<>();
    private MutableLiveData<Integer> toastText = new MutableLiveData<>();
    private final Operations mOperations;
    private ExecutorService mExecutorService;

    public FragmentViewModel(Operations operations) {
        super();
        mOperations = operations;
    }

    public void setOperations(){
        operationsLiveData.setValue(mOperations.getOperations());
    }
    public MutableLiveData<List<OperationItem>> getCollectionsLiveData() {
        return operationsLiveData;
    }

    public MutableLiveData<Boolean> getIsExecutionOnLiveData() {
        return isExecutionOn;
    }

    public MutableLiveData<Integer> getToastText() {
        return toastText;
    }

    public int getColumnCount() {
        return mOperations.getColumnCount();

    }
    public void validateAndStart(String amountOfThreadsString, String amountOfElementsString, boolean isChecked) {
        if(isChecked){
            if(mExecutorService!=null){
                return;
            }
            if (amountOfElementsString.equals("0") || amountOfElementsString.isEmpty()) {
                toastText.setValue(R.string.message_need_more_than_zero_operations);
                isExecutionOn.setValue(false);
            } else if (amountOfThreadsString.equals("0") || amountOfThreadsString.isEmpty()) {
                toastText.setValue(R.string.message_need_more_than_zero_threads);
                isExecutionOn.setValue(false);
            } else {
                startOfExecution(Integer.parseInt(amountOfThreadsString), Integer.parseInt(amountOfElementsString));
            }
        } else {
            forceShutdownExecution(true);
        }


    }

    public void startOfExecution(int amountOfThreads, int amountOfElements) {
        setProgressVisibility(true);
        int counterAmount = operationsLiveData.getValue().size();
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
                    toastText.postValue(R.string.execution_done);
                    forceShutdownExecution(false);
                }
            });

        }
    }

    private void setProgressVisibility(boolean isVisible) {
        List<OperationItem> mOperationsList = operationsLiveData.getValue();
        for (OperationItem item : mOperationsList) {
            item.setOperationOn(isVisible);
        }
        operationsLiveData.setValue(mOperationsList);
    }

    public void forceShutdownExecution(boolean forceStop) {
        if (mExecutorService==null) {
            return;
        }
        mExecutorService.shutdownNow();
        mExecutorService = null;
        isExecutionOn.postValue(false);

        if(forceStop) {
            toastText.setValue(R.string.execution_shutdown);
            setProgressVisibility(false);
        }

    }
}
