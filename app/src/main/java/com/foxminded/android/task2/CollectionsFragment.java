package com.foxminded.android.task2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.foxminded.android.task2.databinding.FragmentCollectionsBinding;

import java.util.ArrayList;

public class CollectionsFragment extends Fragment {

    static final String ARGUMENT_OBJ = "object";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FragmentCollectionsBinding mBinding;
    private ArrayList<OperationItem> mCollectionsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentCollectionsBinding.inflate(inflater, container, false);

        mRecyclerView = mBinding.myRecyclerView;
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mCollectionsList = new ArrayList<OperationItem>();
        createOperationsList();
        mAdapter = new MyRecyclerAdapter(mCollectionsList);

        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mRecyclerView.setAdapter(mAdapter);

        mBinding.startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCollectionsList.set(0,new OperationItem("Adding to start in ArrayList: ","1 ms", true));
                mAdapter.notifyItemChanged(0);
            }
        });
        return mBinding.getRoot();
    }

    private void createOperationsList() {
        mCollectionsList.add(new OperationItem("Adding to start in ArrayList: ", "N/A ms", false));
        mCollectionsList.add(new OperationItem("Adding to start in LinkedList: ", "N/A ms", false));
        mCollectionsList.add(new OperationItem("Adding to start in CopeOnWriteArrayList: ", "N/A ms", false));

        mCollectionsList.add(new OperationItem("Adding to middle in ArrayList: ", "N/A ms", false));
        mCollectionsList.add(new OperationItem("Adding to middle in LinkedList: ", "N/A ms", false));
        mCollectionsList.add(new OperationItem("Adding to middle in CopeOnWriteArrayList: ", "N/A ms", false));

        mCollectionsList.add(new OperationItem("Adding to end in ArrayList: ", "N/A ms", false));
        mCollectionsList.add(new OperationItem("Adding to end in LinkedList: ", "N/A ms", false));
        mCollectionsList.add(new OperationItem("Adding to end in CopeOnWriteArrayList: ", "N/A ms", false));

        mCollectionsList.add(new OperationItem("Search in ArrayList: ", "N/A ms", false));
        mCollectionsList.add(new OperationItem("Search in LinkedList: ", "N/A ms", false));
        mCollectionsList.add(new OperationItem("Search in CopeOnWriteArrayList: ", "N/A ms", false));

        mCollectionsList.add(new OperationItem("Removing from start in ArrayList: ", "N/A ms", false));
        mCollectionsList.add(new OperationItem("Removing from start in LinkedList: ", "N/A ms", false));
        mCollectionsList.add(new OperationItem("Removing from start in CopeOnWriteArrayList: ", "N/A ms", false));

        mCollectionsList.add(new OperationItem("Removing from middle in ArrayList: ", "N/A ms", false));
        mCollectionsList.add(new OperationItem("Removing from middle in LinkedList: ", "N/A ms", false));
        mCollectionsList.add(new OperationItem("Removing from middle in CopeOnWriteArrayList: ", "N/A ms", false));

        mCollectionsList.add(new OperationItem("Removing from end in ArrayList: ", "N/A ms", false));
        mCollectionsList.add(new OperationItem("Removing from end in LinkedList: ", "N/A ms", false));
        mCollectionsList.add(new OperationItem("Removing from end in CopeOnWriteArrayList: ", "N/A ms", false));

    }


    public static CollectionsFragment newInstance() {
        return new CollectionsFragment();
    }


}
