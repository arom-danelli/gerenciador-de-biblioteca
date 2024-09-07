package com.elotech.biblioteca_arom.repositories;

import com.elotech.biblioteca_arom.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
