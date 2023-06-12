package com.muslimtrivia.Trivia.user;

import com.muslimtrivia.Trivia.config.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Map<String, String> extractUserDataFromToken(String token) throws Exception {
        String email = jwtService.extractEmail(token);
        String username = jwtService.extractUserName(token);
        String flag = jwtService.extractFlag(token);

        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("username", username);
        userData.put("flag", flag);

        return userData;
    }





    public ResponseEntity<Map<String, String>> changeUserName(String username, ChangeUsernameRequest request) {
        if (userRepository.existsByUserName(request.getNewUserName())) {
            throw new RuntimeException("Username is already taken. Please choose a different username.");
        }

        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUserName(request.getNewUserName());
        userRepository.save(user);

        var jwtToken = jwtService.tokenGenerator(user);
        Map<String, String> result = new HashMap<>();
        result.put("token", jwtToken);
        return ResponseEntity.ok(result);
    }

    public ResponseEntity<Map<String, String>> changeUserFlag(String username, ChangeFlagRequest request) {
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFlag(request.getNewFlag());
        userRepository.save(user);

        var jwtToken = jwtService.tokenGenerator(user);
        Map<String, String> result = new HashMap<>();
        result.put("token", jwtToken);
        return ResponseEntity.ok(result);
    }
    public ResponseEntity<Map<String, String>> changeUserEmail(String username, ChangeEmailRequest request) {
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getEmail().equals(request.getOldEmail())) {
            throw new RuntimeException("Old email does not match the current email");
        }

        if (userRepository.existsByEmail(request.getNewEmail())) {
            throw new RuntimeException("Email is already registered. Please choose a different email.");
        }

        user.setEmail(request.getNewEmail());
        userRepository.save(user);

        var jwtToken = jwtService.tokenGenerator(user);
        Map<String, String> result = new HashMap<>();
        result.put("token", jwtToken);
        return ResponseEntity.ok(result);
    }

    public ResponseEntity<Map<String, String>> changeUserPassword(String username, ChangePasswordRequest request) {
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        var jwtToken = jwtService.tokenGenerator(user);
        Map<String, String> result = new HashMap<>();
        result.put("token", jwtToken);
        return ResponseEntity.ok(result);
    }

}


