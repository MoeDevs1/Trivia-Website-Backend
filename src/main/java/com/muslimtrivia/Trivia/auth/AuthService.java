package com.muslimtrivia.Trivia.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.muslimtrivia.Trivia.config.JwtService;
import com.muslimtrivia.Trivia.user.Role;
import com.muslimtrivia.Trivia.user.User;
import com.muslimtrivia.Trivia.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if (repository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already registered. Please choose a different email.");
        }

        if (repository.existsByUserName(request.getUserName())) {
            throw new RuntimeException("Username is already taken. Please choose a different username.");
        }

        var user = User.builder()
                .email(request.getEmail())
                .userName(request.getUserName())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .flag(request.getFlag()) // Set the flag value to United States

                .build();
        repository.save(user);
        var jwtToken = jwtService.tokenGenerator(user);
        return new AuthResponse().builder()
                .token(jwtToken)
                .build();
    }

    public AuthResponse authenticate(AuthRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            var user = repository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            var jwtToken = jwtService.tokenGenerator(user);
            return new AuthResponse().builder()
                    .token(jwtToken)
                    .build();
        } catch (UsernameNotFoundException e) {
            throw new RuntimeException("User not found. Please check your email.");
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid credentials. Email or password is incorrect.");
        } catch (Exception e) {
            throw new RuntimeException("Authentication failed: " + e.getMessage());
        }
    }

    public AuthResponse registerGoogle(GoogleRegisterRequest request) throws GeneralSecurityException, IOException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                .setAudience(Collections.singletonList("955085585863-ot8g3rsrvc09ekpiifs07roalvaq5p5j.apps.googleusercontent.com")) // Use your client ID here
                .build();

        GoogleIdToken idToken = verifier.verify(request.getToken());
        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            String firstName = (String) payload.get("given_name"); // Extract the user's first name from the payload
            String flag = "United States"; // Set the default flag value to United States

            if (repository.existsByEmail(email)) {
                throw new RuntimeException("Email is already registered. Please choose a different email.");
            }

            String userName = generateUniqueUsername(firstName);
            var user = User.builder()
                    .email(email)
                    .userName(userName)
                    .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                    .role(Role.USER)
                    .googleSignup(true)
                    .flag(flag) // Set the flag value to United States
                    .build();

            repository.save(user);
            var jwtToken = jwtService.tokenGenerator(user);
            return new AuthResponse().builder()
                    .token(jwtToken)
                    .build();
        } else {
            throw new RuntimeException("Invalid Google token.");
        }
    }

    public AuthResponse loginGoogle(GoogleRegisterRequest request) throws GeneralSecurityException, IOException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                .setAudience(Collections.singletonList("955085585863-ot8g3rsrvc09ekpiifs07roalvaq5p5j.apps.googleusercontent.com"))
                .build();

        GoogleIdToken idToken = verifier.verify(request.getToken());
        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();

            // Check if the user exists in your system
            var user = repository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            // Generate the JWT token
            var jwtToken = jwtService.tokenGenerator(user);
            return new AuthResponse().builder()
                    .token(jwtToken)
                    .build();
        } else {
            throw new RuntimeException("Invalid Google token.");
        }
    }

    private String generateUniqueUsername(String firstName) {
        String baseUsername = firstName.replaceAll("\\s+", "").toLowerCase();
        String username = baseUsername;
        int count = 1;
        while (repository.existsByUserName(username)) {
            username = baseUsername + count;
            count++;
        }
        return username;
    }

}
