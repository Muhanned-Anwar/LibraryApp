package com.example.libraryapp.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.libraryapp.MainActivity;
import com.example.libraryapp.R;
import com.example.libraryapp.controllers.DatabaseControllers;
import com.example.libraryapp.models.Category;

import java.util.ArrayList;

public class CreateCategory extends AppCompatActivity {
    public static ArrayList<String> Categories;
    static EditText edCategoryName;
    DatabaseControllers databaseControllers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_category);

        databaseControllers = new DatabaseControllers(getApplicationContext());

        ImageButton btnBackMain = findViewById(R.id.btnBack);
        btnBackMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent n = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(n);
            }
        });

        edCategoryName = findViewById(R.id.edCategoryName);
        Button btnCreate = findViewById(R.id.btnCreate);
        Categories = new ArrayList<>();
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputValidation()) {
                    long result = databaseControllers.insertCategory(readCategoryData());
                    System.out.println("Result = " + result);
                    if (result != -1) {
                        Toast.makeText(getApplicationContext(), "Created Successfully :)", Toast.LENGTH_LONG).show();
                    }

                    Categories.add(edCategoryName.getText().toString());
                    edCategoryName.setText("");
                }
            }
        });
    }

    public boolean inputValidation() {
        boolean flag = true;
        if (edCategoryName.getText().toString().isEmpty()) {
            edCategoryName.setError("Error, Can't be Empty");
            flag = false;
        }
        return flag;
    }

    public Category readCategoryData() {
        String name;
        name = edCategoryName.getText().toString();
        Category category = new Category(name);
        return category;
    }
}