package com.elotech.biblioteca_arom.services;

import com.elotech.biblioteca_arom.entities.Book;
import com.elotech.biblioteca_arom.entities.Loan;
import com.elotech.biblioteca_arom.entities.User;
import com.elotech.biblioteca_arom.entities.enums.Status;
import com.elotech.biblioteca_arom.repositories.LoanRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes para a classe LoanService, cobrindo as operações de criação, atualização,
 * consulta e exclusão de empréstimos.
 */
@ExtendWith(MockitoExtension.class)
public class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private BookService bookService;

    @InjectMocks
    private LoanService loanService;

    private final User user = new User(1L, "Miquella the Kind", "miquella@kind.com", LocalDate.of(2019, 12, 31), "123456789", null);
    private final Book book = new Book(1L, "Neon Genesis Evangelion", "Hideaki Anno", "123456789", "1994-12-26", "Fiction", null);
    private final Loan loan = new Loan(1L, user, book, LocalDate.of(2023, 9, 1), null, Status.EMPRESTADO);

    /**
     * Testa a criação de um empréstimo quando o livro ainda não foi emprestado.
     * Verifica se o status do empréstimo é definido como 'EMPRESTADO' e se
     * não há data de devolução atribuída.
     */
    @Test
    public void testCreateLoan_whenBookIsNotLoaned() {
        when(bookService.hasActiveLoan(book.getId())).thenReturn(false);
        when(loanRepository.save(any(Loan.class))).thenReturn(loan);

        Loan createdLoan = loanService.createLoan(loan);

        assertNotNull(createdLoan);
        assertEquals(Status.EMPRESTADO, createdLoan.getStatus());
        assertNull(createdLoan.getReturn_date());

        verify(bookService, times(1)).hasActiveLoan(book.getId());
        verify(loanRepository, times(1)).save(loan);
    }

    /**
     * Testa a criação de um empréstimo quando o livro já está emprestado.
     * Verifica se o sistema lança uma exceção ao tentar criar o empréstimo.
     */
    @Test
    public void testCreateLoan_whenBookIsAlreadyLoaned() {
        when(bookService.hasActiveLoan(book.getId())).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> loanService.createLoan(loan));

        assertEquals("O livro já está emprestado e não pode ser emprestado novamente até ser devolvido.", exception.getMessage());
        verify(loanRepository, never()).save(any(Loan.class));
    }

    /**
     * Testa a criação de um empréstimo quando a data de empréstimo é maior que o dia atual.
     * Verifica se o sistema lança uma exceção para garantir que a data de empréstimo é válida.
     */
    @Test
    public void testCreateLoan_whenLoanDateIsInFuture() {
        loan.setLoan_date(LocalDate.now().plusDays(1));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> loanService.createLoan(loan));

        assertEquals("A data de empréstimo não pode ser maior que o dia atual!", exception.getMessage());
        verify(loanRepository, never()).save(any(Loan.class));
    }

    /**
     * Testa a atualização de um empréstimo fornecendo a data de devolução.
     * Verifica se o status e a data de devolução são atualizados corretamente.
     */
    @Test
    public void testUpdateLoan_whenReturnDateIsProvided() {
        when(loanRepository.findById(loan.getId())).thenReturn(Optional.of(loan));

        LocalDate returnDate = LocalDate.of(2023, 9, 10);
        loan.setReturn_date(returnDate);
        loan.setStatus(Status.PRESENTE);

        when(loanRepository.save(any(Loan.class))).thenReturn(loan);

        Loan updatedLoan = loanService.updateLoan(loan.getId(), returnDate);

        assertNotNull(updatedLoan, "O empréstimo atualizado não deve ser nulo");
        assertEquals(returnDate, updatedLoan.getReturn_date());
        assertEquals(Status.PRESENTE, updatedLoan.getStatus());

        verify(loanRepository, times(1)).findById(loan.getId());
        verify(loanRepository, times(1)).save(loan);
    }

    /**
     * Testa a recuperação de todos os empréstimos.
     * Verifica se a lista de empréstimos é retornada corretamente.
     */
    @Test
    public void testGetAllLoans() {
        Loan loan1 = new Loan(1L, user, book, LocalDate.of(2023, 9, 1), null, Status.EMPRESTADO);
        Loan loan2 = new Loan(2L, user, book, LocalDate.of(2023, 9, 5), null, Status.EMPRESTADO);

        List<Loan> loans = Arrays.asList(loan1, loan2);
        when(loanRepository.findAll()).thenReturn(loans);

        List<Loan> result = loanService.getAllLoans();

        assertEquals(2, result.size());
        verify(loanRepository, times(1)).findAll();
    }

    /**
     * Testa a recuperação de empréstimos por usuário.
     * Verifica se a lista de empréstimos de um usuário específico é retornada corretamente.
     */
    @Test
    public void testGetLoansByUser() {
        when(loanRepository.findByUserId(user.getId())).thenReturn(Collections.singletonList(loan));

        List<Loan> userLoans = loanService.getLoansByUser(user.getId());

        assertNotNull(userLoans);
        assertEquals(1, userLoans.size());
        verify(loanRepository, times(1)).findByUserId(user.getId());
    }

    /**
     * Testa a exclusão de um empréstimo.
     * Verifica se o empréstimo é removido corretamente do repositório.
     */
    @Test
    public void testDeleteLoan() {
        Long loanId = 1L;

        when(loanRepository.existsById(loanId)).thenReturn(true);

        doNothing().when(loanRepository).deleteById(loanId);

        loanService.deleteLoan(loanId);

        verify(loanRepository, times(1)).existsById(loanId);
        verify(loanRepository, times(1)).deleteById(loanId);
    }
}

