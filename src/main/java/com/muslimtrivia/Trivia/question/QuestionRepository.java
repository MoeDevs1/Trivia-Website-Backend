package com.muslimtrivia.Trivia.question;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Questions, Integer> {
    List<Questions> findByDifficultyLevel(DifficultyLevel difficultyLevel);

}
