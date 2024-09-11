package com.elotech.biblioteca_arom.controllers;

import com.elotech.biblioteca_arom.entities.Book;
import com.elotech.biblioteca_arom.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador responsável por gerenciar as operações relacionadas aos livros.
 * Fornece endpoints para criação, atualização, obtenção e exclusão de livros.
 */
@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;


    /**
     * Construtor que injeta o serviço de gerenciamento de livros.
     *
     * @param bookService o serviço de livros
     */
    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Cria um novo livro.
     *
     * @param book o objeto Book contendo as informações do livro a ser criado
     * @return uma resposta HTTP com o livro criado e status 201 (Created)
     */
    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        Book createdBook = bookService.createBook(book);
        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }

    /**
     * Retorna uma lista de todos os livros cadastrados.
     *
     * @return uma lista de livros
     */
    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    /**
     * Retorna um livro com base no ID fornecido.
     *
     * @param id o ID do livro a ser recuperado
     * @return uma resposta HTTP com o livro encontrado e status 200 (OK)
     */
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Book book = bookService.getBookById(id);
        return ResponseEntity.ok(book);
    }

    /**
     * Atualiza um livro existente com base no ID fornecido.
     *
     * @param id o ID do livro a ser atualizado
     * @param updatedBook o objeto Book com as novas informações
     * @return uma resposta HTTP com o livro atualizado e status 200 (OK)
     */
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book updatedBook) {
        Book book = bookService.updateBook(id, updatedBook);
        return ResponseEntity.ok(book);
    }

    /**
     * Exclui um livro com base no ID fornecido.
     *
     * @param id o ID do livro a ser excluído
     * @return uma resposta HTTP com status 204 (No Content) se a exclusão for bem-sucedida
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
