package com.foxminded.android.task2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.foxminded.android.task2.databinding.FragmentCollectionsBinding;
import com.foxminded.android.task2.databinding.FragmentMapsBinding;

import java.util.ArrayList;

public class MapsFragment extends Fragment {

    private FragmentCollectionsBinding mBinding;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<OperationItem> mCollectionsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentCollectionsBinding.inflate(inflater,container,false);

        mRecyclerView = mBinding.myRecyclerView;
        mRecyclerView.setHasFixedSize(true);
        mCollectionsList = new ArrayList<OperationItem>();
        createOperationsList();
        mAdapter = new MyRecyclerAdapter(mCollectionsList);

        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        mRecyclerView.setAdapter(mAdapter);

        mBinding.startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCollectionsList.set(0, new OperationItem("Adding to TreeMap: ", "1 ms", true));
            }
        });
        return mBinding.getRoot();
    }

    public static MapsFragment newInstance() {
        return new MapsFragment();
    }

    private void createOperationsList() {
        mCollectionsList.add(new OperationItem("Adding to TreeMap: ", "N/A ms", false));
        mCollectionsList.add(new OperationItem("Adding to HashMap: ", "N/A ms", false));

        mCollectionsList.add(new OperationItem("Search in TreeMap: ", "N/A ms", false));
        mCollectionsList.add(new OperationItem("Search in HashMap: ", "N/A ms", false));

        mCollectionsList.add(new OperationItem("Removing from TreeMap: ", "N/A ms", false));
        mCollectionsList.add(new OperationItem("Removing from HashMap: ", "N/A ms", false));

    }
}

