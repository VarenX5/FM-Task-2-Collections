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
        int numberOfOperation = operation.getNumber();
        //int[] test = {0,3,6,9,12,15,18};
        //int[] test2 = {1,4,7,10,13,16,19};
        //int[] test3 = {2,5,8,11,14,17,20};
        if ((numberOfOperation == 0) | (numberOfOperation % 3 == 0)) {
            ArrayList<Integer> arrayList = new ArrayList<>();
            for (int i = 0; i < amountOfElements; i++) {
                arrayList.add(i);
            }
            switch (numberOfOperation) {
                case 0:
                    startTime = System.nanoTime();
                    arrayList.add(0, 150);
                    endTime = System.nanoTime();
                    break;
                case 3:
                    startTime = System.nanoTime();
                    arrayList.add(amountOfElements / 2, 150);
                    endTime = System.nanoTime();
                    break;
                case 6:
                    startTime = System.nanoTime();
                    arrayList.add(arrayList.size() - 1, 150);
                    endTime = System.nanoTime();
                    break;
                case 9:
                    startTime = System.nanoTime();
                    arrayList.indexOf(arrayList.get(amountOfElements / 2));
                    endTime = System.nanoTime();
                    break;
                case 12:
                    startTime = System.nanoTime();
                    arrayList.remove(0);
                    endTime = System.nanoTime();
                    break;
                case 15:
                    startTime = System.nanoTime();
                    arrayList.remove(amountOfElements / 2);
                    endTime = System.nanoTime();
                    break;
                case 18:
                    startTime = System.nanoTime();
                    arrayList.remove(arrayList.size() - 1);
                    endTime = System.nanoTime();
                    break;
                default:
                    throw new RuntimeException("We got a runtime exception");
            }

        } else if ((numberOfOperation == 1) | (numberOfOperation == 4) | (numberOfOperation == 7) | (numberOfOperation == 10) | (numberOfOperation == 13) | (numberOfOperation == 16) | (numberOfOperation == 19)) {
            LinkedList<Integer> linkedList = new LinkedList<>();
            for (int i = 0; i < amountOfElements; i++) {
                linkedList.add(i);
            }
            switch (numberOfOperation) {
                case 1:
                    startTime = System.nanoTime();
                    linkedList.addFirst( 150);
                    endTime = System.nanoTime();
                    break;
                case 4:
                    startTime = System.nanoTime();
                    linkedList.add(amountOfElements / 2, 150);
                    endTime = System.nanoTime();
                    break;
                case 7:
                    startTime = System.nanoTime();
                    linkedList.addLast( 150);
                    endTime = System.nanoTime();
                    break;
                case 10:
                    startTime = System.nanoTime();
                    linkedList.indexOf(linkedList.get(amountOfElements / 2));
                    endTime = System.nanoTime();
                    break;
                case 13:
                    startTime = System.nanoTime();
                    linkedList.remove(0);
                    endTime = System.nanoTime();
                    break;
                case 16:
                    startTime = System.nanoTime();
                    linkedList.remove(amountOfElements / 2);
                    endTime = System.nanoTime();
                    break;
                case 19:
                    startTime = System.nanoTime();
                    linkedList.removeLast();
                    endTime = System.nanoTime();
                    break;
                default:
                    throw new RuntimeException("We got a runtime exception");
            }

        } else {
            CopyOnWriteArrayList<Integer> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
            for (int i = 0; i < amountOfElements; i++) {
                copyOnWriteArrayList.add(i);
            }
            switch (numberOfOperation) {
                case 2:
                    startTime = System.nanoTime();
                    copyOnWriteArrayList.add(0, 150);
                    endTime = System.nanoTime();
                    break;
                case 5:
                    startTime = System.nanoTime();
                    copyOnWriteArrayList.add(amountOfElements / 2, 150);
                    endTime = System.nanoTime();
                    break;
                case 8:
                    startTime = System.nanoTime();
                    copyOnWriteArrayList.add(copyOnWriteArrayList.size() - 1, 150);
                    endTime = System.nanoTime();
                    break;
                case 11:
                    startTime = System.nanoTime();
                    copyOnWriteArrayList.indexOf(copyOnWriteArrayList.get(amountOfElements / 2));
                    endTime = System.nanoTime();
                    break;
                case 14:
                    startTime = System.nanoTime();
                    copyOnWriteArrayList.remove(0);
                    endTime = System.nanoTime();
                    break;
                case 17:
                    startTime = System.nanoTime();
                    copyOnWriteArrayList.remove(amountOfElements / 2);
                    endTime = System.nanoTime();
                    break;
                case 20:
                    startTime = System.nanoTime();
                    copyOnWriteArrayList.remove(copyOnWriteArrayList.size() - 1);
                    endTime = System.nanoTime();
                    break;
                default:
                    throw new RuntimeException("We got a runtime exception");
            }


        }
        return (double) (endTime - startTime) / 1000000;
    }
}
