package com.foxminded.android.task2.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.foxminded.android.task2.R;
import com.foxminded.android.task2.dto.OperationItem;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
    public void validateAndStart(String amountOfThreadsString, String amountOfElementsString, Boolean isChecked) {
        if(isChecked){
            if(mExecutorService!=null){
                return;
            }
            if (amountOfElementsString.equals("0") || amountOfElementsString.isEmpty()) {
                toastText.setValue(mApplication.getString(R.string.message_need_more_than_zero_operations));
                isExecutionOn.setValue(false);
            } else if (amountOfThreadsString.equals("0") || amountOfThreadsString.isEmpty()) {
                toastText.setValue(mApplication.getString(R.string.message_need_more_than_zero_threads));
                isExecutionOn.setValue(false);
            } else {
                startOfExecution(Integer.parseInt(amountOfThreadsString), Integer.parseInt(amountOfElementsString));
            }
        } else {
            forceShutdownExecution(false);
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
                    toastText.postValue(mApplication.getString(R.string.execution_done));
                    forceShutdownExecution(true);
                }
            });

        }
    }

    private void setProgressVisibility(Boolean isVisible) {
        List<OperationItem> mOperationsList = operationsLiveData.getValue();
        for (OperationItem item : mOperationsList) {
            item.setOperationOn(isVisible);
        }
        operationsLiveData.setValue(mOperationsList);
    }

    public void forceShutdownExecution(Boolean isHidden) {
        if (mExecutorService==null) {
            return;
        }
        if(isHidden){
            mExecutorService.shutdownNow();
            mExecutorService = null;
            isExecutionOn.postValue(false);
        } else {
            toastText.setValue(mApplication.getString(R.string.execution_shutdown));
            mExecutorService.shutdownNow();
            mExecutorService = null;
            setProgressVisibility(false);
            isExecutionOn.setValue(false);
        }

    }
}
