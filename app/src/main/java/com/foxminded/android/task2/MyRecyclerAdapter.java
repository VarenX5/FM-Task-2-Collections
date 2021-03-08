package com.foxminded.android.task2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ExampleViewHolder> {
    private ArrayList<OperationItem> mOperationsList;
    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public ProgressBar mProgressBar;

        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.itemText);
            mProgressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    public MyRecyclerAdapter(ArrayList<OperationItem> operationsList){
        mOperationsList = operationsList;
    }
    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
        return new ExampleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        OperationItem currentOperation = mOperationsList.get(position);
        String textOfOperation;
        textOfOperation = currentOperation.getName()+currentOperation.getTime();
        holder.mTextView.setText(textOfOperation);
        if (currentOperation.isOperationOn()) {
            holder.mProgressBar.setVisibility(ProgressBar.VISIBLE);
        } else {
            holder.mProgressBar.setVisibility(ProgressBar.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mOperationsList.size();
    }
}
