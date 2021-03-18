package com.foxminded.android.task2.model;

import android.content.Context;
import android.content.res.Resources;

import com.foxminded.android.task2.R;
import com.foxminded.android.task2.dto.OperationItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        int numOfOperation= operation.getNumber();
        if ((numOfOperation == 0) | (numOfOperation == 2) | (numOfOperation == 4)) {

            TreeMap<Integer, Integer> treeMap = new TreeMap<>();
            for (int i = 0; i < amountOfElements; i++) {
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
                    boolean contain = treeMap.containsKey(amountOfElements / 2);
                    endTime = System.nanoTime();
                    break;
                case 4:
                    startTime = System.nanoTime();
                    treeMap.remove(amountOfElements / 2);
                    endTime = System.nanoTime();
                    break;
                default:
                    throw new RuntimeException("We got a runtime exception");
            }
        } else {
            HashMap<Integer, Integer> hashMap = new HashMap<Integer, Integer>();
            for (int i = 0; i < amountOfElements; i++) {
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
                    boolean contain = hashMap.containsKey(amountOfElements / 2);
                    endTime = System.nanoTime();
                    break;
                case 5:
                    startTime = System.nanoTime();
                    hashMap.remove(amountOfElements / 2);
                    endTime = System.nanoTime();
                    break;
                default:
                    throw new RuntimeException("We got a runtime exception");

            }
        }
        return (endTime - startTime) / 1000000.0;
    }
}
