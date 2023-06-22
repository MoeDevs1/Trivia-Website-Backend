package com.muslimtrivia.Trivia.user;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

   Optional<User> findByEmail(String email);
   boolean existsByEmail(String email);
   Optional<User> findByUserName(String userName);
   boolean existsByUserName(String userName);

   @Query(value = "SELECT u FROM User u ORDER BY u.score DESC")
   List<User> findTopUsers(Pageable pageable);
}
