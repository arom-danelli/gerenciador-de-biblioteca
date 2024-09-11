package com.elotech.biblioteca_arom.repositories;

import com.elotech.biblioteca_arom.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByCategoryInAndIdNotIn(Set<String> categories, List<Long> excludedIds);

}
