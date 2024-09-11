package com.elotech.biblioteca_arom.controllers;

import com.elotech.biblioteca_arom.entities.Book;
import com.elotech.biblioteca_arom.services.BookService;
import com.elotech.biblioteca_arom.services.GoogleBooksService;
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

    private final GoogleBooksService googleBooksService;

    /**
     * Construtor que injeta o serviço de gerenciamento de livros.
     *
     * @param bookService o serviço de livros
     */
    @Autowired
    public BookController(BookService bookService, GoogleBooksService googleBooksService) {
        this.bookService = bookService;
        this.googleBooksService = googleBooksService;
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
     * Endpoint para buscar livros na API do Google Books com base no termo de busca fornecido.
     * O termo de busca será utilizado para fazer uma requisição à API do Google Books,
     * e os resultados encontrados serão retornados como uma lista de objetos Book.
     *
     * @param query o termo de busca a ser utilizado para procurar livros (exemplo: título, autor, etc.)
     * @return ResponseEntity contendo uma lista de objetos Book correspondentes ao termo de busca
     *         e o status HTTP 200 (OK) se a busca for bem-sucedida.
     */
    @GetMapping("/api/books/search")
    public ResponseEntity<List<Book>> searchBooks(@RequestParam String query) {
        List<Book> books = googleBooksService.searchBooks(query);
        return ResponseEntity.ok(books);
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
