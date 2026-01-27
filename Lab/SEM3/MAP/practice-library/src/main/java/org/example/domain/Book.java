package org.example.domain;

public class Book {
    private Long id;
    private String genre;
    private String title;
    private String author;
    private boolean available;

    public Book() {
    }

    public Book(Long id, String genre, String title, String author, boolean available) {
        this.id = id;
        this.genre = genre;
        this.title = title;
        this.author = author;
        this.available = available;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return title + " by " + author + "\n";
    }
}
