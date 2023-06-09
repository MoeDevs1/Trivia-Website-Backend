

  package com.muslimtrivia.Trivia.auth;

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
                    .userName((request.getUserName()))
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.USER)
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


    }
