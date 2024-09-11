package com.elotech.biblioteca_arom.repositories;

import com.elotech.biblioteca_arom.entities.Loan;
import com.elotech.biblioteca_arom.entities.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    List<Loan> findByUserId(Long userId);

    List<Loan> findByBookId(Long bookId);

    List<Loan> findByBookIdAndStatus(Long bookId, Status status);


}
