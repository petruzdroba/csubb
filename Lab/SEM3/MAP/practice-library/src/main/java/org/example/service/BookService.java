package org.example.service;

import org.example.domain.Book;
import org.example.repo.BookRepo;

import java.util.List;

public class BookService {
    private final BookRepo bookRepo;


    public BookService(BookRepo bookRepo) {
        this.bookRepo = bookRepo;
    }

    public List<Book> getAll(){
        return bookRepo.getAll();
    }

    public List<String> getGenres(){
        return bookRepo.getGenres();
    }

    public List<Book> getFiltered(String genre){
        return bookRepo.getByGenreAvailable(genre);
    }
}
