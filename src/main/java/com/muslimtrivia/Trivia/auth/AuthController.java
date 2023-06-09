package com.muslimtrivia.Trivia.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @RequestBody RegisterRequest request
    ){
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> register(
            @RequestBody AuthRequest request
    ){
        return ResponseEntity.ok(service.authenticate(request));
    }

    @GetMapping("/google/signup")
    public ResponseEntity<AuthResponse> signudpWithGoogle(
            @RequestParam("code") String code,
            OAuth2AuthenticationToken authenticationToken
    ) {
        String userName = authenticationToken.getPrincipal().getAttribute("name");
        String email = authenticationToken.getPrincipal().getAttribute("email");
        String token = authenticationToken.getPrincipal().getAttribute("sub");

        // Pass the required details to the service for signup
        RegisterRequest request = new RegisterRequest();
        request.setUserName(userName);
        request.setEmail(email);
        request.setPassword(token);
        return ResponseEntity.ok(service.register(request));
    }
}
