package com.muslimtrivia.Trivia.game;

import com.muslimtrivia.Trivia.game.DifficultyLevel;
import com.muslimtrivia.Trivia.game.QuestionRepository;
import com.muslimtrivia.Trivia.game.Questions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@RequiredArgsConstructor

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;


    @Transactional
    public Questions addQuestion(Questions question) {
        return questionRepository.save(question);
    }

    @Transactional(readOnly = true)
    public List<Questions> getAllQuestions() {
        return questionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Questions> getQuestionsByDifficulty(DifficultyLevel difficultyLevel) {
        // Here you would add a method to QuestionRepository to find questions by difficulty
        return questionRepository.findByDifficultyLevel(difficultyLevel);
    }

    @Transactional
    public void updateQuestion(Questions question) {
        questionRepository.save(question);
    }

    @Transactional
    public void deleteQuestion(Integer id) {
        questionRepository.deleteById(id);
    }
}
