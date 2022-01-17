package com.example.libraryapp.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.libraryapp.MainActivity;
import com.example.libraryapp.R;
import com.example.libraryapp.adapters.BookAdapter;
import com.example.libraryapp.controllers.DatabaseControllers;
import com.example.libraryapp.models.Book;

import java.util.ArrayList;

public class CategoryBooks extends AppCompatActivity {
    DatabaseControllers databaseControllers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_books);

        Intent intent = getIntent();
        int categoryId = (int) intent.getSerializableExtra("categoryId");
        String categoryName = (String) intent.getSerializableExtra("categoryName");
        int categorySelection = (int) intent.getSerializableExtra("categorySelection");
        TextView tvAppBar = findViewById(R.id.tvAppBar);
        tvAppBar.setText(categoryName);
        appBar();
        databaseControllers = new DatabaseControllers(getApplicationContext());
        ArrayList<Book>  data = databaseControllers.getBooks(categoryId);

        if(!data.isEmpty()){
            BookAdapter adapter = new BookAdapter(data,categorySelection);
            RecyclerView recyclerView = findViewById(R.id.rvBooks);
            recyclerView.setAdapter(adapter);

            RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(lm);
        }

    }

    public void appBar() {
        ImageButton btnBackMain = findViewById(R.id.btnBack);
        btnBackMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent n = new Intent(getApplicationContext(), Library.class);
                startActivity(n);
            }
        });
    }

}