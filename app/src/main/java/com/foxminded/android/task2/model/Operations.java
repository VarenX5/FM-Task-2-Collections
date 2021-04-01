package com.foxminded.android.task2.model;

import com.foxminded.android.task2.dto.OperationItem;

import java.util.List;

public interface Operations {
    int getColumnCount();

    List<OperationItem> getOperations();

    double measureTime(int numOfOperation, OperationItem operation);
}
