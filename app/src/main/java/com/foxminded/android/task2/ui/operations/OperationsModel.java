package com.foxminded.android.task2.ui.operations;

import com.foxminded.android.task2.dto.OperationItem;

import java.util.ArrayList;
import java.util.List;


import io.reactivex.rxjava3.subjects.PublishSubject;

public class OperationsModel implements OperationsContracter.Model  {

    private List<OperationItem> operationsData = new ArrayList<>();
    private final PublishSubject<List<OperationItem>> operationsDataObservable = PublishSubject.create();
    public OperationsModel(){

    }
    @Override
    public List<OperationItem> getOperationsData() {
        return operationsData;
    }
    @Override
    public PublishSubject<List<OperationItem>> getOperationsDataObservable() {
        return operationsDataObservable;
    }
    @Override
    public void setOperationsData(List<OperationItem> operationsData) {
        this.operationsData = operationsData;
        operationsDataObservable.onNext(this.operationsData);
    }
}

