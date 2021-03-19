package com.foxminded.android.task2.model;

import android.content.Context;
import android.content.res.Resources;

import com.foxminded.android.task2.R;
import com.foxminded.android.task2.dto.OperationItem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CollectionsOperations implements Operations {

    private final Context ctx;

    public CollectionsOperations(Context context) {
        ctx = context;
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public List<OperationItem> getOperations() {
        final Resources res = ctx.getResources();
        final String[] operations = res.getStringArray(R.array.name_of_collections_operations);
        final List<OperationItem> items = new ArrayList<>();
        final String ms = ctx.getString(R.string.n_a_ms);
        for (int i = 0; i < 21; i++) {
            items.add(new OperationItem(operations[i], ms, false, i));
        }
        return items;
    }

    @Override
    public double measureTime(int amountOfElements, OperationItem operation) {
        long startTime = 0, endTime = 0;
        List<Integer> operationList;
        int numberOfOperation = operation.getNumber();

        if ((numberOfOperation == 0) | (numberOfOperation % 3 == 0)) {
            operationList = new ArrayList<>();
        } else if ((numberOfOperation == 1) | (numberOfOperation == 4) | (numberOfOperation == 7) | (numberOfOperation == 10) | (numberOfOperation == 13) | (numberOfOperation == 16) | (numberOfOperation == 19)) {
            operationList = new LinkedList<>();
        } else {
            operationList = new CopyOnWriteArrayList<>();
        }
        for (int i = 0; i < amountOfElements; i++) {
            operationList.add(i);
        }
        switch (numberOfOperation) {
            case 0:
            case 1:
            case 2:
                startTime = System.nanoTime();
                operationList.add(0, 150);
                endTime = System.nanoTime();
                break;
            case 3:
            case 4:
            case 5:
                startTime = System.nanoTime();
                operationList.add(amountOfElements / 2, 150);
                endTime = System.nanoTime();
                break;
            case 6:
            case 7:
            case 8:
                startTime = System.nanoTime();
                operationList.add(operationList.size() - 1, 150);
                endTime = System.nanoTime();
                break;
            case 9:
            case 10:
            case 11:
                startTime = System.nanoTime();
                operationList.indexOf(operationList.get(amountOfElements / 2));
                endTime = System.nanoTime();
                break;
            case 12:
            case 13:
            case 14:
                startTime = System.nanoTime();
                operationList.remove(0);
                endTime = System.nanoTime();
                break;
            case 15:
            case 16:
            case 17:
                startTime = System.nanoTime();
                operationList.remove(amountOfElements / 2);
                endTime = System.nanoTime();
                break;
            case 18:
            case 19:
            case 20:
                startTime = System.nanoTime();
                operationList.remove(operationList.size() - 1);
                endTime = System.nanoTime();
                break;
            default:
                throw new RuntimeException("We got a runtime exception");
        }
        return (endTime - startTime) / 1000000.0;
    }
}
