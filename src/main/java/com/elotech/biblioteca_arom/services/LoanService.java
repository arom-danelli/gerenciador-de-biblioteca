package com.elotech.biblioteca_arom.services;

import com.elotech.biblioteca_arom.entities.Loan;
import com.elotech.biblioteca_arom.entities.enums.Status;
import com.elotech.biblioteca_arom.repositories.LoanRepository;
import com.elotech.biblioteca_arom.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final BookService bookService;

    @Autowired
    public LoanService(LoanRepository loanRepository, BookRepository bookRepository, BookService bookService) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.bookService = bookService;
    }

    // 1. Criar Empréstimos (associar um livro a um usuário)
    public Loan createLoan(Loan loan) {
        // Validação da data de empréstimo
        validateLoanDate(loan.getLoan_date());

        // Verificar se o livro já está emprestado (tem um empréstimo ativo)
        if (bookService.hasActiveLoan(loan.getBook().getId())) {
            throw new RuntimeException("O livro já está emprestado e não pode ser emprestado novamente até ser devolvido.");
        }

        // Definir o status como "EMPRESTADO" ao criar o empréstimo
        loan.setStatus(Status.EMPRESTADO);

        // Salvar o novo empréstimo
        return loanRepository.save(loan);
    }

    // 2. Atualizar Empréstimos (alterar status e data de devolução)
    public Loan updateLoan(Long loanId, LocalDate returnDate) {
        // Buscar o empréstimo pelo ID
        Optional<Loan> loanOptional = loanRepository.findById(loanId);
        if (loanOptional.isEmpty()) {
            throw new RuntimeException("Empréstimo não encontrado!");
        }

        Loan loan = loanOptional.get();

        // Atualizar a data de devolução
        loan.setReturn_date(returnDate);

        // Atualizar o status com base na devolução
        if (returnDate == null) {
            loan.setStatus(Status.EMPRESTADO);
        } else {
            loan.setStatus(Status.PRESENTE);
        }

        return loanRepository.save(loan);
    }

    // 3. Verificar se o empréstimo está ativo
    public boolean isActive(Loan loan) {
        return loan.getReturn_date() == null;
    }

    // 4. Listar todos os empréstimos
    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    // 5. Buscar empréstimos por usuário
    public List<Loan> getLoansByUser(Long userId) {
        return loanRepository.findByUserId(userId);
    }

    // 6. Buscar empréstimos por livro
    public List<Loan> getLoansByBook(Long bookId) {
        return loanRepository.findByBookId(bookId);
    }

    // 7. Excluir empréstimo (cancelar empréstimo)
    public void deleteLoan(Long loanId) {
        if (!loanRepository.existsById(loanId)) {
            throw new RuntimeException("Empréstimo não encontrado!");
        }
        loanRepository.deleteById(loanId);
    }

    // Validação da data de empréstimo
    private void validateLoanDate(LocalDate loanDate) {
        if (loanDate.isAfter(LocalDate.now())) {
            throw new RuntimeException("A data de empréstimo não pode ser maior que o dia atual!");
        }
    }
}
