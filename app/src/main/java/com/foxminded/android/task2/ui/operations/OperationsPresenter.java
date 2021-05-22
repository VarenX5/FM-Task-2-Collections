package com.foxminded.android.task2.ui.operations;

import android.util.Log;


import com.foxminded.android.task2.BaseApplication;
import com.foxminded.android.task2.R;
import com.foxminded.android.task2.dto.OperationItem;
import com.foxminded.android.task2.model.CollectionsOperations;
import com.foxminded.android.task2.model.MapsOperations;
import com.foxminded.android.task2.model.Operations;


import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class OperationsPresenter implements OperationsContracter.PresenterView {
    private final OperationsContracter.FragmentViewInterface mView;
    private Operations mOperations;
    private final String mFragmentName;
    private OperationsModel mModel;
    private ExecutorService mExecutorService;
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public OperationsPresenter(OperationsContracter.FragmentViewInterface view, String fragmentName) {
        mView = view;
        mFragmentName = fragmentName;
    }

    @Override
    public void setStartItems() {
        mModel = new OperationsModel();
        if (mFragmentName.equals(OperationsFragment.MAPS_FRAGMENT)) {
            mOperations = new MapsOperations(BaseApplication.getInstance());
        } else {
            mOperations = new CollectionsOperations(BaseApplication.getInstance());
        }
        mCompositeDisposable.add(mModel.getOperationsDataObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setRecyclerViewItems));
        mModel.setOperationsData(mOperations.getOperations());
    }

    @Override
    public void setRecyclerViewItems(List<OperationItem> operationsData) {
        mView.setItems(operationsData);
    }

    @Override
    public void validateAndStart(String amountOfThreadsString, String amountOfElementsString, boolean isChecked) {
        Log.d("wtf", "validateAndStart started from Presenter");
        if (isChecked) {
            if (mExecutorService != null) {
                return;
            }
            if (amountOfElementsString.equals("0") || amountOfElementsString.isEmpty()) {
                mView.showToastText(R.string.message_need_more_than_zero_operations);
                mView.setButtonChecked(false);
            } else if (amountOfThreadsString.equals("0") || amountOfThreadsString.isEmpty()) {
                mView.showToastText(R.string.message_need_more_than_zero_threads);
                mView.setButtonChecked(false);
            } else {
                startOfExecution(Integer.parseInt(amountOfThreadsString), Integer.parseInt(amountOfElementsString));
            }
        } else {
            forceShutdownExecution(true);
        }
    }

    private void startOfExecution(int amountOfThreads, int amountOfElements) {
        Log.d("wtf", "startOfExecutionByOneItem started from Presenter");
        setProgressVisibility(true);
        mExecutorService = Executors.newFixedThreadPool(amountOfThreads);
        Scheduler scheduler = Schedulers.from(mExecutorService);
        List<OperationItem> operationsData = mModel.getOperationsData();
        mCompositeDisposable.add(Observable.just(operationsData)
                .flatMapIterable(s -> s)
                .map(new Function<OperationItem, OperationItem>() {
                    @Override
                    public OperationItem apply(OperationItem operationItem) throws Throwable {
                        operationItem.setTime(Double.toString(mOperations.measureTime(amountOfElements, operationItem)));
                        operationItem.setOperationOn(false);
                        return operationItem;
                    }
                })
                .subscribeOn(scheduler)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mView::setItem, Throwable::printStackTrace, () -> forceShutdownExecution(false)));
    }

    private void forceShutdownExecution(boolean isForceShutdown) {
        Log.d("wtf", "forceShutdownExecution started from Presenter");
        if (mExecutorService == null) {
            return;
        }
        Log.d("wtf", "forceShutdownExecution is not null");
        mExecutorService.shutdownNow();
        mExecutorService = null;
        mView.setButtonChecked(false);
        if (isForceShutdown) {
            mView.showToastText(R.string.execution_shutdown);
            setProgressVisibility(false);
        } else {
            mView.showToastText(R.string.execution_done);
        }
    }

    private void setProgressVisibility(boolean isVisible) {
        List<OperationItem> operationsData = mModel.getOperationsData();
        for (OperationItem item : operationsData) {
            item.setOperationOn(isVisible);
        }
        mModel.setOperationsData(operationsData);
    }

    public int getColumnCount() {
        return mOperations.getColumnCount();
    }

    @Override
    public void onDestroy() {
        mCompositeDisposable.dispose();
    }
}
