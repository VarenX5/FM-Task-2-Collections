package com.foxminded.android.task2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CollectionsRecyclerAdapter extends RecyclerView.Adapter<CollectionsRecyclerAdapter.CollectionsViewHolder> {
    private final ArrayList<OperationItem> mOperationsList;
    public static class CollectionsViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public ProgressBar mProgressBar;

        public CollectionsViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.itemText);
            mProgressBar = itemView.findViewById(R.id.progressBar);
        }
        public void bindItem(OperationItem item){
            String textOfOperation = item.getName()+item.getTime();
            mTextView.setText(textOfOperation);
            if (item.isOperationOn()) {
                mProgressBar.setVisibility(ProgressBar.VISIBLE);
            } else {
                mProgressBar.setVisibility(ProgressBar.INVISIBLE);
            }
        }
    }

    public CollectionsRecyclerAdapter(ArrayList<OperationItem> operationsList){
        mOperationsList = operationsList;
    }
    @NonNull
    @Override
    public CollectionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
        return new CollectionsViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull CollectionsViewHolder holder, int position) {
        holder.bindItem(mOperationsList.get(position));
    }

    @Override
    public int getItemCount() {
        return mOperationsList.size();
    }
}
