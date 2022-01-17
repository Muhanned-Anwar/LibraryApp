package com.example.libraryapp.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libraryapp.R;

public class BookViewHolder extends RecyclerView.ViewHolder {
    public View view;
    public TextView tvBookName;
    public TextView tvAuthorName;
    public TextView tvDate;
//    public TextView tvPagesNumber;
    public ImageView bookImage;

    public BookViewHolder(@NonNull View itemView) {
        super(itemView);
        this.view = itemView;
        this.tvBookName = itemView.findViewById(R.id.tvBookNameItem);
        this.tvAuthorName = itemView.findViewById(R.id.tvAuthorNameItem);
        this.tvDate = itemView.findViewById(R.id.tvDate);
//        tvPagesNumber = itemView.findViewById(R.id.tvPagesNumber);
        this.bookImage = itemView.findViewById(R.id.bookImageItem);
    }
}
