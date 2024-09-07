package com.elotech.biblioteca_arom.controllers;

import com.elotech.biblioteca_arom.entities.Loan;
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
    public ResponseEntity<Loan> createLoan(@RequestBody Loan loan) {
        Loan createdLoan = loanService.createLoan(loan);
        return new ResponseEntity<>(createdLoan, HttpStatus.CREATED);
    }

    /**
     * Atualiza um empréstimo existente com base no ID fornecido.
     *
     * @param id o ID do empréstimo a ser atualizado
     * @param returnDateStr a data de devolução no formato String
     * @return uma resposta HTTP com o empréstimo atualizado e status 200 (OK)
     */
    @PutMapping("/{id}")
    public ResponseEntity<Loan> updateLoan(@PathVariable Long id, @RequestParam("returnDate") String returnDateStr) {
        LocalDate returnDate = LocalDate.parse(returnDateStr);
        Loan updatedLoan = loanService.updateLoan(id, returnDate);
        return ResponseEntity.ok(updatedLoan);
    }

    /**
     * Retorna uma lista de todos os empréstimos cadastrados.
     *
     * @return uma lista de empréstimos
     */
    @GetMapping
    public ResponseEntity<List<Loan>> getAllLoans() {
        List<Loan> loans = loanService.getAllLoans();
        return ResponseEntity.ok(loans);
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
