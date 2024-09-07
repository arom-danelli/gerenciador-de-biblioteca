package com.elotech.biblioteca_arom.controllers;


import com.elotech.biblioteca_arom.entities.Loan;
import com.elotech.biblioteca_arom.services.LoanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {
    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }


    @PostMapping
    public ResponseEntity<Loan> createLoan(@RequestBody Loan loan){
        Loan createdLoan = loanService.createLoan(loan);
        return new ResponseEntity<>(createdLoan, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Loan> updateLoan(@PathVariable Long id, @RequestParam("returnDate") String returnDateStr) {
        LocalDate returnDate = LocalDate.parse(returnDateStr);
        Loan updatedLoan = loanService.updateLoan(id, returnDate);
        return ResponseEntity.ok(updatedLoan);
    }

    @GetMapping
    public ResponseEntity<List<Loan>> getAllLoans() {
        List<Loan> loans = loanService.getAllLoans();
        return ResponseEntity.ok(loans);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Loan>> getLoansByUser(@PathVariable Long userId) {
        List<Loan> userLoans = loanService.getLoansByUser(userId);
        return ResponseEntity.ok(userLoans);
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<Loan>> getLoansByBook(@PathVariable Long bookId) {
        List<Loan> bookLoans = loanService.getLoansByBook(bookId);
        return ResponseEntity.ok(bookLoans);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLoan(@PathVariable Long id) {
        loanService.deleteLoan(id);
        return ResponseEntity.noContent().build();
    }

}
