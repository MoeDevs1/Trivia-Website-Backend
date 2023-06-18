package com.muslimtrivia.Trivia.game;

import com.muslimtrivia.Trivia.question.QuestionService;
import com.muslimtrivia.Trivia.user.User;
import com.muslimtrivia.Trivia.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class LeaderBoardController {

    private final LeaderBoardService leaderBoardService;
    private final UserService userService;
    private final QuestionService questionService;

    @PostMapping("/leaderboard/{username}")  // Add {username} as a path variable
    public ResponseEntity<?> postScoreToLeaderBoard(@PathVariable String username, @RequestBody LeaderBoardRequest request) {
        User user = userService.getCurrentUser(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        int score = request.getScore();
        leaderBoardService.updateLeaderBoard(user, score);
        return ResponseEntity.ok().build();
    }



}
