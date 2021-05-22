package com.foxminded.android.task2.ui.operations;

import com.foxminded.android.task2.dto.OperationItem;

import java.util.List;

import io.reactivex.rxjava3.subjects.PublishSubject;

public interface OperationsContracter {

    interface FragmentViewInterface {
        void setItems(List<OperationItem> items);

        void setItem(OperationItem operationItem);

        void setButtonChecked(boolean isChecked);

        void showToastText(int text);

    }

    interface PresenterView {
        void validateAndStart(String amountOfThreadsString, String amountOfElementsString, boolean isChecked);

        void setStartItems();

        void setRecyclerViewItems(List<OperationItem> operationsData);

        int getColumnCount();

        void onDestroy();
    }

    interface Model {
        void setOperationsData(List<OperationItem> operationsData);
        List<OperationItem> getOperationsData();
        PublishSubject<List<OperationItem>> getOperationsDataObservable();
    }
}
