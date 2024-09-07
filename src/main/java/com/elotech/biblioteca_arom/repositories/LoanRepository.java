package com.elotech.biblioteca_arom.repositories;

import com.elotech.biblioteca_arom.entities.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
}
