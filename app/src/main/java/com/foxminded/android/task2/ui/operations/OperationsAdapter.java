package com.foxminded.android.task2.ui.operations;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.foxminded.android.task2.R;
import com.foxminded.android.task2.dto.OperationItem;

import java.util.ArrayList;
import java.util.List;

public class OperationsAdapter extends RecyclerView.Adapter<OperationsAdapter.CollectionsViewHolder> {
    private final List<OperationItem> items = new ArrayList<>();

    @NonNull
    @Override
    public CollectionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
        return new CollectionsViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull CollectionsViewHolder holder, int position) {
        holder.bindItem(items.get(position));
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<OperationItem> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void setItem(int index, OperationItem item) {
        if (index < items.size()) {
            items.set(index, item);
        }
        notifyItemChanged(index);
    }

    public List<OperationItem> getItems() {
        return items;
    }

    public static class CollectionsViewHolder extends RecyclerView.ViewHolder {
        public final TextView mTextView;
        public final ProgressBar mProgressBar;

        public CollectionsViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.itemText);
            mProgressBar = itemView.findViewById(R.id.progressBar);
        }

        public void bindItem(OperationItem item) {
            String itemText = item.getName() + item.getTime();
            mTextView.setText(itemText);
            if (item.isOperationOn()) {
                mProgressBar.animate()
                        .alpha(1.0f)
                        .setDuration(1500);
            } else {
                mProgressBar.animate()
                        .alpha(0.0f)
                        .setDuration(1500);
            }
        }
    }
}
