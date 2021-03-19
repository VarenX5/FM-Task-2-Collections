package com.foxminded.android.task2.model;

import android.content.Context;
import android.content.res.Resources;

import com.foxminded.android.task2.R;
import com.foxminded.android.task2.dto.OperationItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MapsOperations implements Operations {

    private final Context ctx;

    public MapsOperations(Context context) {
        ctx = context;
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public List<OperationItem> getOperations() {
        final Resources res = ctx.getResources();
        final String[] operations = res.getStringArray(R.array.name_of_map_operations);
        final List<OperationItem> items = new ArrayList<>();
        final String ms = ctx.getString(R.string.n_a_ms);
        for (int i = 0; i < 6; i++) {
            items.add(new OperationItem(operations[i], ms, false, i));
        }
        return items;
    }

    @Override
    public double measureTime(int amountOfElements, OperationItem operation) {
        long startTime = 0, endTime = 0;
        Map<Integer, Integer> operationsMap;

        int numOfOperation = operation.getNumber();
        if ((numOfOperation == 0) | (numOfOperation == 2) | (numOfOperation == 4)) {
            operationsMap = new TreeMap<>();
        } else {
            operationsMap = new HashMap<>();
        }
        for (int i = 0; i < amountOfElements; i++) {
            operationsMap.put(i, 150);
        }
        switch (numOfOperation) {
            case 0:
            case 1:
                startTime = System.nanoTime();
                operationsMap.put(0, 125);
                endTime = System.nanoTime();
                break;
            case 2:
            case 3:
                startTime = System.nanoTime();
                boolean contain = operationsMap.containsKey(amountOfElements / 2);
                endTime = System.nanoTime();
                break;
            case 4:
            case 5:
                startTime = System.nanoTime();
                operationsMap.remove(amountOfElements / 2);
                endTime = System.nanoTime();
                break;
            default:
                throw new RuntimeException("We got a runtime exception");
        }

        return (endTime - startTime) / 1000000.0;
    }
}
