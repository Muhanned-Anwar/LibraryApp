package com.example.libraryapp.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.libraryapp.R;
import com.example.libraryapp.UpdateBook;
import com.example.libraryapp.controllers.DatabaseControllers;

import java.io.Serializable;

public class BookDetails extends AppCompatActivity {
    DatabaseControllers databaseControllers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        databaseControllers = new DatabaseControllers(getApplicationContext());
        Intent intent = getIntent();
        int bookId = (int) intent.getSerializableExtra("bookId");
        String bookName = (String) intent.getSerializableExtra("bookName");
        String bookAuthor = (String) intent.getSerializableExtra("bookAuthor");
        String date = (String) intent.getSerializableExtra("date");
        String pagesNumber = (String) intent.getSerializableExtra("pagesNumber");
        byte[] imgByteArray = (byte[]) intent.getSerializableExtra("imgByteArray");
        int categoryId = (int) intent.getSerializableExtra("categoryId");
        int categorySelection = (int) intent.getSerializableExtra("categorySelection");

        Bitmap bookImageBitMap = BitmapFactory.decodeByteArray(imgByteArray, 0, imgByteArray.length);

        TextView tvBookName = findViewById(R.id.tvBookNameItemDet);
        TextView tvBookAuthor = findViewById(R.id.tvAuthorNameItemDet);
        TextView tvDate = findViewById(R.id.tvDateDet);
        TextView tvPagesNumber = findViewById(R.id.tvPagesNumberDet);
        ImageView img = findViewById(R.id.bookImageItemDet);
        TextView tvAppBar = findViewById(R.id.tvAppBarDet);
        TextView tvCategoryId = findViewById(R.id.tvCategoryId);
        tvBookName.setText(bookName);
        tvAppBar.setText(bookName);
        tvBookAuthor.setText(bookAuthor);
        tvDate.setText(date);
        tvPagesNumber.setText(pagesNumber);
        img.setImageBitmap(bookImageBitMap);
        tvCategoryId.setText(categoryId + "");

        Button btnEdit = findViewById(R.id.btnEditBook);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(getApplicationContext(), UpdateBook.class);
                intent2.putExtra("bookId",bookId);
                intent2.putExtra("bookName", bookName);
                intent2.putExtra("bookAuthor", bookAuthor);
                intent2.putExtra("date", date);
                intent2.putExtra("pagesNumber", pagesNumber);
                intent2.putExtra("imgByteArray", imgByteArray);
                intent2.putExtra("categoryId", categoryId);
                intent2.putExtra("categorySelection", categorySelection);
                startActivity(intent2);
            }
        });

        ImageButton btnDeleteBook = findViewById(R.id.btnDeleteBook);
        btnDeleteBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean result = databaseControllers.deleteBook(bookId + "");
                if (result) {
                    Toast.makeText(getApplicationContext(), "Deleted Successfully (:", Toast.LENGTH_SHORT).show();
                    Intent n = new Intent(getApplicationContext(), Library.class);
                    startActivity(n);
                } else {
                    Toast.makeText(getApplicationContext(), "Deleted Failed ):", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}