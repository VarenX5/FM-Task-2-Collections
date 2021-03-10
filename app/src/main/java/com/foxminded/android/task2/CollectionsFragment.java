package com.foxminded.android.task2;

import android.content.res.Resources;
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
        mCollectionsList = new ArrayList<OperationItem>();
        createOperationsList();
        mAdapter = new CollectionsRecyclerAdapter(mCollectionsList);

        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mRecyclerView.setAdapter(mAdapter);

        mBinding.startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCollectionsList.set(0, new OperationItem("Adding to start in ArrayList: ", "1 ms", true));
                mAdapter.notifyItemChanged(0);
            }
        });
        return mBinding.getRoot();
    }

    private void createOperationsList() {
        Resources res = getResources();
        String[] operations = res.getStringArray(R.array.name_of_collections_operations);
        String ms = getString(R.string.n_a_ms);
        mCollectionsList.add(new OperationItem(operations[0], ms, false));
        mCollectionsList.add(new OperationItem(operations[1], ms, false));
        mCollectionsList.add(new OperationItem(operations[2], ms, false));

        mCollectionsList.add(new OperationItem(operations[3], ms, false));
        mCollectionsList.add(new OperationItem(operations[4], ms, false));
        mCollectionsList.add(new OperationItem(operations[5], ms, false));

        mCollectionsList.add(new OperationItem(operations[6], ms, false));
        mCollectionsList.add(new OperationItem(operations[7], ms, false));
        mCollectionsList.add(new OperationItem(operations[8], ms, false));

        mCollectionsList.add(new OperationItem(operations[9], ms, false));
        mCollectionsList.add(new OperationItem(operations[10], ms, false));
        mCollectionsList.add(new OperationItem(operations[11], ms, false));

        mCollectionsList.add(new OperationItem(operations[12], ms, false));
        mCollectionsList.add(new OperationItem(operations[13], ms, false));
        mCollectionsList.add(new OperationItem(operations[14], ms, false));

        mCollectionsList.add(new OperationItem(operations[15], ms, false));
        mCollectionsList.add(new OperationItem(operations[16], ms, false));
        mCollectionsList.add(new OperationItem(operations[17], ms, false));

        mCollectionsList.add(new OperationItem(operations[18], ms, false));
        mCollectionsList.add(new OperationItem(operations[19], ms, false));
        mCollectionsList.add(new OperationItem(operations[20], ms, false));

    }


    public static CollectionsFragment newInstance() {
        return new CollectionsFragment();
    }


}
