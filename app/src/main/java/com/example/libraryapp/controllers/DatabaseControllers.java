package com.example.libraryapp.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.libraryapp.models.Book;
import com.example.libraryapp.models.Category;

import java.util.ArrayList;

public class DatabaseControllers extends SQLiteOpenHelper {

    public static final String DB_NAME = "LIBRARY";
    public static final int DB_VERSION = 4;

    public static final String TABLE_CATEGORY_NAME = "CATEGORIES";
    public static final String CATEGORY_ID = "CATEGORY_ID";
    public static final String CATEGORY_NAME = "CATEGORY_NAME";


    public static final String TABLE_BOOK_NAME = "BOOKS";
    public static final String BOOK_ID = "BOOK_id";
    public static final String BOOK_NAME = "BOOK_NAME";
    public static final String BOOK_AUTHOR_NAME = "BOOK_AUTHOR_NAME";
    public static final String BOOK_DATE = "BOOK_DATE";
    public static final String BOOK_PAGES_NUMBER = "BOOK_PAGES_NUMBER";
    public static final String BOOK_KEY_IMG_URL = "ImgFavourite";

    Context context;

    public DatabaseControllers(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {


        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_CATEGORY_NAME +
                " (" + CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + CATEGORY_NAME + " TEXT NOT NULL);");


        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_BOOK_NAME +
                " (" +
                BOOK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                BOOK_NAME + " TEXT," +
                BOOK_AUTHOR_NAME + " TEXT," +
                BOOK_DATE + " TEXT," +
                BOOK_PAGES_NUMBER + " INTEGER, " +
                BOOK_KEY_IMG_URL + " BLOB, " +
                CATEGORY_ID + " INTEGER," +
                "FOREIGN KEY(" + CATEGORY_ID + ") REFERENCES " + TABLE_CATEGORY_NAME + "(" + CATEGORY_ID + ") );");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOK_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY_NAME);
        onCreate(sqLiteDatabase);
    }

    public long insertCategory(Category category) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(CATEGORY_NAME, category.getName());

        long result = db.insert(TABLE_CATEGORY_NAME, null, cv);
        return result;
    }

    public void insertBook(Book book) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(BOOK_NAME, book.getBookName());
        cv.put(BOOK_AUTHOR_NAME, book.getAuthorName());
        cv.put(BOOK_DATE, book.getDate());
        cv.put(BOOK_PAGES_NUMBER, book.getPagesNumber());
        cv.put(BOOK_KEY_IMG_URL, book.getImg());
        cv.put(CATEGORY_ID, book.getCategory_id());

        long result = db.insert(TABLE_BOOK_NAME, null, cv);
        if (result != -1) {
            Toast.makeText(context.getApplicationContext(), "Created Successfully :)", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context.getApplicationContext(), "Created Failed ):", Toast.LENGTH_LONG).show();
        }

    }

    public void updateCategory(Category category) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CATEGORY_NAME, category.getName());
        String args[] = {category.getId() + ""};
        db.update(TABLE_CATEGORY_NAME, cv, CATEGORY_ID + "?", args);

    }

    public int updateBook(Book book) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(BOOK_NAME, book.getBookName());
        cv.put(BOOK_AUTHOR_NAME, book.getAuthorName());
        cv.put(BOOK_DATE, book.getDate());
        cv.put(BOOK_PAGES_NUMBER, book.getPagesNumber());
        cv.put(BOOK_KEY_IMG_URL, book.getImg());
        cv.put(CATEGORY_ID, book.getCategory_id());

        String args[] = {book.getId() + ""};
//        db.execSQL("update " + TABLE_BOOK_NAME + " set " + BOOK_ID + );
        int result = db.update(TABLE_BOOK_NAME, cv, BOOK_ID + "=?", args);

        if (result > 0) {
            Toast.makeText(context.getApplicationContext(), "Updated is successfully", Toast.LENGTH_LONG).show();
        } else {
            System.out.println("Result = " + result);
            Toast.makeText(context.getApplicationContext(), "Updated is Failed", Toast.LENGTH_LONG).show();
        }
        return result;
    }

    public ArrayList<Category> getAllCategories() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Category> categories = new ArrayList<>();

        String query = "select * from " + TABLE_CATEGORY_NAME + ";";
        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);

            Category category = new Category(name);
            category.setId(id);
            categories.add(category);

            cursor.moveToNext();
        }
        return categories;
    }

    public ArrayList<String> getAllCategoriesNames() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<String> names = new ArrayList<>();

        String query = "select " + CATEGORY_NAME + " from " + TABLE_CATEGORY_NAME + ";";
        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String name = cursor.getString(0);
            names.add(name);

            cursor.moveToNext();
        }
        return names;
    }

    public ArrayList<Book> getAllBooks() {
        SQLiteDatabase db = getReadableDatabase();
        String query = "select * from " + TABLE_BOOK_NAME + ";";
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }

        if (cursor == null) {
            return null;
        } else {
            ArrayList<Book> books = new ArrayList<>();
            cursor.moveToFirst();
//            while (cursor.moveToNext())
            while (!cursor.isAfterLast()) {
                int id = cursor.getInt(0);
                String bookName = cursor.getString(1);
                String authorName = cursor.getString(2);
                String date = cursor.getString(3);
                int pagesNumber = cursor.getInt(4);
                byte[] img = cursor.getBlob(5);
                int category_id = cursor.getInt(6);


                Book book = new Book(bookName, authorName, date, pagesNumber, img, category_id);
                book.setId(id);
                books.add(book);

                cursor.moveToNext();
            }
            return books;
        }

    }

    public ArrayList<Book> getBooks(int idCategory) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "select * from " + TABLE_BOOK_NAME + " where " + CATEGORY_ID + "=?;";
        String[] args = {String.valueOf(idCategory)};

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, args);
        }

        if (cursor == null) {
            return null;
        } else {
            ArrayList<Book> books = new ArrayList<>();
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int id = cursor.getInt(0);
                String bookName = cursor.getString(1);
                String authorName = cursor.getString(2);
                String date = cursor.getString(3);
                int pagesNumber = cursor.getInt(4);
                byte[] img = cursor.getBlob(5);
                int category_id = cursor.getInt(6);


                Book book = new Book(bookName, authorName, date, pagesNumber, img, category_id);
                book.setId(id);
                books.add(book);

                cursor.moveToNext();
            }
            return books;
        }
    }

    public boolean deleteCategory(String id) {
        SQLiteDatabase db = getWritableDatabase();
        String[] args = {id};
        int result = db.delete(TABLE_CATEGORY_NAME, CATEGORY_ID + "=?", args);
        if (result == Integer.parseInt(id)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteBook(String id) {
        SQLiteDatabase db = getWritableDatabase();
        String[] args = {id};
        int result = db.delete(TABLE_BOOK_NAME, BOOK_ID + "=?", args);
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void deleteAllCategories() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from " + TABLE_CATEGORY_NAME + ";");
    }

    public void deleteAllBooks() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from " + TABLE_BOOK_NAME + ";");
    }

    public int getCategoryId(String name) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "select " + CATEGORY_ID + " from " + TABLE_CATEGORY_NAME + " where " + CATEGORY_NAME + " =?;";
        String[] args = {name};
        Cursor cursor = db.rawQuery(query, args);
        cursor.moveToFirst();

        return cursor.getInt(0);
    }

}
