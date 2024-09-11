package com.elotech.biblioteca_arom.controllers;

import com.elotech.biblioteca_arom.dtos.LoanDTO;
import com.elotech.biblioteca_arom.entities.Book;
import com.elotech.biblioteca_arom.entities.Loan;
import com.elotech.biblioteca_arom.entities.enums.Status;
import com.elotech.biblioteca_arom.services.LoanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador responsável por gerenciar as operações relacionadas aos empréstimos de livros.
 * Fornece endpoints para criação, atualização, obtenção e exclusão de empréstimos.
 */
@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService loanService;

    /**
     * Construtor que injeta o serviço de empréstimos.
     *
     * @param loanService o serviço de gerenciamento de empréstimos
     */
    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    /**
     * Cria um novo empréstimo.
     *
     * @param loan o objeto Loan contendo as informações do empréstimo a ser criado
     * @return uma resposta HTTP com o empréstimo criado e status 201 (Created)
     */
    @PostMapping
    public ResponseEntity<?> createLoan(@RequestBody Loan loan) {
        try {
            Loan createdLoan = loanService.createLoan(loan);
            return new ResponseEntity<>(createdLoan, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Atualiza um empréstimo existente com base no ID fornecido.
     *
     * @param id o ID do empréstimo a ser atualizado
     * @param returnDateStr a data de devolução no formato String
     * @return uma resposta HTTP com o empréstimo atualizado e status 200 (OK)
     */
    @PutMapping("/{id}")
    public ResponseEntity<Loan> updateLoan(
            @PathVariable Long id,
            @RequestParam("returnDate") String returnDateStr,
            @RequestParam(value = "status", required = false) String statusStr) {

        LocalDate returnDate = LocalDate.parse(returnDateStr);
        Status status = (statusStr != null) ? Status.valueOf(statusStr) : null;

        Loan updatedLoan = loanService.updateLoan(id, returnDate, status);
        return ResponseEntity.ok(updatedLoan);
    }


    /**
     * Retorna uma lista de todos os empréstimos cadastrados.
     *
     * @return uma lista de empréstimos
     */
    @GetMapping
    public ResponseEntity<List<LoanDTO>> getAllLoans() {
        List<LoanDTO> loanDetails = loanService.getAllLoanDetails();
        return ResponseEntity.ok(loanDetails);
    }

    /**
     * Retorna uma lista de empréstimos associados a um determinado usuário.
     *
     * @param userId o ID do usuário
     * @return uma lista de empréstimos do usuário
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Loan>> getLoansByUser(@PathVariable Long userId) {
        List<Loan> userLoans = loanService.getLoansByUser(userId);
        return ResponseEntity.ok(userLoans);
    }

    /**
     * Retorna uma lista de empréstimos associados a um determinado livro.
     *
     * @param bookId o ID do livro
     * @return uma lista de empréstimos do livro
     */
    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<Loan>> getLoansByBook(@PathVariable Long bookId) {
        List<Loan> bookLoans = loanService.getLoansByBook(bookId);
        return ResponseEntity.ok(bookLoans);
    }

    /**
     * Endpoint para recomendar livros com base nas categorias dos livros que o usuário já emprestou.
     * O sistema verifica quais categorias de livros o usuário já emprestou e, a partir disso,
     * recomenda livros da mesma categoria que ele ainda não pegou emprestado.
     *
     * @param userId o ID do usuário para o qual as recomendações serão geradas
     * @return ResponseEntity contendo uma lista de objetos Book recomendados ao usuário
     *         e o status HTTP 200 (OK) se as recomendações forem geradas com sucesso.
     */
    @GetMapping("/recomendations/{userId}")
    public ResponseEntity<List<Book>> recomendBooks(@PathVariable Long userId) {
        List<Book> recomendations = loanService.recomendBooksForUser(userId);
        return ResponseEntity.ok(recomendations);
    }

    /**
     * Exclui um empréstimo com base no ID fornecido.
     *
     * @param id o ID do empréstimo a ser excluído
     * @return uma resposta HTTP com status 204 (No Content) se a exclusão for bem-sucedida
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLoan(@PathVariable Long id) {
        loanService.deleteLoan(id);
        return ResponseEntity.noContent().build();
    }
}
