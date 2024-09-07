package com.elotech.biblioteca_arom.services;

import com.elotech.biblioteca_arom.entities.Book;
import com.elotech.biblioteca_arom.repositories.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes para a classe BookService, cobrindo as operações de criação, consulta,
 * atualização e exclusão de livros.
 */
@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    /**
     * Testa a criação de um livro.
     * Verifica se o livro é salvo corretamente e se o título é atribuído.
     */
    @Test
    public void testCreateBook() {
        Book book = new Book();
        book.setTitle("Test Book");

        when(bookRepository.save(book)).thenReturn(book);
        Book createdBook = bookService.createBook(book);

        assertNotNull(createdBook);
        assertEquals("Test Book", createdBook.getTitle());
    }

    /**
     * Testa a recuperação de todos os livros.
     * Verifica se a lista de livros é retornada corretamente.
     */
    @Test
    public void testGetAllBooks() {
        Book book1 = new Book();
        book1.setTitle("Book 1");
        Book book2 = new Book();
        book2.setTitle("Book 2");

        List<Book> books = Arrays.asList(book1, book2);
        when(bookRepository.findAll()).thenReturn(books);

        List<Book> result = bookService.getAllBooks();
        assertEquals(2, result.size());
        assertEquals("Book 1", result.get(0).getTitle());
        assertEquals("Book 2", result.get(1).getTitle());
    }

    /**
     * Testa a recuperação de um livro por ID.
     * Verifica se o livro é encontrado corretamente e se os dados correspondem ao esperado.
     */
    @Test
    public void testGetBookById() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Book foundBook = bookService.getBookById(1L);

        assertNotNull(foundBook);
        assertEquals(1L, foundBook.getId());
        assertEquals("Test Book", foundBook.getTitle());
    }

    /**
     * Testa a atualização de um livro existente.
     * Verifica se os dados do livro são atualizados corretamente.
     */
    @Test
    public void testUpdateBook() {
        Book existingBook = new Book();
        existingBook.setId(1L);
        existingBook.setTitle("Old Title");

        Book updatedBook = new Book();
        updatedBook.setTitle("New Title");
        updatedBook.setAuthor("New Author");
        updatedBook.setIsbn("123456789");
        updatedBook.setCategory("New Category");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(existingBook)).thenReturn(existingBook);

        Book result = bookService.updateBook(1L, updatedBook);

        assertNotNull(result);
        assertEquals("New Title", result.getTitle());
        assertEquals("New Author", result.getAuthor());
        assertEquals("123456789", result.getIsbn());
        assertEquals("New Category", result.getCategory());
    }

    /**
     * Testa a exclusão de um livro.
     * Verifica se o método de exclusão foi chamado no repositório.
     */
    @Test
    public void testDeleteBook() {
        Long bookId = 1L;
        doNothing().when(bookRepository).deleteById(bookId);

        bookService.deleteBook(bookId);

        verify(bookRepository, times(1)).deleteById(bookId);
    }
}
