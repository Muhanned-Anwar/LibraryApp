package com.example.libraryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.libraryapp.screens.CreateBook;
import com.example.libraryapp.screens.CreateCategory;
import com.example.libraryapp.screens.Favorite;
import com.example.libraryapp.screens.Library;

public class MainActivity extends AppCompatActivity {

    public static final int PERMISSION_REQUEST_CODE = 1020;
    public static final int BOOK_IMAGE_REQUEST_CODE = 1202;

    public static boolean storagePermissionStatus = false;
    TextView tvStoragePermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnLibrary = findViewById(R.id.btnLibrary);
        Button btnFavorite = findViewById(R.id.btnFavorite);
        Button btnBook = findViewById(R.id.btnBook);
        Button btnCategory = findViewById(R.id.btnCategory);

        tvStoragePermission = findViewById(R.id.tvStoragePermissionStatusMain);
        pickImage();

        btnLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent n = new Intent(getApplicationContext(), Library.class);
                startActivity(n);
            }
        });
        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent n = new Intent(getApplicationContext(), Favorite.class);
                startActivity(n);
            }
        });
        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent n = new Intent(getApplicationContext(), CreateBook.class);
//                n.putExtra("storagePermissionStatus", storagePermissionStatus);
                startActivity(n);
            }
        });
        btnCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent n = new Intent(getApplicationContext(), CreateCategory.class);
                startActivity(n);
            }
        });


    }

    public void pickImage() {
        checkPermission();
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

    public void checkPermission() {
        int permissionStatus = ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permissionStatus == PackageManager.PERMISSION_DENIED) {
            String[] permission = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(MainActivity.this, permission, PERMISSION_REQUEST_CODE);
        } else {
            storagePermissionStatus = true;
        }
    }
}