package com.example.libraryapp.screens;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.libraryapp.MainActivity;
import com.example.libraryapp.R;
import com.example.libraryapp.controllers.DatabaseControllers;
import com.example.libraryapp.models.Book;
import com.example.libraryapp.models.Category;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class CreateBook extends AppCompatActivity {

    int category;
    Spinner spCategories;

    ImageView bookImage;
    EditText bookName;
    EditText authorName;
    EditText releaseYear;
    EditText pagesNumber;
    Button btnCreateBook;
    ImageButton btnUploadImage;

    public static DatabaseControllers databaseControllers;

    public static final int PERMISSION_REQUEST_CODE = 1020;
    public static final int BOOK_IMAGE_REQUEST_CODE = 1202;
    public static Uri bookImageURI;
    public static byte[] bookImageBitMap;

    TextView tvStoragePermission;

    private boolean storagePermissionStatus = MainActivity.storagePermissionStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_book);
        Intent intent = getIntent();
//        storagePermissionStatus = (boolean) intent.getSerializableExtra("storagePermissionStatus");
        databaseControllers = new DatabaseControllers(getApplicationContext());
        appBar();
        spinner();
        btnUploadImage = findViewById(R.id.btnUploadImage);
        pickImage();

        bookImage = findViewById(R.id.bookImage);
        bookName = findViewById(R.id.edBookName);
        authorName = findViewById(R.id.edAuthorName);
        releaseYear = findViewById(R.id.edReleaseYear);
        pagesNumber = findViewById(R.id.edPagesNumber);

        btnCreateBook = findViewById(R.id.btnCreateBook);

        tvStoragePermission = findViewById(R.id.tvStoragePermissionStatus);

        ArrayList<Book> books = new ArrayList<>();

        btnCreateBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputValidation()) {
                    databaseControllers.insertBook(readBookData());
                }
            }
        });
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

    public void spinner() {
        spCategories = findViewById(R.id.spCategory);

        ArrayList<String> spDataCategories = databaseControllers.getAllCategoriesNames();

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spDataCategories);
        spCategories.setAdapter(spinnerAdapter);


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
        if (bookImageURI == null) {
            Toast.makeText(this, "You should select image for book..", Toast.LENGTH_LONG).show();
            flag = false;
        }
        return flag;
    }

    public Book readBookData() {
        String book_name, author_name, release_year, spSelected;
        int pages_number, category_id;
        byte[] book_image;
        book_name = bookName.getText().toString();
        bookName.setText("");
        author_name = authorName.getText().toString();
        authorName.setText("");
        release_year = releaseYear.getText().toString();
        releaseYear.setText("");
        pages_number = Integer.parseInt(pagesNumber.getText().toString());
        pagesNumber.setText("");
        spSelected = spCategories.getSelectedItem().toString();
        category_id = databaseControllers.getCategoryId(spSelected);
        book_image = bookImageBitMap;
        bookImage.setImageURI(null);
        bookImageURI = null;
        bookImageBitMap = null;
        Toast.makeText(this, "Inserted is successfully", Toast.LENGTH_LONG);

        return new Book(book_name, author_name, release_year, pages_number, book_image, category_id);
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
                    bookImage.setImageURI(bookImageURI);
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(bookImageURI);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                        bookImage.setImageBitmap(bitmap);
                        bookImageBitMap = getBytes(bitmap);
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
            ActivityCompat.requestPermissions(CreateBook.this, permission, PERMISSION_REQUEST_CODE);
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