package com.elotech.biblioteca_arom.services;

import com.elotech.biblioteca_arom.entities.Book;
import com.elotech.biblioteca_arom.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável pela gestão de livros no sistema de biblioteca.
 * Fornece operações CRUD (criar, ler, atualizar, excluir) e funcionalidades adicionais,
 * como verificar se um livro tem empréstimos ativos.
 */
@Service
public class BookService {

    private final BookRepository bookRepository;

    /**
     * Construtor que injeta o repositório de livros no serviço.
     *
     * @param bookRepository o repositório de livros usado para acessar os dados persistentes
     */
    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Cria um novo livro no sistema.
     *
     * @param book o objeto Book a ser salvo
     * @return o livro criado com um ID gerado
     */
    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    /**
     * Recupera todos os livros disponíveis no sistema.
     *
     * @return uma lista contendo todos os livros
     */
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    /**
     * Recupera um livro específico pelo seu ID.
     *
     * @param id o ID do livro a ser recuperado
     * @return o livro correspondente ao ID fornecido
     * @throws RuntimeException se o livro não for encontrado
     */
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado!"));
    }

    /**
     * Atualiza os detalhes de um livro existente.
     *
     * @param id o ID do livro a ser atualizado
     * @param updateBook o objeto Book contendo os novos dados do livro
     * @return o livro atualizado
     * @throws RuntimeException se o livro não for encontrado
     */
    public Book updateBook(Long id, Book updateBook) {
        Book existingBook = getBookById(id);
        existingBook.setTitle(updateBook.getTitle());
        existingBook.setAuthor(updateBook.getAuthor());
        existingBook.setIsbn(updateBook.getIsbn());
        existingBook.setCategory(updateBook.getCategory());
        return bookRepository.save(existingBook);
    }

    /**
     * Exclui um livro do sistema pelo seu ID.
     *
     * @param id o ID do livro a ser excluído
     */
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    /**
     * Verifica se um livro específico possui um empréstimo ativo (não devolvido).
     *
     * @param bookId o ID do livro a ser verificado
     * @return true se o livro tiver um empréstimo ativo, false caso contrário
     * @throws RuntimeException se o livro não for encontrado
     */
    public boolean hasActiveLoan(Long bookId) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            return book.getLoans().stream().anyMatch(loan -> loan.getReturn_date() == null);
        }
        throw new RuntimeException("Livro não encontrado!");
    }
}
