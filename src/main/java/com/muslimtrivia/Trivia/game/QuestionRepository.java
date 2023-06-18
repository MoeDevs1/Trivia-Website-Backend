package com.muslimtrivia.Trivia.game;

import com.muslimtrivia.Trivia.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Questions, Integer> {
    List<Questions> findByDifficultyLevel(DifficultyLevel difficultyLevel);

}
