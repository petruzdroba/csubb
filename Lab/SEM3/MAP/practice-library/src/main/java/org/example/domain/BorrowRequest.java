package org.example.domain;

import java.time.LocalDateTime;
import java.util.List;

public class BorrowRequest {
    private Long id;
    private Patron patron;
    private List<Book> books;
    private LocalDateTime date;
    private Status status;

    public BorrowRequest(Long id, Patron patron, List<Book> books, LocalDateTime date, Status status) {
        this.id = id;
        this.patron = patron;
        this.books = books;
        this.date = date;
        this.status = status;
    }

    public BorrowRequest(Patron patron, List<Book> books) {
        this.id = null;
        this.patron = patron;
        this.books = books;
        this.date=LocalDateTime.now();
        this.status = Status.REQUESTED;
    }

    public BorrowRequest() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Patron getPatron() {
        return patron;
    }

    public void setPatron(Patron patron) {
        this.patron = patron;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
