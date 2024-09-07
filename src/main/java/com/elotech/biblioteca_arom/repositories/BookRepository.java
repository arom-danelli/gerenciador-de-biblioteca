package com.elotech.biblioteca_arom.repositories;

import com.elotech.biblioteca_arom.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}
