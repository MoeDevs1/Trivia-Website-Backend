package com.muslimtrivia.Trivia.user;

import com.muslimtrivia.Trivia.auth.AuthService;
import com.muslimtrivia.Trivia.config.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class UserController {

    private final JwtService service;

    @GetMapping("/user")
    public ResponseEntity<?> getUserFromToken(@RequestHeader(value="Authorization") String token) {
        try {
            String email = service.extractEmail(token.replace("Bearer ", ""));
            String username = service.extractUserName(token.replace("Bearer ", ""));

            Map<String, String> userData = new HashMap<>();
            userData.put("email", email);
            userData.put("username", username);

            return ResponseEntity.ok(userData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to extract user data from token: " + e.getMessage());
        }
    }

}
