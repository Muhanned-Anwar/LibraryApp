package com.example.libraryapp.models;

public class Book {
    private int id;
    private String bookName;
    private String authorName;
    private String date;
    private int pagesNumber;
    private byte[] img;
    private int category_id;

    public Book(String bookName, String authorName, String date, int pagesNumber, byte[] img, int category_id) {
        this.bookName = bookName;
        this.authorName = authorName;
        this.date = date;
        this.pagesNumber = pagesNumber;
        this.img = img;
        this.category_id = category_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getPagesNumber() {
        return pagesNumber;
    }

    public void setPagesNumber(int pagesNumber) {
        this.pagesNumber = pagesNumber;
    }
}
