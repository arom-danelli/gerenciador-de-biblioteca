package com.elotech.biblioteca_arom.services;

import com.elotech.biblioteca_arom.entities.Book;
import com.elotech.biblioteca_arom.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
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

    public boolean hasActiveLoan(Long bookId) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            return book.getLoans().stream().anyMatch(loan -> loan.getReturn_date() == null);
        }
        throw new RuntimeException("Livro não encontrado!");
    }
}
