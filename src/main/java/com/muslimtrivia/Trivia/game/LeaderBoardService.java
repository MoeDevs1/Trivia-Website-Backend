package com.muslimtrivia.Trivia.game;

import com.muslimtrivia.Trivia.user.User;
import com.muslimtrivia.Trivia.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor

@Service
public class LeaderBoardService {

    private final LeaderBoardRepository gameSessionRepository;
    private final UserRepository userRepository;


    public void updateLeaderBoard(User user, int score) {
        LeaderBoard leaderBoard = LeaderBoard.builder()
                .user(user)
                .build();
        gameSessionRepository.save(leaderBoard);

        user.setScore(user.getScore() + score);
        userRepository.save(user);
    }

}