package com.example.libraryapp.holders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libraryapp.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder {
    public View view;
    public TextView categoryName;

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
        categoryName = itemView.findViewById(R.id.tvCategoryName);

    }
}
