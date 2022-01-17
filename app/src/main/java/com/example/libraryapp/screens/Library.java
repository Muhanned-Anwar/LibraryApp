package com.example.libraryapp.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;

import com.example.libraryapp.MainActivity;
import com.example.libraryapp.R;
import com.example.libraryapp.adapters.CategoryAdapter;
import com.example.libraryapp.controllers.DatabaseControllers;
import com.example.libraryapp.models.Category;

import java.util.ArrayList;

public class Library extends AppCompatActivity {

    DatabaseControllers databaseControllers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        appBar();
        databaseControllers = new DatabaseControllers(getApplicationContext());
        ArrayList<Category> categoriesNames = databaseControllers.getAllCategories();

        CategoryAdapter categoryAdapter = new CategoryAdapter(categoriesNames);
        RecyclerView recyclerView = findViewById(R.id.rvLib);
        recyclerView.setAdapter(categoryAdapter);

        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(lm);
    }


    public void appBar() {
        ImageButton btnBackMain = findViewById(R.id.btnBack);
        btnBackMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent n = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(n);
            }
        });
    }
}