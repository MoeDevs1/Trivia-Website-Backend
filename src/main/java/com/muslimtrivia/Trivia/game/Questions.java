package com.muslimtrivia.Trivia.game;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Questions {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private String questionText;

    @Column(nullable = false)
    private String correctAnswer;

    @Enumerated(EnumType.STRING)
    private DifficultyLevel difficultyLevel;

    @ElementCollection
    private List<String> options;
}
