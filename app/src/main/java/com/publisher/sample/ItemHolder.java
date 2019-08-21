package com.publisher.sample;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class ItemHolder extends RecyclerView.ViewHolder {
    private final TextView textView;

    public ItemHolder(@NonNull View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.tv_id);
    }

    void bind(String text) {
        textView.setText(text);
    }
}