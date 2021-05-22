package com.foxminded.android.task2.ui.operations;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.foxminded.android.task2.databinding.FragmentCollectionsBinding;
import com.foxminded.android.task2.dto.OperationItem;

import java.util.List;
import java.util.Objects;

public class OperationsFragment extends Fragment implements OperationsContracter.FragmentViewInterface{
    public static final String MAPS_FRAGMENT = "MapsFragment";
    private static final String FRAGMENT_NAME = "FragmentName";
    private OperationsPresenter mOperationsPresenter;
    private FragmentCollectionsBinding mBinding;
    private final OperationsAdapter mAdapter = new OperationsAdapter();


    public static OperationsFragment newInstance(String argument) {
        OperationsFragment operationsFragment = new OperationsFragment();
        Bundle args = new Bundle();
        args.putString(FRAGMENT_NAME, argument);
        operationsFragment.setArguments(args);
        return operationsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOperationsPresenter = new OperationsPresenter(this, getArguments().getString(FRAGMENT_NAME));
        mOperationsPresenter.setStartItems();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentCollectionsBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = mBinding.myRecyclerView;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), mOperationsPresenter.getColumnCount()));
        recyclerView.setAdapter(mAdapter);
        mBinding.startButton.setOnCheckedChangeListener((buttonView, isChecked) -> mOperationsPresenter.validateAndStart(getTextFromInput(mBinding.editTextThreads), getTextFromInput(mBinding.editTextOperations), isChecked));
    }

    private String getTextFromInput(EditText editText) {
        return editText.getText().toString().trim();
    }


    @Override
    public void setItems(List<OperationItem> items) {
        mAdapter.setItems(items);
    }

    @Override
    public void setItem( OperationItem item) {
        mAdapter.setItem(item.getNumber(), item);
    }

    @Override
    public void setButtonChecked(boolean isChecked) {
        mBinding.startButton.setChecked(isChecked);
    }

    @Override
    public void showToastText(int text) {
        Toast.makeText(getActivity(),getString(text),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mOperationsPresenter.onDestroy();
    }
}