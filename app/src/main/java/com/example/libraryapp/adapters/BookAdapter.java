package com.example.libraryapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libraryapp.R;
import com.example.libraryapp.holders.BookViewHolder;
import com.example.libraryapp.models.Book;
import com.example.libraryapp.models.Category;
import com.example.libraryapp.screens.BookDetails;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookViewHolder> {
    private ArrayList<Book> books;
    private int categorySelection;
    Context context;

    public BookAdapter(ArrayList<Book> books, int categorySelection) {
        this.books = books;
        this.categorySelection = categorySelection;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_book_item, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
           Book book = books.get(position);
           int bookId = book.getId();
           String bookName = book.getBookName();
           String bookAuthor = book.getAuthorName();
           String date = book.getDate();
           String pagesNumber = book.getPagesNumber() + "";
           byte[] imgByteArray = book.getImg();
           int categoryId = book.getCategory_id();
           Bitmap bookImageBitMap = BitmapFactory.decodeByteArray(imgByteArray, 0, imgByteArray.length);


        holder.tvBookName.setText(bookName);
        holder.tvAuthorName.setText(bookAuthor);
        holder.tvDate.setText(date);
        holder.bookImage.setImageBitmap(bookImageBitMap);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BookDetails.class);
                intent.putExtra("bookId", bookId);
                intent.putExtra("bookName", bookName);
                intent.putExtra("bookAuthor", bookAuthor);
                intent.putExtra("date", date);
                intent.putExtra("pagesNumber", pagesNumber);
                intent.putExtra("imgByteArray", imgByteArray);
                intent.putExtra("categoryId", categoryId);
                intent.putExtra("categorySelection",categorySelection);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return books.size();
    }


}
