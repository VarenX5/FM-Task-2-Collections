package com.foxminded.android.task2.ui.operations;

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

import com.foxminded.android.task2.R;
import com.foxminded.android.task2.databinding.FragmentCollectionsBinding;
import com.foxminded.android.task2.dto.OperationItem;

import java.util.ArrayList;

public class CollectionsFragment extends Fragment {

    static final String ARGUMENT_OBJ = "object";

    private RecyclerView mRecyclerView;
    private final CollectionsRecyclerAdapter mAdapter = new CollectionsRecyclerAdapter();
    private FragmentCollectionsBinding mBinding;
    private ArrayList<OperationItem> mCollectionsList;

    public static CollectionsFragment newInstance() {
        return new CollectionsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentCollectionsBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mRecyclerView = mBinding.myRecyclerView;
        mRecyclerView.setHasFixedSize(true);
        mCollectionsList = new ArrayList<>();
        createOperationsList();
        mAdapter.setItems(mCollectionsList);

        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mRecyclerView.setAdapter(mAdapter);

        mBinding.startButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mCollectionsList.set(0, new OperationItem("Adding to start in ArrayList: ", "1 ms", true));
            mAdapter.notifyItemChanged(0);
        });
    }

    private void createOperationsList() {
        Resources res = getResources();
        String[] operations = res.getStringArray(R.array.name_of_collections_operations);
        String ms = getString(R.string.n_a_ms);
        for (int i=0;i<21;i++){
            mCollectionsList.add(new OperationItem(operations[i], ms, false));
        }
    }
}
