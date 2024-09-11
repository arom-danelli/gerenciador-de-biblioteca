package com.elotech.biblioteca_arom.services;

import com.elotech.biblioteca_arom.dtos.LoanDTO;
import com.elotech.biblioteca_arom.entities.Book;
import com.elotech.biblioteca_arom.entities.Loan;
import com.elotech.biblioteca_arom.entities.enums.Status;
import com.elotech.biblioteca_arom.repositories.LoanRepository;
import com.elotech.biblioteca_arom.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Serviço responsável pela gestão de empréstimos de livros no sistema de biblioteca.
 * Fornece operações para criar, atualizar, listar e excluir empréstimos,
 * além de verificar se há empréstimos ativos e realizar validações.
 */
@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;


    /**
     * Construtor que injeta os repositórios e serviços necessários.
     *
     * @param loanRepository o repositório de empréstimos
     * @param bookRepository o repositório de livros
     * @param bookService    o serviço de livros para operações relacionadas
     */
    @Autowired
    public LoanService(LoanRepository loanRepository, BookRepository bookRepository, BookService bookService) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
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
        if (loan.getLoan_date() != null && loan.getLoan_date().isBefore(LocalDate.now())) {
            throw new RuntimeException("A data de empréstimo não pode ser no passado!");
        }

        if (loan.getLoan_date() == null) {
            loan.setLoan_date(LocalDate.now());
        }

        List<Loan> activeLoans = loanRepository.findByBookIdAndStatus(loan.getBook().getId(), Status.EMPRESTADO);
        if (!activeLoans.isEmpty()) {
            throw new RuntimeException("O livro já está emprestado!");
        }

        if (loan.getReturn_date() != null && loan.getReturn_date().isBefore(loan.getLoan_date())) {
            throw new RuntimeException("A data de devolução não pode ser anterior à data de empréstimo!");
        }

        loan.setStatus(Status.EMPRESTADO);

        return loanRepository.save(loan);
    }

    /**
     * Atualiza um empréstimo existente, alterando a data de devolução e o status.
     *
     * @param loanId     o ID do empréstimo a ser atualizado
     * @param returnDate a data de devolução a ser registrada
     * @return o empréstimo atualizado
     * @throws RuntimeException se o empréstimo não for encontrado
     */
    public Loan updateLoan(Long loanId, LocalDate returnDate, Status status) {
        Optional<Loan> loanOptional = loanRepository.findById(loanId);
        if (loanOptional.isEmpty()) {
            throw new RuntimeException("Empréstimo não encontrado!");
        }
        Loan loan = loanOptional.get();
        loan.setReturn_date(returnDate);

        if (status != null) {
            loan.setStatus(status);
        } else {
            loan.setStatus(returnDate == null ? Status.EMPRESTADO : Status.PRESENTE);
        }

        return loanRepository.save(loan);
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

    /**
     * Gera uma lista de recomendações de livros para um usuário com base nas categorias
     * dos livros que ele já pegou emprestado. O método busca livros na mesma categoria
     * que o usuário já emprestou, mas que ele ainda não pegou emprestado.
     *
     * @param userId o ID do usuário para o qual as recomendações serão geradas
     * @return Uma lista de objetos Book recomendados ao usuário, com base nas categorias
     * dos livros que ele já pegou emprestado, excluindo os livros que ele já pegou.
     */
    public List<Book> recomendBooksForUser(Long userId) {
        List<Loan> userLoans = loanRepository.findByUserId(userId);

        Set<String> borrowedCategories = userLoans.stream()
                .map(loan -> loan.getBook().getCategory())
                .collect(Collectors.toSet());

        return bookRepository.findByCategoryInAndIdNotIn(borrowedCategories,
                userLoans.stream().map(loan -> loan.getBook().getId()).collect(Collectors.toList()));
    }

    /**
     * Retorna os detalhes de todos os empréstimos cadastrados.
     * Esse método busca todos os empréstimos registrados no repositório,
     * converte cada um em um objeto `LoanDTO` contendo as principais informações do empréstimo.
     *
     * @return uma lista de objetos `LoanDTO`, onde cada DTO contém o ID do empréstimo, data de empréstimo,
     * data de devolução (se aplicável), status do empréstimo, nome do usuário e título do livro.
     */
    public List<LoanDTO> getAllLoanDetails() {
        List<Loan> loans = loanRepository.findAll();

        return loans.stream()
                .map(loan -> new LoanDTO(
                        loan.getId(),
                        loan.getLoan_date().toString(),
                        loan.getReturn_date() != null ? loan.getReturn_date().toString() : null,
                        loan.getStatus().name(),
                        loan.getUser().getName(),
                        loan.getBook().getTitle()
                ))
                .collect(Collectors.toList());
    }

}
