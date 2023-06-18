package com.muslimtrivia.Trivia.game;

import com.muslimtrivia.Trivia.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaderBoardRepository extends JpaRepository<LeaderBoard, Integer> {
    List<LeaderBoard> findByUser(User user);

}
