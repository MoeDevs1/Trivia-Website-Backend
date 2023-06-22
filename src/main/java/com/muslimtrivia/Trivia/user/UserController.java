package com.muslimtrivia.Trivia.user;

import com.muslimtrivia.Trivia.auth.AuthService;
import com.muslimtrivia.Trivia.config.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class UserController {

    private final JwtService jwtService;
    private final UserService userService;
    private final UserRepository userRepository;


    @GetMapping("/topUsers")
    public ResponseEntity<List<TopUserDTO>> getTopUsers() {
        try {
            List<TopUserDTO> topUsers = userService.getTopUsers();
            return ResponseEntity.ok(topUsers);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(List.of());
        }
    }



    @GetMapping("/user")
    public ResponseEntity<?> getUserFromToken(@RequestHeader(value = "Authorization") String token) {
        try {
            Map<String, String> userData = userService.extractUserDataFromToken(token.replace("Bearer ", ""));
            return ResponseEntity.ok(userData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to extract user data from token: " + e.getMessage());
        }

    }

    @PutMapping("/changeUsername")
    public ResponseEntity<Map<String, String>> changeUserName(@RequestHeader(value = "Authorization") String token,
                                                              @RequestBody ChangeUsernameRequest request) {
        try {
            return userService.changeUserName(jwtService.extractUserName(token.replace("Bearer ", "")), request);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to update username: " + e.getMessage()));
        }
    }

    @PutMapping("/changeFlag")
    public ResponseEntity<Map<String, String>> changeUserFlag(@RequestHeader(value = "Authorization") String token,
                                                              @RequestBody ChangeFlagRequest request) {
        try {
            return userService.changeUserFlag(jwtService.extractUserName(token.replace("Bearer ", "")), request);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to update flag: " + e.getMessage()));
        }
    }

    @PutMapping("/changeEmail")
    public ResponseEntity<Map<String, String>> changeUserEmail(@RequestHeader(value = "Authorization") String token,
                                                               @RequestBody ChangeEmailRequest request) {
        try {
            return userService.changeUserEmail(jwtService.extractUserName(token.replace("Bearer ", "")), request);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to update email: " + e.getMessage()));
        }
    }

    @PutMapping("/changePassword")
    public ResponseEntity<Map<String, String>> changeUserPassword(@RequestHeader(value = "Authorization") String token,
                                                                  @RequestBody ChangePasswordRequest request) {
        try {
            return userService.changeUserPassword(jwtService.extractUserName(token.replace("Bearer ", "")), request);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to update password: " + e.getMessage()));
        }
    }


}
