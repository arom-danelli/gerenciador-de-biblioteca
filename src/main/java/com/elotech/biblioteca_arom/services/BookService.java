package com.elotech.biblioteca_arom.services;

import com.elotech.biblioteca_arom.entities.Book;
import com.elotech.biblioteca_arom.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class BookService {
    private final BookRepository bookRepository;
    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }


    //CREATE
    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    // READ (find All)
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // READ (find by ID)
    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Livro não encontrado!"));
    }

    // UPDATE
    public Book updateBook(Long id, Book updateBook) {
        Book existingBook = getBookById(id);
        existingBook.setTitle(updateBook.getTitle());
        existingBook.setAuthor(updateBook.getAuthor());
        existingBook.setIsbn(updateBook.getIsbn());
        existingBook.setCategory(updateBook.getCategory());
        return bookRepository.save(existingBook);


    }

    // DELETE
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}
