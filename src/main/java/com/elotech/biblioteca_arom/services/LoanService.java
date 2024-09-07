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

/**
 * Serviço responsável pela gestão de empréstimos de livros no sistema de biblioteca.
 * Fornece operações para criar, atualizar, listar e excluir empréstimos,
 * além de verificar se há empréstimos ativos e realizar validações.
 */
@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final BookService bookService;

    /**
     * Construtor que injeta os repositórios e serviços necessários.
     *
     * @param loanRepository o repositório de empréstimos
     * @param bookRepository o repositório de livros
     * @param bookService o serviço de livros para operações relacionadas
     */
    @Autowired
    public LoanService(LoanRepository loanRepository, BookRepository bookRepository, BookService bookService) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.bookService = bookService;
    }

    /**
     * Cria um novo empréstimo de livro.
     * Verifica se o livro já está emprestado e valida a data de empréstimo.
     *
     * @param loan o objeto Loan contendo os detalhes do empréstimo
     * @return o empréstimo criado com o status atualizado
     * @throws RuntimeException se o livro já estiver emprestado ou se a data de empréstimo for inválida
     */
    public Loan createLoan(Loan loan) {
        validateLoanDate(loan.getLoan_date());

        if (bookService.hasActiveLoan(loan.getBook().getId())) {
            throw new RuntimeException("O livro já está emprestado e não pode ser emprestado novamente até ser devolvido.");
        }

        loan.setStatus(Status.EMPRESTADO);
        return loanRepository.save(loan);
    }

    /**
     * Atualiza um empréstimo existente, alterando a data de devolução e o status.
     *
     * @param loanId o ID do empréstimo a ser atualizado
     * @param returnDate a data de devolução a ser registrada
     * @return o empréstimo atualizado
     * @throws RuntimeException se o empréstimo não for encontrado
     */
    public Loan updateLoan(Long loanId, LocalDate returnDate) {
        Optional<Loan> loanOptional = loanRepository.findById(loanId);
        if (loanOptional.isEmpty()) {
            throw new RuntimeException("Empréstimo não encontrado!");
        }

        Loan loan = loanOptional.get();
        loan.setReturn_date(returnDate);

        if (returnDate == null) {
            loan.setStatus(Status.EMPRESTADO);
        } else {
            loan.setStatus(Status.PRESENTE);
        }

        return loanRepository.save(loan);
    }

    /**
     * Valida a data de empréstimo para garantir que não seja uma data futura.
     *
     * @param loanDate a data de empréstimo a ser validada
     * @throws RuntimeException se a data for maior que o dia atual
     */
    private void validateLoanDate(LocalDate loanDate) {
        if (loanDate.isAfter(LocalDate.now())) {
            throw new RuntimeException("A data de empréstimo não pode ser maior que o dia atual!");
        }
    }

    /**
     * Verifica se o empréstimo está ativo (sem data de devolução).
     *
     * @param loan o objeto Loan a ser verificado
     * @return true se o empréstimo estiver ativo, false caso contrário
     */
    public boolean isActive(Loan loan) {
        return loan.getReturn_date() == null;
    }

    /**
     * Retorna todos os empréstimos cadastrados no sistema.
     *
     * @return uma lista contendo todos os empréstimos
     */
    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    /**
     * Retorna todos os empréstimos feitos por um usuário específico.
     *
     * @param userId o ID do usuário
     * @return uma lista de empréstimos associados ao usuário
     */
    public List<Loan> getLoansByUser(Long userId) {
        return loanRepository.findByUserId(userId);
    }

    /**
     * Retorna todos os empréstimos relacionados a um livro específico.
     *
     * @param bookId o ID do livro
     * @return uma lista de empréstimos associados ao livro
     */
    public List<Loan> getLoansByBook(Long bookId) {
        return loanRepository.findByBookId(bookId);
    }

    /**
     * Exclui um empréstimo pelo seu ID.
     *
     * @param loanId o ID do empréstimo a ser excluído
     * @throws RuntimeException se o empréstimo não for encontrado
     */
    public void deleteLoan(Long loanId) {
        if (!loanRepository.existsById(loanId)) {
            throw new RuntimeException("Empréstimo não encontrado!");
        }
        loanRepository.deleteById(loanId);
    }
}
