package com.example.libraryapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.libraryapp.controllers.DatabaseControllers;
import com.example.libraryapp.models.Book;
import com.example.libraryapp.screens.Library;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class UpdateBook extends AppCompatActivity {

    Spinner spCategories;

    ImageView bookImage;
    EditText bookName;
    EditText authorName;
    EditText releaseYear;
    EditText pagesNumber;
    Button btnUpdateBook;
    ImageButton btnUploadImage;
    int bookId;

    DatabaseControllers databaseControllers;

    public static final int PERMISSION_REQUEST_CODE = 1020;
    public static final int BOOK_IMAGE_REQUEST_CODE = 1202;
    public static Uri bookImageURI;
    public static byte[] bookImageByteArray;
    public static Bitmap bookImageBitMap;

    TextView tvStoragePermission;

    private boolean storagePermissionStatus = MainActivity.storagePermissionStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_book);
        databaseControllers = new DatabaseControllers(getApplicationContext());
        Intent intent = getIntent();
        bookId = (int) intent.getSerializableExtra("bookId");
        String IBookName = (String) intent.getSerializableExtra("bookName");
        String IBookAuthor = (String) intent.getSerializableExtra("bookAuthor");
        String IDate = (String) intent.getSerializableExtra("date");
        String IPagesNumber = (String) intent.getSerializableExtra("pagesNumber");
        int categorySelection = (int) intent.getSerializableExtra("categorySelection");

        byte[] IImgByteArray = (byte[]) intent.getSerializableExtra("imgByteArray");
        bookImageByteArray = IImgByteArray;
        bookImageBitMap = BitmapFactory.decodeByteArray(IImgByteArray, 0, IImgByteArray.length);

        appBar();
        spinner(categorySelection);
        btnUploadImage = findViewById(R.id.btnUploadImageU);
        pickImage();

        bookImage = findViewById(R.id.bookImageU);
        bookName = findViewById(R.id.edBookNameU);
        authorName = findViewById(R.id.edAuthorNameU);
        releaseYear = findViewById(R.id.edReleaseYearU);
        pagesNumber = findViewById(R.id.edPagesNumberU);

        bookImage.setImageBitmap(bookImageBitMap);
        bookName.setText(IBookName);
        authorName.setText(IBookAuthor);
        releaseYear.setText(IDate);
        pagesNumber.setText(IPagesNumber);

        btnUpdateBook = findViewById(R.id.btnUpdateBook);

        tvStoragePermission = findViewById(R.id.tvStoragePermissionStatus);

        btnUpdateBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputValidation()) {
                    int result = databaseControllers.updateBook(readBookData());
                    if (result > 0) {
                        Intent n = new Intent(getApplicationContext(), Library.class);
                        startActivity(n);                    }
                }
            }
        });
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

    public void spinner(int categorySelection) {
        spCategories = findViewById(R.id.spCategoryU);

        ArrayList<String> spDataCategories = databaseControllers.getAllCategoriesNames();

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spDataCategories);
        spCategories.setAdapter(spinnerAdapter);
        System.out.println("Selection = " + categorySelection);
        spCategories.setSelection(categorySelection);

    }

    public boolean inputValidation() {
        boolean flag = true;
        if (bookName.getText().toString().isEmpty()) {
            bookName.setError("Error, Can't be Empty");
            flag = false;
        }

        if (authorName.getText().toString().isEmpty()) {
            authorName.setError("Error, Can't be Empty");
            flag = false;
        }
        if (releaseYear.getText().toString().isEmpty()) {
            releaseYear.setError("Error, Can't be Empty");
            flag = false;
        }
        if (pagesNumber.getText().toString().isEmpty()) {
            pagesNumber.setError("Error, Can't be Empty");
            flag = false;
        }
        if (bookImageByteArray == null) {
            Toast.makeText(this, "You should select image for book..", Toast.LENGTH_LONG).show();
            flag = false;
        }
        return flag;
    }

    public Book readBookData() {
        String book_name, author_name, release_year, spSelected;
        int pages_number, category_id, book_id;
        byte[] book_image;
        book_id = bookId;
        book_name = bookName.getText().toString();
        author_name = authorName.getText().toString();
        release_year = releaseYear.getText().toString();
        pages_number = Integer.parseInt(pagesNumber.getText().toString());
        spSelected = spCategories.getSelectedItem().toString();
        category_id = databaseControllers.getCategoryId(spSelected);
        book_image = bookImageByteArray;
        Book newBook = new Book(book_name, author_name, release_year, pages_number, book_image, category_id);
        newBook.setId(book_id);
        return newBook;
    }

    public void pickImage() {
        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (storagePermissionStatus) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Select Book Image"), BOOK_IMAGE_REQUEST_CODE);
                } else {
                    checkPermission();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                tvStoragePermission.setText("User Denied Storage Permission");
                storagePermissionStatus = false;
            } else {
                tvStoragePermission.setText("User Granted Storage Permission");
                storagePermissionStatus = true;
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case BOOK_IMAGE_REQUEST_CODE:
                if (data != null) {
                    bookImageURI = data.getData();
//                    bookImage.setImageURI(bookImageURI);
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(bookImageURI);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        bookImage.setImageBitmap(bitmap);
                        bookImageByteArray = getBytes(bitmap);
                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(this, "Please select profile image", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void checkPermission() {
        int permissionStatus = ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permissionStatus == PackageManager.PERMISSION_DENIED) {
            String[] permission = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(UpdateBook.this, permission, PERMISSION_REQUEST_CODE);
        } else {
            storagePermissionStatus = true;
        }
    }

    public byte[] getBytes(Bitmap bitmap){
        ByteArrayOutputStream stream =  new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0,stream);
        return  stream.toByteArray();
    }
}