package com.muslimtrivia.Trivia.question;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
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
    public List<Questions> getQuestionsByDifficulty(DifficultyLevel difficultyLevel, int numberOfQuestions) {
        List<Questions> allQuestions = questionRepository.findByDifficultyLevel(difficultyLevel);
        Collections.shuffle(allQuestions); // Shuffle the questions randomly

        // Return a sublist containing the desired number of questions
        return allQuestions.subList(0, numberOfQuestions);
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
